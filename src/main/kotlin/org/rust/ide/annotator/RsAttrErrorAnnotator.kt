/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.psi.PsiElement
import org.rust.ide.inspections.fixes.SubstituteTextFix
import org.rust.lang.core.psi.*
import org.rust.lang.core.psi.ext.elementType
import org.rust.lang.utils.RsDiagnostic
import org.rust.lang.utils.addToHolder

class RsAttrErrorAnnotator : AnnotatorBase() {
    override fun annotateInternal(element: PsiElement, holder: AnnotationHolder) {
        if (element !is RsMetaItem) return
        checkMetaBadDelim(element, holder)
        checkAttrTemplateCompatible(element, holder)
    }
}


private fun checkAttrTemplateCompatible(metaItem: RsMetaItem, holder: AnnotationHolder) {
    val name = (metaItem.path ?: return).text
    val attrInfo = (RS_BUILTIN_ATTRIBUTES[name] as? BuiltinAttributeInfo) ?: return
    val template = attrInfo.template
    val argsPresent = metaItem.metaItemArgs?.litExprList?.isEmpty() ?: false ||
        metaItem.metaItemArgs?.metaItemList?.isEmpty() ?: false
    if (argsPresent) {
        if (template.list == null) {
            emitMalformedAttribute(metaItem, name, template, holder)
        }
    } else if (metaItem.eq != null) {
        if (template.nameValueStr == null) {
            emitMalformedAttribute(metaItem, name, template, holder)
        }
    } else if (!template.word) {
        emitMalformedAttribute(metaItem, name, template, holder)
    }
}

private fun emitMalformedAttribute(metaItem: RsMetaItem, name: String, template: AttributeTemplate, holder: AnnotationHolder) {
    val inner = if (metaItem.context is RsInnerAttr) "!" else ""
    var first = true
    val stringBuilder = StringBuilder()
    if (template.word) {
        first = false
        stringBuilder.append("#${inner}[${name}]")
    }
    if (template.list != null) {
        if (!first) stringBuilder.append(" or ")
        first = false
        stringBuilder.append("#${inner}[${name}(${template.list})]")
    }
    if (template.nameValueStr != null) {
        if (!first) stringBuilder.append(" or ")
        stringBuilder.append("#${inner}[${name} = ${template.nameValueStr}]")
    }
    val msg = if (first) "Must be of the form" else "The following are the possible correct uses"
    RsDiagnostic.MalformedAttributeInput(metaItem, name, "$msg $stringBuilder").addToHolder(holder)
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
    val openDelim = element.compactTT?.children?.first {
        it.elementType == RsElementTypes.EQ ||
            it.elementType == RsElementTypes.LBRACE ||
            it.elementType == RsElementTypes.LBRACK
    }
        ?: return
    if (openDelim.elementType == RsElementTypes.EQ) return
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
