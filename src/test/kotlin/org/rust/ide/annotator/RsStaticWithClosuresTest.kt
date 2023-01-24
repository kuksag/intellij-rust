/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

class RsStaticWithClosuresTest: RsAnnotatorTestBase(RsSyntaxErrorsAnnotator::class) {
    fun `test E0697`() = checkByText("""
        fn main() {
            <error descr="Closures cannot be static [E0697]">static</error> || {};
        }
    """)

    fun `test E697 allow generators`() = checkByText("""
        #![feature(generators, generator_trait)]

        fn main() {
            let generator = static || {
                yield 1;
                return "foo"
            };
        }
    """)

    fun `test E697 generators inside a lambda`() = checkByText("""
        #![feature(generators, generator_trait)]

        fn main() {
            let foo = || {
                let generator = static || {
                    yield 1;
                    return "foo"
                };
            };

            let bar = <error descr="Closures cannot be static [E0697]">static</error> || {
                let generator = static || {
                    yield 1;
                    return "foo"
                };
            };
        }
    """)
}
