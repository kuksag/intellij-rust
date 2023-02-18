/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

class RsAttributeContainsMetaItemSeveralTimesTest: RsAnnotatorTestBase(RsSyntaxErrorsAnnotator::class) {
    fun `test E0538`() = checkByText("""
        #[deprecated(
            since="1.0.0",
            note="First deprecation note.",
            <error descr="Multiple 'note' items [E0538]">note="Second deprecation note."</error>
        )]
        fn deprecated_function() {}
    """)

    fun `test E0538 compiler test -- deprecation-sanity`() = checkByText("""
        #[deprecated(
            since = "a",
            <error descr="Multiple 'since' items [E0538]">since = "b"</error>,
            note = "c"
        )]
        fn f1() { }
    """)
}
