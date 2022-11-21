/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.inspections

class RsAssignInMatchGuardInspectionTest : RsInspectionsTestBase(RsAssignInMatchGuardInspection::class) {
    fun `test E0510 assign to matched value simple`() = checkByText(
        """
        fn main() {
            let mut x = Some(0);
            match x {
                None => {}
                Some(_) if {
                    <error descr="The matched value was assigned in a match guard [E0510]">x = None</error>;
                    false
                } => {}
                Some(_) => {}
            }
        }
    """
    )

    fun `test E0510 with dereference`() = checkByText(
        """
        fn main() {
            let mut x = &Some(0);
            match *x {
                None => (),
                Some(_) if {
                    <error descr="The matched value was assigned in a match guard [E0510]">x = &None</error>;
                    false
                } => (),
                Some(_) => (),
             }
        }
    """
    )
}
