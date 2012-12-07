package net.awired.aclm.argument.parser2;

import junit.framework.Assert;
import net.awired.aclm.help.ArgRule;
import org.junit.Rule;
import org.junit.Test;

public class ParseTest2 {

    @Rule
    public ArgRule<ArgumentTestManager2> argRule = new ArgRule<ArgumentTestManager2>();

    public ArgumentTestManager2          manager = new ArgumentTestManager2();

    public ParseTest2() {
        argRule.manager = manager;
    }

    @Test
    public void SHORT_APPENDED_2PARAM() {
        argRule.setArgs(new String[] { "erftest" });
        argRule.runParser();

        Assert.assertFalse(argRule.parseSuccess);
        Assert.assertEquals("", argRule.out);
        Assert.assertEquals("enumArgumentTest: no default argument to parse : erftest\n"
                + "Usage: enumArgumentTest [ -a ip num ][ -m int ... ][ -v ][ -l ... ][ -p port ][ -s scenario ]\n"
                + "Try `enumArgumentTest --help' for more information.\n", argRule.err);
    }

}
