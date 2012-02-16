package net.awired.aclm.argument.parser;

import static org.junit.Assert.assertEquals;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import junit.framework.Assert;
import net.awired.aclm.argument.parser.ArgumentTestManager.Scenarios;
import net.awired.aclm.help.ArgRule;
import org.junit.Rule;
import org.junit.Test;

public class ParseTest {

    @Rule
    public ArgRule<ArgumentTestManager> argRule = new ArgRule<ArgumentTestManager>();

    public ArgumentTestManager manager = new ArgumentTestManager();

    public ParseTest() {
        argRule.manager = manager;
    }

    @Test
    public void HELPER() {
        argRule.setArgs(new String[] { "-h" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals(1, argRule.manager.getHelperArgument().getNumCall());
        Assert.assertEquals("Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                + "  -a=ip num...             Set the server address where to connect and this part\n"
                + "                              is long to chdck return to a new line\n"
                + "                           Multicall minimum   : 1\n"
                + "                           Multicall maximum   : 0\n"
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
                + "                           Default Value       : T001 23 T002 45\n\n", argRule.out);
        Assert.assertEquals("", argRule.err);
    }

    @Test
    public void NORMAL_SHORT() {
        argRule.setArgs(new String[] { "-s", "TRIP_1_2_6", "-l", "-p", "42", "-v" });
        argRule.runParser();

        // -s
        Assert.assertEquals(Scenarios.TRIP_1_2_6, manager.scenario.getParamOneValue());
        Assert.assertEquals(1, manager.scenario.getNumCall());
        // -l
        Assert.assertEquals(1, manager.loop.getNumCall());
        // -p
        Assert.assertEquals(1, manager.port.getNumCall());
        Assert.assertEquals((Integer) 42, manager.port.getParamOneValue());
        // -v
        Assert.assertEquals(1, manager.verbose.getNumCall());

        Assert.assertFalse(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("", argRule.err);
    }

    @Test
    public void MULTICALL() {
        argRule.setArgs(new String[] { "-l", "-l", "-l", "-l" });
        argRule.runParser();
        // -l
        Assert.assertEquals(4, manager.loop.getNumCall());

        Assert.assertFalse(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("", argRule.err);
    }

    @Test
    public void MULTICALL_OVERFLOW() {
        argRule.setArgs(new String[] { "-l", "-l", "-l", "-l", "-l" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: argument must be called maximum 4 times -- [ -l ... ]\n"
                + "  enumArgumentTest -l -l -l -l -l\n" + "________________________________^\n"
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void MULTICALL_NOOVERFLOW2() {
        argRule.setArgs(new String[] { "-lllll" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: argument must be called maximum 4 times -- [ -l ... ]\n"
                + "  enumArgumentTest -lllll\n" + "________________________^\n"
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n"
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void MULTICALL_NOOVERFLOW() {
        argRule.setArgs(new String[] { "-llll" });
        argRule.runParser();

        Assert.assertEquals(4, manager.loop.getNumCall());

        Assert.assertFalse(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("", argRule.err);
    }

    @Test
    public void SHORT_APPENDED() {
        argRule.setArgs(new String[] { "-lv" });
        argRule.runParser();

        // -l
        Assert.assertEquals(1, manager.loop.getNumCall());
        // -v
        Assert.assertEquals(1, manager.verbose.getNumCall());

        Assert.assertFalse(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_FAIL_NOEXISTS() {
        argRule.setArgs(new String[] { "-lz" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: Argument does not exists : 'z'\n" //
                + "  enumArgumentTest -lz\n" //
                + "_____________________^\n" //
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n" //
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_PARAMED_FAIL() {
        argRule.setArgs(new String[] { "-lvp" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: Param(s) not found after argument -- [ -p port ]\n" //
                + "  enumArgumentTest -lvp\n" //
                + "______________________^\n" //
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n" //
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_PARAMED_FAIL_REVERTED() {
        argRule.setArgs(new String[] { "-lpv" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: Param(s) not found after argument -- [ -p port ]\n" //
                + "  enumArgumentTest -lpv\n" //
                + "_____________________^\n" //
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n" //
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_PARAMED_STICKED_NOTPARAM() {
        argRule.setArgs(new String[] { "-lpv", "42", "-l" });
        argRule.runParser();

        // -l
        Assert.assertEquals(2, manager.loop.getNumCall());
        // -v
        Assert.assertEquals(1, manager.verbose.getNumCall());
        // -p
        Assert.assertEquals(1, manager.port.getNumCall());
        Assert.assertEquals((Integer) 42, manager.port.getParamOneValue());

        Assert.assertFalse(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_PARAMED_STICKED() {
        argRule.setArgs(new String[] { "-lvp42", "-l" });
        argRule.runParser();

        // -l
        Assert.assertEquals(2, manager.loop.getNumCall());
        // -v
        Assert.assertEquals(1, manager.verbose.getNumCall());
        // -p
        Assert.assertEquals(1, manager.port.getNumCall());
        Assert.assertEquals((Integer) 42, manager.port.getParamOneValue());

        Assert.assertFalse(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_PARAMED_FAIL2() {
        argRule.setArgs(new String[] { "-lvp", "-l" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: -l is not a valid Integer -- [ -p port ]\n" //
                + "  enumArgumentTest -lvp -l\n" //
                + "________________________^\n" //
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n" //
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_FAIL_PARAM() {
        argRule.setArgs(new String[] { "-lp" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: Param(s) not found after argument -- [ -p port ]\n" //
                + "  enumArgumentTest -lp\n" //
                + "_____________________^\n" //
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n" //
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);

    }

    @Test
    public void SHORT_APPENDED_PARAM() {
        argRule.setArgs(new String[] { "-lpv", "42" });
        argRule.runParser();

        // -l
        Assert.assertEquals(1, manager.loop.getNumCall());
        // -v
        Assert.assertEquals(1, manager.verbose.getNumCall());
        // -p
        Assert.assertEquals(1, manager.port.getNumCall());
        Assert.assertEquals((Integer) 42, manager.port.getParamOneValue());

        Assert.assertFalse(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_FAIL() {
        argRule.setArgs(new String[] { "-lpvz", "42" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: Argument does not exists : 'z'\n" //
                + "  enumArgumentTest -lpvz 42\n" //
                + "_______________________^\n" //
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n" //
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_FAIL3() {
        argRule.setArgs(new String[] { "-lps" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: Param(s) not found after argument -- [ -p port ]\n" //
                + "  enumArgumentTest -lps\n" //
                + "_____________________^\n" //
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n" //
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_FAIL4() {
        argRule.setArgs(new String[] { "-lps", "42", "TRIP_1_2_6" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals(
                "enumArgumentTest: Sticked arguments can have only one paramed argument -- [ -s scenario ]\n" //
                        + "  enumArgumentTest -lps 42 TRIP_1_2_6\n" //
                        + "______________________^\n" //
                        + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n" //
                        + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_2PARAM7() {
        argRule.setArgs(new String[] { "-la", "127.0.0.1", "43", "-l" });
        argRule.runParser();

        // -s
        try {
            Assert.assertEquals(Arrays.asList(InetAddress.getByName("127.0.0.1")),
                    manager.address.getParamOneValues());
        } catch (UnknownHostException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(Arrays.asList(43), manager.address.getParamTwoValues());
        // -l
        Assert.assertEquals(2, manager.loop.getNumCall());

        Assert.assertFalse(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_PARAMED() {
        argRule.setArgs(new String[] { "-lp42", "-l" });
        argRule.runParser();

        // -l
        Assert.assertEquals(2, manager.loop.getNumCall());
        // -p
        Assert.assertEquals(1, manager.port.getNumCall());
        Assert.assertEquals((Integer) 42, manager.port.getParamOneValue());

        Assert.assertFalse(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_PARAMED_DBLE_FAIL() {
        argRule.setArgs(new String[] { "-lpz", "z" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: z is not a valid Integer -- [ -p port ]\n" //
                + "  enumArgumentTest -lpz z\n" // 
                + "________________________^\n" //
                + "May be the root cause : \n" //
                + "    z is not a valid Integer -- [ -p port ]\n" //
                + "      enumArgumentTest -lpz z\n" //
                + "__________________________^\n" //
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n" //
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void SHORT_APPENDED_2PARAM() {
        argRule.setArgs(new String[] { "-lpa", "127.0.0.1", "43" });
        argRule.runParser();

        Assert.assertTrue(argRule.exit);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: 127.0.0.1 is not a valid Integer -- [ -p port ]\n" //
                + "  enumArgumentTest -lpa 127.0.0.1 43\n" //
                + "________________________^\n" //
                + "May be the root cause : \n" //
                + "    a is not a valid Integer -- [ -p port ]\n" //
                + "      enumArgumentTest -lpa 127.0.0.1 43\n" //
                + "__________________________^\n" //
                + "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]\n" //
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

    @Test
    public void should_read_multicall_arguments() {
        argRule.setArgs(new String[] { "-a", "127.0.0.1", "43" });
        argRule.runParser();

    }

    @Test
    public void should_read_single_when_multicall_arguments() {
        argRule.setArgs(new String[] { "-p", "8080", "-a", "127.0.0.1", "43", "-a", "127.0.0.1", "44" });
        argRule.runParser();

        assertEquals((Integer) 43, argRule.getManager().address.getParamTwoValues().get(0));
        assertEquals((Integer) 44, argRule.getManager().address.getParamTwoValues().get(1));

    }
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

}
