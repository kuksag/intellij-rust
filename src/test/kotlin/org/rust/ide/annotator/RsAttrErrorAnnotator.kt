/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

class RsAttrErrorAnnotatorTest : RsAnnotatorTestBase(RsAttrErrorAnnotator::class) {

    fun `test check attributes wrong delimiter`() = checkByText(
        """
        #![crate_type = foo!()]

        #[allow <error descr="Wrong meta list delimiters">{ foo_lint }</error> ]
        fn delim_brace() {}

        #[allow <error descr="Wrong meta list delimiters">[ foo_lint ]</error> ]
        fn delim_bracket() {}

        #[cfg_attr(unix, allow <error descr="Wrong meta list delimiters">{ foo_lint }</error>)]
        fn cfg() {}

        #[cfg_attr(unix, cfg_attr(unix, foo<error descr="Wrong meta list delimiters">{bar}</error>))]
        fn nested_cfg() {}
    """)

    fun `test check attributes template compatibility`() = checkByText(
        """
        #![<error descr="Malformed `recursion_limit` attribute input">recursion_limit</error>]

        #[<error descr="Malformed `rustc_lint_query_instability` attribute input">rustc_lint_query_instability(a)</error>]
        fn f1() {}

        #[<error descr="Malformed `link_name` attribute input">link_name</error>]
        extern "C" {
            fn bar() -> u32;
        }

        #[<error descr="Malformed `marker` attribute input">marker(always)</error>]
        trait Marker1 {}

        #[<error descr="Malformed `marker` attribute input">marker("never")</error>]
        trait Marker2 {}

        #[<error descr="Malformed `marker` attribute input">marker(key = "value")</error>]
        trait Marker3 {}

        trait Foo {}

        #[<error descr="Malformed `rustc_on_unimplemented` attribute input">rustc_on_unimplemented</error>]
        impl Foo for u32 {}

        #[<error descr="Malformed `track_caller` attribute input">track_caller(1)</error>]
        fn f() {}

        #[<error descr="Malformed `rustc_must_implement_one_of` attribute input">rustc_must_implement_one_of</error>]
        trait Tr3 {}
    """)
}
