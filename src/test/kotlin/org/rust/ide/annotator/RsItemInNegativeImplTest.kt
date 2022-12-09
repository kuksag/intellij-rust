/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

class RsItemInNegativeImplTest : RsAnnotatorTestBase(RsSyntaxErrorsAnnotator::class) {
    fun `test E0749 type alias`() = checkByText("""
        trait Foo {
            type Bar;
        }

        impl !Foo for u32 {
            <error descr="Negative impls cannot have any items [E0749]">type Bar = i32;</error>
        }
    """)

    fun `test E0749 function`() = checkByText("""
        trait Foo {
            fn bar();
        }

        impl !Foo for u32 {
            <error descr="Negative impls cannot have any items [E0749]">fn bar() {}</error>
        }
    """)

    fun `test E0749 empty`() = checkByText("""
        trait Foo {
            fn bar();
        }

        impl !Foo for u32 {}
    """)


}
