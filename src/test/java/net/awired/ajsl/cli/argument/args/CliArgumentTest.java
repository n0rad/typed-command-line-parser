/**
 * 
 */
package net.awired.ajsl.cli.argument.args;

import junit.framework.Assert;
import net.awired.ajsl.cli.argument.CliArgumentDefinitionException;
import net.awired.ajsl.cli.argument.interfaces.CliArgument;
import org.testng.annotations.Test;

/**
 * @author n0rad
 * 
 */
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
            CliArgument argument3 = new CliNoParamArgument((char) 2);
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

    /**
     * Test method for {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#addHiddenName(java.lang.String)}.
     */
    @Test
    public void testAddHiddenName() {

    }

    /**
     * Test method for {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#reset()}.
     */
    @Test
    public void testReset() {

    }

    /**
     * Test method for {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#toStringValues(boolean)}.
     */
    @Test
    public void testToStringValues() {

    }

    /**
     * Test method for {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#checkParse(java.util.List)}.
     */
    @Test
    public void testCheckParse() {

    }

    /**
     * Test method for {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#parse(java.util.List, int)}.
     */
    @Test
    public void testParse() {

    }

    /**
     * Test method for {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#checkDefinition()}.
     */
    @Test
    public void testCheckDefinition() {
    }

    /**
     * Test method for
     * {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#addForbiddenArgument(net.net.awired.ajsl.cli.argument.interfaces.CliArgument)}
     * .
     */
    @Test
    public void testAddForbiddenArgument() {
    }

    /**
     * Test method for
     * {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#addNeededArgument(net.net.awired.ajsl.cli.argument.interfaces.CliArgument)}
     * .
     */
    @Test
    public void testAddNeededArgument() {

    }

    /**
     * Test method for {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#toStringArgument()}.
     */
    @Test
    public void testToStringArgument() {

    }

    /**
     * Test method for {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#toString()}.
     */
    @Test
    public void testToString() {

    }

    /**
     * Test method for {@link net.net.awired.ajsl.cli.argument.interfaces.CliArgument#setName(java.lang.String)}.
     */
    @Test
    public void testSetName() {

    }

}
