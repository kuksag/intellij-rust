/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.intentions

import com.intellij.injected.editor.EditorWindow
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.injection.Injectable
import org.intellij.lang.annotations.Language
import org.intellij.plugins.intelliLang.inject.InjectLanguageAction
import org.intellij.plugins.intelliLang.inject.InjectedLanguage
import org.intellij.plugins.intelliLang.inject.UnInjectLanguageAction
import org.rust.SkipTestWrapping
import org.rust.lang.core.psi.ext.ancestorOrSelf

@SkipTestWrapping
class InjectLanguageIntentionTest : RsIntentionTestBase(InjectLanguageAction::class) {
    fun `test available inside string`() = checkAvailable("""
        const C: &str = "/*caret*/";
    """)

    fun `test available before string`() = checkAvailable("""
        const C: &str = /*caret*/"123";
    """)

    fun `test unavailable inside number`() = doUnavailableTest("""
        const C: i32 = /*caret*/123;
    """)

    fun `test available string literals inside macro call bodies`() = checkAvailable("""
        foobar!(/*caret*/"abc(def)");
    """)

    fun `test inject RegExp into string literal`() = doTest("RegExp", """
        const C: &str = /*caret*/"<inject>abc(def)</inject>";
    """)

    fun `test available inside a macro call body`() = checkAvailable("""
        foo!(/*caret*/foo bar baz);
    """)

    fun `test available inside a macro call identifier`() = checkAvailable("""
        foo/*caret*/!(foo bar baz);
    """)

    fun `test unavailable inside a macro call without a body`() = doUnavailableTest("""
        foo/*caret*/!
    """)

    fun `test inject RegExp into macro call body 1`() = doTest("RegExp", """
        |foo! {
        |<inject>    /*caret*/abc(def)
        |</inject>}
    """.trimMargin())

    fun `test inject RegExp into macro call body 2`() = doTest("RegExp", """
        |    foo! {
        |<inject>        /*caret*/abc(def)
        |</inject>    }
    """.trimMargin())

    private fun checkAvailable(@Language("Rust") code: String) {
        InlineFile(code.trimIndent()).withCaret()
        check(intention.isAvailable(project, myFixture.editor, myFixture.file)) {
            "Intention is not available"
        }
    }

    private fun doTest(lang: String, @Language("Rust") code: String) {
        InlineFile(code.trimIndent()).withCaret()
        val language = InjectedLanguage.findLanguageById(lang)
        val injectable = Injectable.fromLanguage(language)
        InjectLanguageAction.invokeImpl(project, myFixture.editor, myFixture.file, injectable)
        try {
            val host = findInjectionHost(myFixture.editor, myFixture.file)
                ?: error("InjectionHost not found")
            val injectedList = InjectedLanguageManager.getInstance(project).getInjectedPsiFiles(host)
                ?: error("Language injection failed")
            assertEquals(injectedList.size, 1)
            val injectedPsi = injectedList.first().first
            assertEquals(injectedPsi.language, language)
            myFixture.checkHighlighting(false, true, false, false)
        } finally {
            UnInjectLanguageAction.invokeImpl(project, myFixture.editor, myFixture.file)
        }
    }
}

private fun findInjectionHost(editor: Editor, file: PsiFile): PsiLanguageInjectionHost? {
    if (editor is EditorWindow) return null
    val offset = editor.caretModel.offset
    val vp = file.viewProvider
    for (language in vp.languages) {
        val host = vp.findElementAt(offset, language)
            ?.ancestorOrSelf<PsiLanguageInjectionHost>()
        if (host != null && host.isValidHost) {
            return host
        }
    }
    return null
}
