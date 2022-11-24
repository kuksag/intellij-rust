/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.inspections

class RsConstOrTypeParamsInExternTest : RsInspectionsTestBase(RsConstOrTypeParamsInExternInspection::class) {
    fun `test E0044 type param`() = checkByText(
        """
            extern "C" {
                fn foo<error descr="Foreign items may not have type parameters [E0044]"><T></error>();
            }

            fn main() {
            }
        """
    )

    fun `test E0044 const type param`() = checkByText(
        """
            extern "C" {
                fn foo<error descr="Foreign items may not have type parameters [E0044]"><const X: usize></error>();
            }

            fn main() {
            }
        """
    )

    fun `test E0044 type and const type params`() = checkByText(
        """
            extern "C" {
                fn foo<error descr="Foreign items may not have type parameters [E0044]"><T, const X: usize></error>();
            }

            fn main() {
            }
        """
    )
}
