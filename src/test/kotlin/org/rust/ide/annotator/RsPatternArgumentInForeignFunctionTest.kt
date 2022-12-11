/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

class RsPatternArgumentInForeignFunctionTest: RsAnnotatorTestBase(RsSyntaxErrorsAnnotator::class) {
    fun `test E0130 tuple pattern`() = checkByText("""
        extern "C" {
            fn foo(<error descr="Patterns aren't allowed in foreign function declarations [E0130]">(a, b): (u32, u32)</error>);
        }
    """)

    fun `test E0130 tuple struct pattern`() = checkByText("""
        struct MyInt(i32);

        extern "C" {
            fn foo(<error descr="Patterns aren't allowed in foreign function declarations [E0130]">MyInt(x): MyInt</error>);
        }
    """)

    fun `test E0130 struct pattern`() = checkByText("""
        struct Point {
            x: i32,
            y: i32,
        }

        extern "C" {
            fn foo(<error descr="Patterns aren't allowed in foreign function declarations [E0130]">Point { x, y }: Point</error>);
        }
    """)

    fun `test E0130 array pattern`() = checkByText("""
        extern "C" {
            fn foo(<error descr="Patterns aren't allowed in foreign function declarations [E0130]">[x, ..]: [i32; 3]</error>);
        }
    """)

    fun `test E0130 allow all cases without extern`() = checkByText("""
        struct MyInt(i32);

        fn foo(MyInt(x): MyInt) {}

        struct Point {
            x: i32,
            y: i32,
        }

        fn bar(Point { x, y }: Point) {}

        fn baz([x, ..]: [i32; 3]) {}

        fn qux(a: i32) {}
    """)

    fun `test E0130 allow argument without pattern`() = checkByText("""
        extern "C" {
            fn foo(a: i32);
        }
    """)
}
