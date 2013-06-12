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
package net.awired.aclm.ask;

import java.io.ByteArrayInputStream;
import junit.framework.Assert;
import net.awired.aclm.param.CliParamEnum;
import org.junit.Test;

public class AskTest {

    public enum Transactions {
        TRIP, T001, T002, T006, T246, T247, T244;
    }

    @Test
    public void testRun() {

        Ask<Transactions> ask = new Ask<Transactions>("Which transaction do you want", new CliParamEnum<Transactions>(
                "transaction", Transactions.class));

        ask.setIn(new ByteArrayInputStream("TRIP".getBytes()));
        ask.run();

        Assert.assertEquals(Transactions.TRIP, ask.getValue());

    }

    @Test
    public void testRunEmptyWithDefaultValue() {
        Ask<Transactions> ask = new Ask<Transactions>("Which transaction do you want", new CliParamEnum<Transactions>(
                "transaction", Transactions.class));

        ask.setIn(new ByteArrayInputStream(" ".getBytes()));
        ask.setDefaultValue(Transactions.T001);
        ask.run();

        Assert.assertEquals(Transactions.T001, ask.getValue());
    }

    @Test
    public void testRunAskTwice() {
        Ask<Transactions> ask = new Ask<Transactions>("Which transaction do you want", new CliParamEnum<Transactions>(
                "transaction", Transactions.class));

        ask.setIn(new ByteArrayInputStream("fdfsdf\r\nT006".getBytes()));
        ask.run();

        Assert.assertEquals(Transactions.T006, ask.getValue());
    }

}
