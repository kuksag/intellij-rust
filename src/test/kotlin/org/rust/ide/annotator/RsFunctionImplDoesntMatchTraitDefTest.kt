/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

class RsFunctionImplDoesntMatchTraitDefTest : RsAnnotatorTestBase(RsErrorAnnotator::class) {
    fun `test E0053 alias`() = checkErrors(
        """
        struct Foo {}

        trait Bar {
            type Baz;
            fn qux(baz: Baz);
        }

        impl Bar for Foo {
            type Baz = i32;
            fn qux(baz: Baz) {}
        }
    """)
}
