package net.awired.ajsl.cli.argument.implementation;

import java.net.InetAddress;
import java.net.UnknownHostException;
import junit.framework.Assert;
import net.awired.ajsl.cli.argument.implementation.ArgumentTestManager.Scenarios;

public enum ArgumentTestEnum implements CliArgumentTestIface<ArgumentTestManager> {

    HELPER(new String[] { "-h" }, null, "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
            + "  -a=ip num                Set the server address where to connect and this part\n"
            + "                              is long to chdck return to a new line\n"
            + "                           ip                  : ip very long description to \n"
            + "                             check return to a new line\n"
            + "                           num                 : num description\n"
            + "                           Default Value       : -a /127.0.0.1 23\n"
            + "  -h, --help               This helper\n" + "  -l...                    Multicall minimum   : 1\n"
            + "                           Multicall maximum   : 4\n"
            + "  -m=int...                Multicall minimum   : 1\n"
            + "                           Multicall maximum   : 2\n"
            + "  -p, --port=port          Set the server port where to connect\n"
            + "                           Default Value       : -p 2006\n"
            + "  -s=scenario              scenario            : TRIP_1_2_6 | TRIP_246_244_247\n"
            + "                           Default Value       : -s TRIP_1_2_6\n" + "  -v\n"
            + "  transactions num         hand made scenario based on transaction list\n"
            + "                           transactions        : desk a TRIP | T001 | T002 | \n"
            + "                             T006 | T246 | T247 | T244\n"
            + "                           num                 : desk b\n"
            + "                           Default Value       : T001 23 T002 45\n\n") {

        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
            Assert.assertEquals(1, argManager.getHelperArgument().getNumCall());
        }

    },

    NORMAL_SHORT(new String[] { "-s", "TRIP_1_2_6", "-l", "-p", "42", "-v" }, null, null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {

            Assert.assertFalse(exit);
            // -s
            Assert.assertEquals(Scenarios.TRIP_1_2_6, argManager.scenario.getParamOneValue());
            Assert.assertEquals(1, argManager.scenario.getNumCall());
            // -l
            Assert.assertEquals(1, argManager.loop.getNumCall());
            // -p
            Assert.assertEquals(1, argManager.port.getNumCall());
            Assert.assertEquals((Integer) 42, argManager.port.getParamOneValue());
            // -v
            Assert.assertEquals(1, argManager.verbose.getNumCall());
        }
    },

    MULTICALL(new String[] { "-l", "-l", "-l", "-l" }, null, null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertFalse(exit);
            // -l
            Assert.assertEquals(4, argManager.loop.getNumCall());
        }
    },

    MULTICALL_OVERFLOW(new String[] { "-l", "-l", "-l", "-l", "-l" },
            "enumArgumentTest: argument must be called maximum 4 times -- [ -l ... ]\n"
                    + "  enumArgumentTest -l -l -l -l -l\n" + "___________________^\n"
                    + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                    + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
        }
    },

    SHORT_APPENDED(new String[] { "-lv" }, null, null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertFalse(exit);
            // -l
            Assert.assertEquals(1, argManager.loop.getNumCall());
            // -v
            Assert.assertEquals(1, argManager.verbose.getNumCall());
        }
    },

    SHORT_APPENDED_FAIL_NOEXISTS(new String[] { "-lz" }, "enumArgumentTest: Argument does not exists : 'z'\n"
            + "  enumArgumentTest -lz\n" + "_____________________^\n"
            + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
            + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
        }
    },

    SHORT_APPENDED_PARAMED_FAIL(new String[] { "-lvp" },
            "enumArgumentTest: Param(s) not found after argument -- [ -p port ]\n" + "  enumArgumentTest -lvp\n"
                    + "______________________^\n" + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                    + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
        }
    },

    SHORT_APPENDED_PARAMED_FAIL_REVERTED(new String[] { "-lpv" },
            "enumArgumentTest: Param(s) not found after argument -- [ -p port ]\n" + "  enumArgumentTest -lpv\n"
                    + "_____________________^\n" + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                    + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
        }
    },

    SHORT_APPENDED_PARAMED_STICKED_NOTPARAM(new String[] { "-lpv", "42", "-l" }, null, null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            // -l
            Assert.assertEquals(2, argManager.loop.getNumCall());
            // -v
            Assert.assertEquals(1, argManager.verbose.getNumCall());
            // -p
            Assert.assertEquals(1, argManager.port.getNumCall());
            Assert.assertEquals((Integer) 42, argManager.port.getParamOneValue());
        }
    },

    SHORT_APPENDED_PARAMED_STICKED(new String[] { "-lvp42", "-l" }, null, null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            // -l
            Assert.assertEquals(2, argManager.loop.getNumCall());
            // -v
            Assert.assertEquals(1, argManager.verbose.getNumCall());
            // -p
            Assert.assertEquals(1, argManager.port.getNumCall());
            Assert.assertEquals((Integer) 42, argManager.port.getParamOneValue());
        }
    },

    SHORT_APPENDED_PARAMED_FAIL2(new String[] { "-lvp", "-l" },
            "enumArgumentTest: -l is not a valid Integer -- [ -p port ]\n" + "  enumArgumentTest -lvp -l\n"
                    + "________________________^\n" + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                    + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
        }
    },

    SHORT_APPENDED_FAIL_PARAM(new String[] { "-lp" },
            "enumArgumentTest: Param(s) not found after argument -- [ -p port ]\n" + "  enumArgumentTest -lp\n"
                    + "_____________________^\n" + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                    + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
        }
    },

    SHORT_APPENDED_PARAM(new String[] { "-lpv", "42" }, null, null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertFalse(exit);
            // -l
            Assert.assertEquals(1, argManager.loop.getNumCall());
            // -v
            Assert.assertEquals(1, argManager.verbose.getNumCall());
            // -p
            Assert.assertEquals(1, argManager.port.getNumCall());
            Assert.assertEquals((Integer) 42, argManager.port.getParamOneValue());
        }
    },

    SHORT_APPENDED_FAIL(new String[] { "-lpvz", "42" }, "enumArgumentTest: Argument does not exists : 'z'\n"
            + "  enumArgumentTest -lpvz 42\n" + "_______________________^\n"
            + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
            + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
        }
    },

    SHORT_APPENDED_FAIL3(new String[] { "-lps" },
            "enumArgumentTest: Param(s) not found after argument -- [ -p port ]\n" + "  enumArgumentTest -lps\n"
                    + "_____________________^\n" + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                    + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
        }
    },

    SHORT_APPENDED_FAIL4(new String[] { "-lps", "42", "TRIP_1_2_6" },
            "enumArgumentTest: Sticked arguments can have only one paramed argument -- [ -s scenario ]\n"
                    + "  enumArgumentTest -lps 42 TRIP_1_2_6\n" + "______________________^\n"
                    + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                    + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
        }
    },

    SHORT_APPENDED_2PARAM7(new String[] { "-la", "127.0.0.1", "43", "-l" }, null, null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertFalse(exit);
            // -s
            try {
                Assert.assertEquals(InetAddress.getByName("127.0.0.1"), argManager.address.getParamOneValue());
            } catch (UnknownHostException e) {
                Assert.fail(e.getMessage());
            }
            Assert.assertEquals((Integer) 43, argManager.address.getParamTwoValue());
            // -l
            Assert.assertEquals(2, argManager.loop.getNumCall());
        }
    },

    SHORT_APPENDED_PARAMED(new String[] { "-lp42", "-l" }, null, null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertFalse(exit);
            // -l
            Assert.assertEquals(2, argManager.loop.getNumCall());
            // -p
            Assert.assertEquals(1, argManager.port.getNumCall());
            Assert.assertEquals((Integer) 42, argManager.port.getParamOneValue());
        }
    },

    SHORT_APPENDED_PARAMED_DBLE_FAIL(new String[] { "-lpz", "z" },
            "enumArgumentTest: z is not a valid Integer -- [ -p port ]\n" + "  enumArgumentTest -lpz z\n"
                    + "________________________^\n" + "May be the root cause : \n"
                    + "    z is not a valid Integer -- [ -p port ]\n" + "      enumArgumentTest -lpz z\n"
                    + "__________________________^\n" + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                    + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {

            Assert.assertTrue(exit);
        }
    },

    SHORT_APPENDED_2PARAM(new String[] { "-lpa", "127.0.0.1", "43" },
            "enumArgumentTest: 127.0.0.1 is not a valid Integer -- [ -p port ]\n"
                    + "  enumArgumentTest -lpa 127.0.0.1 43\n" + "________________________^\n"
                    + "May be the root cause : \n" + "    a is not a valid Integer -- [ -p port ]\n"
                    + "      enumArgumentTest -lpa 127.0.0.1 43\n" + "__________________________^\n"
                    + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                    + "Try `enumArgumentTest --help' for more information.\n", null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            Assert.assertTrue(exit);
        }
    },

    //    SHORT_APPENDED_2PARAM32("-lpa", "127.0.0.1") {
    //        @Override
    //        public void checkResult(CliArgumentManagerTest argManager, boolean exit) {
    //            Assert.fail();
    //        }
    //    },
    //
    //    SHORT_APPENDED_2PARAM34("-lap", "127.0.0.1") {
    //        @Override
    //        public void checkResult(CliArgumentManagerTest argManager, boolean exit) {
    //            Assert.fail();
    //        }
    //    },
    //    SHORT_APPENDED_2PARAM35("-lap", "127.0.0.1", "42") {
    //        @Override
    //        public void checkResult(CliArgumentManagerTest argManager, boolean exit) {
    //            Assert.fail();
    //        }
    //    },
    //    SHORT_APPENDED_2PARAM37("-p", "42", "-l") {
    //        @Override
    //        public void checkResult(CliArgumentManagerTest argManager, boolean exit) {
    //            Assert.fail();
    //        }
    //    },
    //    SHORT_APPENDED_2PARAM38("-l", "-p") {
    //        @Override
    //        public void checkResult(CliArgumentManagerTest argManager, boolean exit) {
    //            Assert.fail();
    //        }
    //    },
    //
    //    SHORT_APPENDED_2PARAM39("-p42") {
    //        @Override
    //        public void checkResult(CliArgumentManagerTest argManager, boolean exit) {
    //            Assert.fail();
    //        }
    //    },
    /*
     * shortname :
     * -lpt
     * 
     * -ltp
     * 
     * -p42
     * -pl42
     * -lp42
     * -lp
     * -m42 43
     * -m 42 43
     * -m 45 TRIP
     * -m 45 -l
     * -m 45 -t TRIP
     * -t T002 42 TRIP 43
     * 
     * longname :
     * --verbose
     * --port
     * --port42
     * --port=42
     * --port 42
     * --multi 42 -f
     * --multi 42 43
     * --transaction T002 42 TRIP 45
     * --transaction T002 42 TRIP
     * --transaction T002 42 TRIP 45 T001 33
     */

    ;

    @Override
    public void preTest() {
    }

    @Override
    public void postTest() {

    }

    private final String   err;
    private final String   out;
    private final String[] args;

    private ArgumentTestEnum(String[] args, String err, String out) {
        this.args = args;
        this.out = out;
        this.err = err;
    }

    public ArgumentTestManager getManager() {
        return new ArgumentTestManager();
    }

    @Override
    public void checkResult(ArgumentTestManager argManager, boolean exit, String exception) {
        throw new RuntimeException("should not be called for this test enum");
    }

    /////////////////////////////////////////////////////////////////////

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
