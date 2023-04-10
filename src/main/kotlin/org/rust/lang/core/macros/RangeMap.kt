/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.lang.core.macros

import com.intellij.openapi.util.TextRange
import com.intellij.util.SmartList
import org.rust.stdext.optimizeList
import org.rust.stdext.readVarInt
import org.rust.stdext.writeVarInt
import java.io.DataInput
import java.io.DataOutput
import kotlin.math.max
import kotlin.math.min

/**
 * Must provide [equals] method because it is used to track changes in the macro expansion mechanism
 */
open class RangeMap(val ranges: List<MappedTextRange>) {

    constructor(range: MappedTextRange) : this(listOf(range))

    constructor(ranges: SmartList<MappedTextRange>) : this(ranges.optimizeList())

    fun isEmpty(): Boolean = ranges.isEmpty()

    fun mapOffsetFromExpansionToCallBody(offset: Int): Int? {
        return mapOffsetFromExpansionToCallBody(offset, false) ?: mapOffsetFromExpansionToCallBody(offset, true)
    }

    fun mapOffsetFromExpansionToCallBody(offset: Int, stickToLeft: Boolean): Int? {
        return ranges.singleOrNull { range ->
            offset >= range.dstOffset && (offset < range.dstEndOffset || stickToLeft && offset == range.dstEndOffset)
        }?.let { range ->
            range.srcOffset + (offset - range.dstOffset)
        }
    }

    fun mapOffsetFromCallBodyToExpansion(offset: Int): List<Int> {
        return ranges.filter { range ->
            offset >= range.srcOffset && offset < range.srcEndOffset
        }.map { range ->
            range.dstOffset + (offset - range.srcOffset)
        }
    }

    fun mapTextRangeFromExpansionToCallBody(toMap: TextRange): List<MappedTextRange> {
        return ranges.mapNotNull { it.dstIntersection(toMap) }
    }

    fun mapMappedTextRangeFromExpansionToCallBody(toMap: MappedTextRange): List<MappedTextRange> {
        return mapTextRangeFromExpansionToCallBody(TextRange(toMap.srcOffset, toMap.srcEndOffset)).map { mapped ->
            MappedTextRange(
                mapped.srcOffset,
                toMap.dstOffset + (mapped.dstOffset - toMap.srcOffset),
                mapped.length
            )
        }
    }

    fun mapAll(other: RangeMap): RangeMap {
        return RangeMap(other.ranges.flatMap(::mapMappedTextRangeFromExpansionToCallBody))
    }

    fun writeTo(data: DataOutput) {
        data.writeInt(ranges.size)
        ranges.forEach {
            data.writeMappedTextRange(it)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RangeMap

        if (ranges != other.ranges) return false

        return true
    }

    override fun hashCode(): Int {
        return ranges.hashCode()
    }

    companion object {
        val EMPTY: RangeMap = RangeMap(emptyList())

        fun readFrom(data: DataInput): RangeMap {
            val size = data.readInt()
            val ranges = (0 until size).map { data.readMappedTextRange() }
            return RangeMap(ranges)
        }
    }
}

private fun DataInput.readMappedTextRange(): MappedTextRange = MappedTextRange(
    readVarInt(),
    readVarInt(),
    readVarInt()
)

private fun DataOutput.writeMappedTextRange(range: MappedTextRange) {
    writeVarInt(range.srcOffset)
    writeVarInt(range.dstOffset)
    writeVarInt(range.length)
}

data class MappedTextRange(
    val srcOffset: Int,
    val dstOffset: Int,
    val length: Int
) {
    init {
        require(srcOffset >= 0) { "srcOffset should be >= 0; got: $srcOffset" }
        require(dstOffset >= 0) { "dstOffset should be >= 0; got: $dstOffset" }
        require(length > 0) { "length should be grater than 0; got: $length" }
    }
}

val MappedTextRange.srcEndOffset: Int get() = srcOffset + length
val MappedTextRange.dstEndOffset: Int get() = dstOffset + length

val MappedTextRange.srcRange: TextRange get() = TextRange(srcOffset, srcOffset + length)
@Suppress("unused")
val MappedTextRange.dstRange: TextRange get() = TextRange(dstOffset, dstOffset + length)

fun MappedTextRange.srcShiftLeft(delta: Int) = copy(srcOffset = srcOffset - delta)
fun MappedTextRange.dstShiftRight(delta: Int) = copy(dstOffset = dstOffset + delta)

fun MappedTextRange.shiftRight(delta: Int) = copy(srcOffset = srcOffset + delta, dstOffset = dstOffset + delta)

fun MappedTextRange.withLength(length: Int) = copy(length = length)

private fun MappedTextRange.dstIntersection(range: TextRange): MappedTextRange? {
    val newDstStart = max(dstOffset, range.startOffset)
    val newDstEnd = min(dstEndOffset, range.endOffset)
    return if (newDstStart < newDstEnd) {
        val srcDelta = newDstStart - dstOffset
        MappedTextRange(
            srcOffset = srcOffset + srcDelta,
            dstOffset = newDstStart,
            length = newDstEnd - newDstStart
        )
    } else {
        null
    }
}

/**
 * Adds the [range] to [this] list or merges [range] with the last list element if they intersect.
 * Used as an optimization to reduce the list size
 */
fun MutableList<MappedTextRange>.mergeAdd(range: MappedTextRange) {
    val last = lastOrNull()

    if (last?.srcEndOffset == range.srcOffset && last.dstEndOffset == range.dstOffset) {
        set(size - 1, MappedTextRange(
            last.srcOffset,
            last.dstOffset,
            last.length + range.length
        ))
    } else {
        add(range)
    }
}
