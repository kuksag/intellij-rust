/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

class RsImplForDynTraitTest : RsAnnotatorTestBase(RsErrorAnnotator::class) {
    fun `test E0785 error when impl for dyn auto trait`() = checkErrors("""
        auto trait AutoTrait {}

        impl <error descr="Cannot define inherent `impl` for a dyn auto trait [E0785]">dyn AutoTrait</error> {}
    """)

    fun `test E0785 no error when impl for dyn trait`() = checkErrors("""
        trait Trait {}

        impl dyn Trait {}
    """)

    fun `test E0785 error when impl for dyn multiple auto traits`() = checkErrors("""
        auto trait Foo {}
        auto trait Bar {}

        impl <error descr="Cannot define inherent `impl` for a dyn auto trait [E0785]">dyn Foo + Bar</error> {}
    """)

    fun `test E0785 no error when impl for dyn multiple auto traits and one regular trait`() = checkErrors("""
        auto trait Foo {}
        auto trait Bar {}
        trait Baz {}

        impl dyn Foo + Bar + Baz {}
    """)

    fun `test E0785 no error when impl for dyn multiple auto traits and multiple regular traits`() = checkErrors("""
        auto trait Foo {}
        auto trait Bar {}
        trait Baz {}
        trait Zab {}

        impl dyn Foo + Bar + Baz + Zab {}
    """)

    fun `test E0785 no error when impl trait for dyn auto trait`() = checkErrors("""
        auto trait Foo {}
        trait Bar {}

        impl Bar for dyn Foo {}
    """)

    fun `test no E0785 when one trait is unresolved`() = checkErrors("""
        auto trait AutoTrait {}

        impl dyn Unresolved + AutoTrait {}
    """)

    fun `test no E0785 when one trait is unresolved and the type is in parens`() = checkErrors("""
        auto trait AutoTrait {}

        impl dyn Unresolved + AutoTrait {}
    """)

    fun `test no E0785 when one trait is unresolved through a type alias`() = checkErrors("""
        auto trait AutoTrait {}
        type AliasedTrait = dyn Unresolved + AutoTrait;
        impl AliasedTrait {}
    """)

    fun `test no E0785 in impl for non-normalized associated type projection`() = checkErrors("""
        auto trait AutoTrait {}
        trait Foo {
            type Item;
        }
        struct S;
        impl Foo for S {
            type Item = dyn AutoTrait;
        }

        impl <error descr="Can impl only `struct`s, `enum`s, `union`s and trait objects [E0118]"><S as Foo>::Item</error> {}
    """)
}
