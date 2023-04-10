/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package org.rust.ide.intentions

import org.rust.SkipTestWrapping

@SkipTestWrapping
class NestUseStatementsIntentionTest : RsIntentionTestBase(NestUseStatementsIntention::class) {
    fun `test simple use statements`() = doAvailableTest("""
        use /*caret*/std::error;
        use std::io;
    """, """
        use /*caret*/std::{
            error,
            io
        };

    """)

    fun `test caret last path`() = doAvailableTest("""
        use std::error/*caret*/;
        use std::io;
    """, """
        use /*caret*/std::{
            error,
            io
        };

    """)

    fun `test example use statements`() = doAvailableTest("""
        use /*caret*/std::error::Error;
        use std::io::Write;
        use std::path::PathBuf;
    """, """
        use /*caret*/std::{
            error::Error,
            io::Write,
            path::PathBuf
        };

    """)

    fun `test nest statements which only be able to nested with selected one`() = doAvailableTest("""
        use /*caret*/std::error::Error;
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """, """
        use /*caret*/std::{
            error::Error,
            io::Write
        };
        use quux::spam;
        use quux::eggs;
    """)

    fun `test inner path`() = doAvailableTest("""
        use std::{
            sync/*caret*/::mpsc,
            sync::Arc
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """, """
        use std::{
            /*caret*/sync::{
                mpsc,
                Arc
            }
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """)

    fun `test inner path without last comma`() = doAvailableTest("""
        use std::{
            sync/*caret*/::mpsc,
            sync::Arc
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """, """
        use std::{
            /*caret*/sync::{
                mpsc,
                Arc
            }
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """)

    fun `test inner path with last position of caret`() = doAvailableTest("""
        use std::{
            sync::mpsc,
            sync::Ar/*caret*/c
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """, """
        use std::{
            /*caret*/sync::{
                mpsc,
                Arc
            }
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """)

    fun `test inner path with many useSpeck`() = doAvailableTest("""
        use std::{
            sync::mpsc,
            sync::Ar/*caret*/c,
            sync::B,
            sync::C,
            sync::D,
            sync::E,
            sync::F,
            sync::G
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """, """
        use std::{
            /*caret*/sync::{
                mpsc,
                Arc,
                B,
                C,
                D,
                E,
                F,
                G
            }
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """)

    fun `test very nested inner path`() = doAvailableTest("""
        use std::{
            b1::{
                c1::{
                    d1::{
                        f::/*caret*/x,
                        f::y
                    },
                    d2
                },
                c2
            },
            b2
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """, """
        use std::{
            b1::{
                c1::{
                    d1::{
                        /*caret*/f::{
                            x,
                            y
                        }
                    },
                    d2
                },
                c2
            },
            b2
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """)

    fun `test very nested inner path but caret is middle of it`() = doAvailableTest("""
        use std::{
            b1::{
                c1/*caret*/::{
                    d1::{
                        f::g1,
                        f::g2
                    },
                    d2
                },
                c1::x,
                c1::y,
                c2::z
            },
            b2
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """, """
        use std::{
            b1::{
                c1::{
                    d1::{
                        f::g1,
                        f::g2
                    },
                    d2,
                    x,
                    y
                },
                c2::z
            },
            b2
        };
        use std::io::Write;
        use quux::spam;
        use quux::eggs;
    """)

    fun `test cannot nest if non common base path exist`() = doUnavailableTest("""
        use a1::/*caret*/b::c;
        use a2::b::c;
    """)

    fun `test starts with colon colon`() = doAvailableTest("""
        use ::a1/*caret*/::b1::c;
        use ::a1::b2::c;
    """, """
        use /*caret*/::a1::{
            b1::c,
            b2::c
        };

    """)

    fun `test starts with colon colon with no colon colon`() = doUnavailableTest("""
        use ::a1/*caret*/::b::c;
        use a1::b::c;
    """)

    fun `test converts module to self`() = doAvailableTest("""
        use foo/*caret*/;
        use foo::foo;
        use foo::bar;
    """, """
        use foo::{
            self,
            foo,
            bar
        };

    """)

    fun `test pub and non-pub on pub`() = doAvailableTest("""
        pub use a::b1;
        use a::b2;
        pub use a::b3/*caret*/;
        use a::b4;
        pub use a::b5;
    """, """
        pub use a::{
            b1,
            b3,
            b5
        };
        use a::b2;
        use a::b4;

    """)

    fun `test pub and non-pub on non-pub`() = doAvailableTest("""
        use a::b1;
        pub use a::b2;
        use a::b3/*caret*/;
        pub use a::b4;
        use a::b5;
    """, """
        use a::{
            b1,
            b3,
            b5
        };
        pub use a::b2;
        pub use a::b4;

    """)

    fun `test keep outer doc comment`() = doAvailableTest("""
        /// My doc comment
        use a/*caret*/::b::c;
        use a::b::d;
    """, """
        /// My doc comment
        use a::{
            b::c,
            b::d
        };

    """)

    fun `test keep middle doc comment`() = doAvailableTest("""
        /// My doc comment
        use a/*caret*/::b::c;
        /// My doc comment 2
        use a::b::d;
    """, """
        /// My doc comment
        /// My doc comment 2
        use a::{
            b::c,
            b::d
        };

    """)

    fun `test keep middle comment`() = doAvailableTest("""
        // My comment
        use a/*caret*/::b::c;
        // My comment 2
        use a::b::d;
    """, """
        // My comment
        // My comment 2
        use a::{
            b::c,
            b::d
        };

    """)

    fun `test imported item with same name as module 1`() = doAvailableTest("""
        use a::/*caret*/a;
        use a::b;
    """, """
        use a::{
            a,
            b
        };

    """)

    fun `test imported item with same name as module 2`() = doAvailableTest("""
        use ::a::/*caret*/a;
        use ::a::b;
    """, """
        use ::a::{
            a,
            b
        };

    """)

    fun `test imported item with same name as module renamed 1`() = doAvailableTest("""
        use a::/*caret*/a as x;
        use a::b;
    """, """
        use a::{
            a as x,
            b
        };

    """)

    fun `test imported item with same name as module renamed 2`() = doAvailableTest("""
        use ::a::/*caret*/a as x;
        use ::a::b;
    """, """
        use ::a::{
            a as x,
            b
        };

    """)

    fun `test renamed import 1`() = doAvailableTest("""
        use foo/*caret*/::bar as baz;
        use foo::quux;
    """, """
        use foo::{
            bar as baz,
            quux
        };

    """)

    fun `test renamed import 2`() = doAvailableTest("""
        use foo/*caret*/::quux;
        use foo::bar as baz;
    """, """
        use foo::{
            quux,
            bar as baz
        };

    """)

    fun `test renamed import 3`() = doAvailableTest("""
        use top::{
            foo::quux,
            foo/*caret*/::bar as baz,
        };
    """, """
        use top::{
            foo::{
                quux,
                bar as baz
            }
        };
    """)

    fun `test renamed import as nesting root 1`() = doAvailableTest("""
        use /*caret*/top as hop;
        use top::foo;
    """, """
        use top::{
            self as hop,
            foo
        };

    """)

    fun `test renamed import as nesting root 2`() = doAvailableTest("""
        use top::{
            /*caret*/foo::bar,
            foo as boo,
        };

    """, """
        use top::{
            foo::{
                bar,
                self as boo
            }
        };

    """)
}
