package net.awired.ajsl.cli.argument.implementation;

import net.awired.ajsl.cli.argument.CliArgumentManager;
import net.awired.ajsl.cli.argument.args.CliNoParamArgument;
import net.awired.ajsl.cli.argument.args.CliOneParamArgument;
import net.awired.ajsl.cli.param.CliParamInt;
import org.testng.Assert;

public enum ArgumentTestDefinitionEnum implements CliArgumentTestIface<CliArgumentManager> {

    DEF_NAME("default argument could not have a name") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
                private final CliOneParamArgument<Integer> def;

                {
                    def = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                    def.setName("willfail");
                    setDefaultArgument(def);
                }
            };
        }
    },

    DEF_NOPARAM("default argument could not be a CliArgument") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
                private final CliNoParamArgument def;

                {
                    def = new CliNoParamArgument('q');
                    setDefaultArgument(def);
                }
            };
        }
    },

    DEF_HIDDENNAME("default argument could not have hidden names") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
                private final CliOneParamArgument<Integer> def;

                {
                    def = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                    def.addHiddenName("willfail");
                    setDefaultArgument(def);
                }
            };
        }
    },

    DUPLICATE_SHORTNAME("shortname duplication : -q") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
                private final CliOneParamArgument<Integer> def;
                private final CliNoParamArgument           arg1;

                {
                    arg1 = new CliNoParamArgument('q');
                    addArg(arg1);

                    def = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                    setDefaultArgument(def);
                }
            };
        }
    },

    DUPLICATE_SHORTNAME2("shortname duplication : -q") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
                private final CliOneParamArgument<Integer> arg2;
                private final CliNoParamArgument           arg1;

                {
                    arg1 = new CliNoParamArgument('q');
                    addArg(arg1);

                    arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                    addArg(arg2);
                }
            };
        }
    },

    DUPLICATE_HIDDENNAME("hidden name duplication : hiddenname") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
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
        }
    },

    DUPLICATE_NAME("name duplication : -l") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
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
        }
    },

    FORBIDDEN_EXISTS("argument -q contain forbidden arg [ -l ] that is not referenced in manager") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
                private final CliOneParamArgument<Integer> arg2;
                private final CliNoParamArgument           arg1;

                {
                    arg1 = new CliNoParamArgument('l');

                    arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                    arg2.addForbiddenArgument(arg1);
                    addArg(arg2);
                }
            };
        }
    },

    NEEDED_EXISTS("argument [ -q salut ] contain needed arg [ -l ] that is not referenced in manager") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
                private final CliOneParamArgument<Integer> arg2;
                private final CliNoParamArgument           arg1;

                {
                    arg1 = new CliNoParamArgument('l');

                    arg2 = new CliOneParamArgument<Integer>('q', new CliParamInt("salut"));
                    arg2.addNeededArgument(arg1);
                    addArg(arg2);
                }
            };
        }
    },

    NEEDED_AND_FORBIDDEN("argument [ -l ] can not be needed and forbidden for argument [ -q salut ]") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
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
        }
    },

    CIRCULAR_FORBIDDEN1(
            "argument [ -q salut ] need [ -l ] by dependency but the needed argument[ -m ] forbid it : [ -l ]") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
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
        }
    },

    CIRCULAR_FORBIDDEN2("argument [ -q salut ] has a needed argument [ -m ] that forbit it  : [ -q salut ]") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
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
        }
    },

    CIRCULAR_FORBIDDEN3("argument [ -l ] has a needed argument [ -n ] that forbit it  : [ -l ]") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
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
        }
    },

    CIRCULAR_FORBIDDEN4("argument [ -l ] need [ -m ] by dependency but the needed argument[ -n ] forbid it : [ -m ]") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
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
        }
    },
    CIRCULAR_FORBIDDEN5("argument [ -l ] has a needed argument [ -n ] that forbit it  : [ -l ]") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
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
        }
    },
    CIRCULAR_FORBIDDEN6("argument [ -l ] need [ -n ] by dependency but the needed argument[ -o ] forbid it : [ -n ]") {
        @Override
        public CliArgumentManager getManager() {
            return new CliArgumentManager("argumentTestDefinition") {
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
        }
    }

    ;

    private final String   err;
    private final String   out;
    private final String[] args;
    private final String   exception;

    private ArgumentTestDefinitionEnum(String exception) {
        this.args = new String[] {};
        this.out = null;
        this.err = null;
        this.exception = exception;
    }

    @Override
    public void checkResult(CliArgumentManager argManager, boolean exit) {
        throw new RuntimeException("should not be called for this enum class");
    }

    @Override
    public void checkResult(CliArgumentManager argManager, boolean exit, String exception) {
        Assert.assertEquals(exception.toString(), this.exception);
    }

    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void preTest() {
    }

    @Override
    public void postTest() {

    }

    /**
     * @return the args
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * @return the err
     */
    public String getErr() {
        return err;
    }

    /**
     * @return the out
     */
    public String getOut() {
        return out;
    }

}
