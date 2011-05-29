package net.awired.ajsl.cli.argument.args;

import junit.framework.Assert;
import net.awired.ajsl.cli.argument.CliArgumentDefinitionException;
import net.awired.ajsl.cli.argument.interfaces.CliArgument;
import org.junit.Test;

public class CliArgumentTest {

    @Test
    public void testCompareTo() {
        CliArgument arg1 = new CliNoParamArgument('S');
        CliArgument arg2 = new CliNoParamArgument('s');

        Assert.assertEquals(1, arg1.compareTo(arg2));
        Assert.assertEquals(-1, arg2.compareTo(arg1));
    }

    @Test
    public void testCliArgument() {
        CliArgument argument = new CliNoParamArgument('s');
        Assert.assertEquals("-s", argument.getShortName());
        CliArgument argument2 = new CliNoParamArgument('S');
        Assert.assertEquals("-S", argument2.getShortName());

        boolean test = false;
        try {
            new CliNoParamArgument((char) 2);
        } catch (CliArgumentDefinitionException e) {
            test = true;
        }
        Assert.assertTrue(test);
    }

    @Test
    public void testIsMulticall() {
        CliArgument argument = new CliNoParamArgument('s');
        argument.setMultiCallMax(0);
        Assert.assertEquals(true, argument.isMulticall());
        argument.setMultiCallMax(1);
        Assert.assertEquals(false, argument.isMulticall());
    }
}
