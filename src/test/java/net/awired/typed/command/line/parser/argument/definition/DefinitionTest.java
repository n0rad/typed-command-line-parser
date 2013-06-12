/**
 *
 *     Copyright (C) Awired.net
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package net.awired.typed.command.line.parser.argument.definition;

import junit.framework.Assert;
import net.awired.typed.command.line.parser.argument.CliArgumentManager;
import net.awired.typed.command.line.parser.argument.args.CliNoParamArgument;
import net.awired.typed.command.line.parser.argument.args.CliOneParamArgument;
import net.awired.typed.command.line.parser.help.ArgRule;
import net.awired.typed.command.line.parser.param.CliParamInt;
import org.junit.Rule;
import org.junit.Test;

public class DefinitionTest {

    @Rule
    public ArgRule<CliArgumentManager> argRule = new ArgRule<CliArgumentManager>();

    @Test
    public void DEF_NAME() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> def;
            {
                def = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                def.setName("willfail");
                setDefaultArgument(def);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("default argument could not have a name", e.toString());
        }
    }

    @Test
    public void DEF_NOPARAM() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliNoParamArgument def;

            {
                def = new CliNoParamArgument('q');
                setDefaultArgument(def);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("default argument could not be a CliArgument", e.toString());
        }

    }

    @Test
    public void DEF_HIDDENNAME() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> def;

            {
                def = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                def.addHiddenName("willfail");
                setDefaultArgument(def);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("default argument could not have hidden names", e.toString());
        }
    }

    @Test
    public void DUPLICATE_SHORTNAME() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> def;
            private final CliNoParamArgument           arg1;

            {
                arg1 = new CliNoParamArgument('q');
                addArg(arg1);

                def = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                setDefaultArgument(def);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("shortname duplication : -q", e.toString());
        }
    }

    @Test
    public void DUPLICATE_SHORTNAME2() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg1;

            {
                arg1 = new CliNoParamArgument('q');
                addArg(arg1);

                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                addArg(arg2);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("shortname duplication : -q", e.toString());
        }
    }

    @Test
    public void DUPLICATE_HIDDENNAME() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg1;

            {
                arg1 = new CliNoParamArgument('l');
                arg1.addHiddenName("hiddenname");
                addArg(arg1);

                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                arg2.addHiddenName("hiddenname");
                addArg(arg2);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("hidden name duplication : hiddenname", e.toString());
        }
    }

    @Test
    public void DUPLICATE_NAME() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg1;

            {
                arg1 = new CliNoParamArgument('l');
                arg1.setName("name");
                addArg(arg1);

                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                arg2.setName("--name");
                addArg(arg2);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("name duplication : -l", e.toString());
        }
    }

    @Test
    public void FORBIDDEN_EXISTS() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg1;

            {
                arg1 = new CliNoParamArgument('l');

                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                arg2.addForbiddenArgument(arg1);
                addArg(arg2);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("argument -q contain forbidden arg [ -l ] that is not referenced in manager",
                    e.toString());
        }
    }

    @Test
    public void NEEDED_EXISTS() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg1;

            {
                arg1 = new CliNoParamArgument('l');

                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                arg2.addNeededArgument(arg1);
                addArg(arg2);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("argument [ -q salut ] contain needed arg [ -l ] that is not referenced in manager",
                    e.toString());
        }
    }

    @Test
    public void NEEDED_AND_FORBIDDEN() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg1;

            {
                arg1 = new CliNoParamArgument('l');
                addArg(arg1);

                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                arg2.addNeededArgument(arg1);
                arg2.addForbiddenArgument(arg1);
                addArg(arg2);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("argument [ -l ] can not be needed and forbidden for argument [ -q salut ]",
                    e.toString());
        }
    }

    @Test
    public void CIRCULAR_FORBIDDEN1() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg3;
            private final CliNoParamArgument           arg1;

            {
                arg1 = new CliNoParamArgument('l');
                addArg(arg1);

                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                addArg(arg2);

                arg3 = new CliNoParamArgument('m');
                addArg(arg3);

                arg2.addNeededArgument(arg1);
                arg2.addNeededArgument(arg3);
                arg3.addForbiddenArgument(arg1);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals(
                    "argument [ -q salut ] need [ -l ] by dependency but the needed argument[ -m ] forbid it : [ -l ]",
                    e.toString());
        }
    }

    @Test
    public void CIRCULAR_FORBIDDEN2() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg3;
            private final CliNoParamArgument           arg1;

            {
                arg1 = new CliNoParamArgument('l');
                addArg(arg1);

                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                addArg(arg2);

                arg3 = new CliNoParamArgument('m');
                addArg(arg3);

                arg2.addNeededArgument(arg1);
                arg1.addNeededArgument(arg3);
                arg3.addForbiddenArgument(arg2);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("argument [ -q salut ] has a needed argument [ -m ] that forbit it  : [ -q salut ]",
                    e.toString());
        }
    }

    @Test
    public void CIRCULAR_FORBIDDEN3() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg3;
            private final CliNoParamArgument           arg1;
            private final CliNoParamArgument           arg4;

            {
                arg1 = new CliNoParamArgument('l');
                addArg(arg1);

                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                addArg(arg2);

                arg3 = new CliNoParamArgument('m');
                addArg(arg3);

                arg4 = new CliNoParamArgument('n');
                addArg(arg4);

                arg1.addNeededArgument(arg2);
                arg2.addNeededArgument(arg3);
                arg3.addNeededArgument(arg4);
                arg4.addForbiddenArgument(arg1);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("argument [ -l ] has a needed argument [ -n ] that forbit it  : [ -l ]", e.toString());
        }
    }

    @Test
    public void CIRCULAR_FORBIDDEN4() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg3;
            private final CliNoParamArgument           arg1;
            private final CliNoParamArgument           arg4;

            {
                arg1 = new CliNoParamArgument('l');
                addArg(arg1);

                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                addArg(arg2);

                arg3 = new CliNoParamArgument('m');
                addArg(arg3);

                arg4 = new CliNoParamArgument('n');
                addArg(arg4);

                arg1.addNeededArgument(arg2);
                arg2.addNeededArgument(arg3);
                arg2.addNeededArgument(arg4);
                arg4.addForbiddenArgument(arg3);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals(
                    "argument [ -l ] need [ -m ] by dependency but the needed argument[ -n ] forbid it : [ -m ]",
                    e.toString());
        }
    }

    @Test
    public void CIRCULAR_FORBIDDEN5() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg3;
            private final CliNoParamArgument           arg1;
            private final CliNoParamArgument           arg4;

            {
                arg1 = new CliNoParamArgument('l');
                addArg(arg1);
                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                addArg(arg2);
                arg3 = new CliNoParamArgument('m');
                addArg(arg3);
                arg4 = new CliNoParamArgument('n');
                addArg(arg4);

                arg1.addNeededArgument(arg2);
                arg2.addNeededArgument(arg3);
                arg2.addNeededArgument(arg4);
                arg4.addForbiddenArgument(arg1);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals("argument [ -l ] has a needed argument [ -n ] that forbit it  : [ -l ]", e.toString());
        }
    }

    @Test
    public void CIRCULAR_FORBIDDEN6() {
        argRule.manager = new CliArgumentManager("argumentTestDefinition") {
            private final CliOneParamArgument<Integer> arg2;
            private final CliNoParamArgument           arg3;
            private final CliNoParamArgument           arg1;
            private final CliNoParamArgument           arg4;
            private final CliNoParamArgument           arg5;

            {
                arg1 = new CliNoParamArgument('l');
                addArg(arg1);
                arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                addArg(arg2);
                arg3 = new CliNoParamArgument('m');
                addArg(arg3);
                arg4 = new CliNoParamArgument('n');
                addArg(arg4);
                arg5 = new CliNoParamArgument('o');
                addArg(arg5);

                arg1.addNeededArgument(arg2);
                arg1.addNeededArgument(arg3);
                arg2.addNeededArgument(arg4);
                arg3.addNeededArgument(arg5);
                arg5.addForbiddenArgument(arg4);
            }
        };
        try {
            argRule.runParser();
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertEquals(
                    "argument [ -l ] need [ -n ] by dependency but the needed argument[ -o ] forbid it : [ -n ]",
                    e.toString());
        }
    }

}
