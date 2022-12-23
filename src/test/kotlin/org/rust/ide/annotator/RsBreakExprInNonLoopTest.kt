/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.annotator

class RsBreakExprInNonLoopTest: RsAnnotatorTestBase(RsSyntaxErrorsAnnotator::class) {
    fun `test E0571 loop break value`() = checkByText("""
        fn main() {
            'while_loop: while true {
                <error descr="`break` with value from a `while` loop [E0571]">break ()</error>;
                loop {
                    <error descr="`break` with value from a `while` loop [E0571]">break 'while_loop 123</error>;
                };
            }

            while let Some(_) = Some(()) {
                if <error descr="`break` with value from a `while` loop [E0571]">break ()</error> {
                }
            }

            while let Some(_) = Some(()) {
                <error descr="`break` with value from a `while` loop [E0571]">break None</error>;
            }

            'while_let_loop: while let Some(_) = Some(()) {
                loop {
                    <error descr="`break` with value from a `while` loop [E0571]">break 'while_let_loop "nope"</error>;
                };
            }

            for _ in &[1,2,3] {
                <error descr="`break` with value from a `for` loop [E0571]">break ()</error>;
                <error descr="`break` with value from a `for` loop [E0571]">break [()]</error>;
            }

            'for_loop: for _ in &[1,2,3] {
                loop {
                    break Some(3);
                    <error descr="`break` with value from a `for` loop [E0571]">break 'for_loop Some(17)</error>;
                };
            }
        }
    """)
}
