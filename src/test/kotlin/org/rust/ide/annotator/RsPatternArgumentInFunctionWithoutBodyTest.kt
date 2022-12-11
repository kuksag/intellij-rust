/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

class RsPatternArgumentInFunctionWithoutBodyTest : RsAnnotatorTestBase(RsSyntaxErrorsAnnotator::class) {
    fun `test E0642 allow pattern with body`() = checkByText("""
        struct MyInt(i32);

        trait Foo {
            fn bar(MyInt(a): MyInt) {}
            fn baz((a, b): (i32, i32)) {}
        }
    """)

    fun `test E0642 allow without pattern`() = checkByText("""
        trait Foo {
            fn bar();
            fn baz() {}
        }
    """)

    fun `test E0642`() = checkByText("""
        struct MyInt(i32);

        trait Foo {
            fn bar(<error descr="Patterns aren't allowed in function without body [E0642]">MyInt(a): MyInt</error>);
            fn baz(<error descr="Patterns aren't allowed in function without body [E0642]">(a, b): (i32, i32)</error>);
        }
    """)
}
