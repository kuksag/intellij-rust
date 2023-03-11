/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.intentions


class RemoveDbgIntentionTest : RsIntentionTestBase(RemoveDbgIntention::class) {

    fun `test remove dbg! from expr`() = doAvailableTest("""
        fn test() {
            let a = 1 + dbg!(3/*caret*/);
        }
    """, """
        fn test() {
            let a = 1 + 3/*caret*/;
        }
    """)

    fun `test remove dbg! from stmt`() = doAvailableTest("""
        fn test() {
            dbg!(3/*caret*/);
        }
    """, """
        fn test() {
            3/*caret*/;
        }
    """)

    fun `test remove recursive dbg!`() = doAvailableTest("""
        fn test() {
            dbg!(dbg!(3/*caret*/));
        }
    """, """
        fn test() {
            dbg!(3/*caret*/);
        }
    """)

    fun `test remove dbg! from function parameter`() = doAvailableTest("""
        fn f1(a: usize, b: usize) {}

        fn test() {
            f1(1 + dbg!((3 + 1/*caret*/) * 2)), dbg!(10));
        }
    """, """
        fn f1(a: usize, b: usize) {}

        fn test() {
            f1(1 + ((3 + 1/*caret*/) * 2)), dbg!(10));
        }
    """)

    fun `test remove dbg!`() = doAvailableTest("""
        fn f1(a: usize) {}

        fn test() {
            dbg!(f1(3/*caret*/));
        }
    """, """
        fn f1(a: usize) {}

        fn test() {
            f1(3/*caret*/);
        }
    """)


    fun `test remove dbg! 2`() = doAvailableTest("""
        fn test() {
            let a = dbg!(dbg!(1) + 3/*caret*/);
        }
    """, """
        fn test() {
            let a = dbg!(1) + 3/*caret*/;
        }
    """)

    fun `test remove outer dbg!`() = doAvailableTest("""
        fn test() {
            let a = dbg!(1 +/*caret*/ dbg!(3));
        }
    """, """
        fn test() {
            let a = 1 +/*caret*/ dbg!(3);
        }
    """)

    fun `test remove dbg! with paren`() = doAvailableTest("""
        fn test() {
            let a = dbg!((1 + 3/*caret*/));
        }
    """, """
        fn test() {
            let a = (1 + 3/*caret*/);
        }
    """)

    fun `test remove dbg! with binary expr`() = doAvailableTest("""
        fn main() {
            assert_eq!(dbg!(/*caret*/1 + 1) * 2, 4);
        }
    """, """
        fn main() {
            assert_eq!((/*caret*/1 + 1) * 2, 4);
        }
    """)

    fun `test remove dbg! with dot expr`() = doAvailableTest("""
        fn main() {
            assert_eq!(dbg!(1i32 - 2/*caret*/).abs(), 1);
        }
    """, """
        fn main() {
            assert_eq!((1i32 - 2/*caret*/).abs(), 1);
        }
    """)

    fun `test cursor in dbg!`() = doAvailableTest("""
        fn test() {
            let a = db/*caret*/g!(1 + 3);
        }
    """, """
        fn test() {
            let a = /*caret*/1 + 3;
        }
    """)

    fun `test cursor in whitespace`() = doAvailableTest("""
        fn test() {
            let a = dbg!(1 + 3             /*caret*/        );
        }
    """, """
        fn test() {
            let a = 1 + 3/*caret*/;
        }
    """)

    fun `test not available for custom dbg! macro`() = doUnavailableTest("""
        macro_rules! dbg {
            ($ e:expr) => { $ e };
        }
        fn test() {
            let a = 1 + dbg!(3/*caret*/);
        }
    """)

    fun `test not available if not expression`() = doUnavailableTest("""
        dbg!(3/*caret*/);
    """)
}
