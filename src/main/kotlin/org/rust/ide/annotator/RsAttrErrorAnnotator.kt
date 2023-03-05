/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.rust.ide.inspections.fixes.SubstituteTextFix
import org.rust.lang.core.psi.RS_BUILTIN_ATTRIBUTES
import org.rust.lang.core.psi.RsElementTypes
import org.rust.lang.core.psi.RsMetaItem
import org.rust.lang.core.psi.ext.elementType
import org.rust.lang.core.psi.ext.isCfgUnknown
import org.rust.lang.core.psi.ext.isRootMetaItem
import org.rust.lang.core.psi.ext.name
import org.rust.lang.utils.RsDiagnostic
import org.rust.lang.utils.addToHolder

class RsAttrErrorAnnotator : AnnotatorBase() {
    override fun annotateInternal(element: PsiElement, holder: AnnotationHolder) {
        if (element !is RsMetaItem) return
        checkMetaBadDelim(element, holder)
    }
}

private fun setParen(text: String): String {
    val leftIdx = text.indexOfLast { it == '[' || it == '{' }
    val rightIdx = text.indexOfFirst { it == ']' || it == '}' }
    val chars = text.toCharArray()
    chars[leftIdx] = '('
    chars[rightIdx] = ')'
    return chars.concatToString()
}

private fun checkMetaBadDelim(element: RsMetaItem, holder: AnnotationHolder) {
    val openDelim = element.compactTT?.children?.first { it.elementType == RsElementTypes.LBRACE || it.elementType == RsElementTypes.LBRACK }
        ?: return
    val closingDelim = element.compactTT?.children?.last { it.elementType == RsElementTypes.RBRACE || it.elementType == RsElementTypes.RBRACK }
        ?: return
    when (Pair(openDelim.elementType, closingDelim.elementType)) {
        Pair(RsElementTypes.LBRACE, RsElementTypes.RBRACE), Pair(RsElementTypes.LBRACK, RsElementTypes.RBRACK) -> {
            val fixedText = setParen(element.text)
            val fix = SubstituteTextFix.replace(
                "Replace brackets", element.containingFile, element.textRange, fixedText
            )
            RsDiagnostic.WrongMetaDelimiters(openDelim, closingDelim, fix).addToHolder(holder)
        }
    }
}
