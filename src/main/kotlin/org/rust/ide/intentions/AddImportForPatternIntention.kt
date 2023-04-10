/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.intentions

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.rust.ide.inspections.import.AutoImportFix
import org.rust.ide.intentions.AddImportForPatternIntention.Context
import org.rust.ide.intentions.util.macros.InvokeInside
import org.rust.lang.core.psi.RsMatchArm
import org.rust.lang.core.psi.RsPatBinding
import org.rust.lang.core.psi.RsPatIdent
import org.rust.lang.core.psi.ext.ancestorOrSelf
import org.rust.lang.core.psi.ext.ancestorStrict
import org.rust.lang.core.psi.ext.isAncestorOf

class AddImportForPatternIntention : RsElementBaseIntentionAction<Context>() {
    override fun getText(): String = "Import"
    override fun getFamilyName(): String = "Add import for path in pattern"

    // TODO ideally, it should work inside macro expansions, but now it can't work with import chooser
    override val attributeMacroHandlingStrategy: InvokeInside get() = InvokeInside.MACRO_CALL
    override val functionLikeMacroHandlingStrategy: InvokeInside get() = InvokeInside.MACRO_CALL

    override fun startInWriteAction(): Boolean = false
    override fun getElementToMakeWritable(currentFile: PsiFile): PsiFile = currentFile

    class Context(val pat: RsPatIdent, val context: AutoImportFix.Context)

    // enum E { A, B }
    // match e {
    //     A => {}
    //     B => {}
    // }   ^ RsPatBinding
    override fun findApplicableContext(project: Project, editor: Editor, element: PsiElement): Context? {
        val pat = element.ancestorOrSelf<RsPatBinding>() ?: return null
        if (pat.bindingMode != null) return null
        val patIdent = pat.parent as? RsPatIdent ?: return null
        // TODO: Support other patterns (not in match arm)
        val matchArm = patIdent.ancestorStrict<RsMatchArm>() ?: return null
        if (!matchArm.pat.isAncestorOf(patIdent)) return null
        if (pat.reference.multiResolve().isNotEmpty()) return null

        val context = AutoImportFix.findApplicableContext(pat) ?: return null
        return Context(patIdent, context)
    }

    override fun invoke(project: Project, editor: Editor, ctx: Context) {
        AutoImportFix(ctx.pat, ctx.context).invoke(project)
    }
}
