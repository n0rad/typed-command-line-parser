package net.awired.ajsl.cli.ask;

import java.io.StringBufferInputStream;
import junit.framework.Assert;
import net.awired.ajsl.cli.param.CliParamEnum;
import org.testng.annotations.Test;

public class AskTest {

    public enum Transactions {
        TRIP, T001, T002, T006, T246, T247, T244;
    }

    @Test
    public void testRun() {

        Ask<Transactions> ask = new Ask<Transactions>("Which transaction do you want", new CliParamEnum<Transactions>(
                "transaction", Transactions.class));

        ask.setIn(new StringBufferInputStream("TRIP"));
        ask.run();

        Assert.assertEquals(Transactions.TRIP, ask.getValue());

    }

    @Test
    public void testRunEmptyWithDefaultValue() {
        Ask<Transactions> ask = new Ask<Transactions>("Which transaction do you want", new CliParamEnum<Transactions>(
                "transaction", Transactions.class));

        ask.setIn(new StringBufferInputStream(" "));
        ask.setDefaultValue(Transactions.T001);
        ask.run();

        Assert.assertEquals(Transactions.T001, ask.getValue());
    }

    @Test
    public void testRunAskTwice() {
        Ask<Transactions> ask = new Ask<Transactions>("Which transaction do you want", new CliParamEnum<Transactions>(
                "transaction", Transactions.class));

        ask.setIn(new StringBufferInputStream("fdfsdf\r\nT006"));
        ask.run();

        Assert.assertEquals(Transactions.T006, ask.getValue());
    }

}
