/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

class RsAttrErrorAnnotatorTest: RsAnnotatorTestBase(RsAttrErrorAnnotator::class) {

    fun `test check attributes wrong delimiter`() = checkByText("""
        #[allow <error descr="Wrong meta list delimiters">{ foo_lint }</error> ]
        fn delim_brace() {}

        #[allow <error descr="Wrong meta list delimiters">[ foo_lint ]</error> ]
        fn delim_bracket() {}

        #[cfg_attr(unix, allow <error descr="Wrong meta list delimiters">{ foo_lint }</error>)]
        fn cfg() {}

        #[cfg_attr(unix, cfg_attr(unix, foo<error descr="Wrong meta list delimiters">{bar}</error>))]
        fn nested_cfg() {}
    """)

}
