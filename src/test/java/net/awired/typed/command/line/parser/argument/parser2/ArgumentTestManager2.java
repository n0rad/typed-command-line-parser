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
package net.awired.typed.command.line.parser.argument.parser2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.awired.typed.command.line.parser.argument.CliArgumentManager;
import net.awired.typed.command.line.parser.argument.args.CliNoParamArgument;
import net.awired.typed.command.line.parser.argument.args.CliOneParamArgument;
import net.awired.typed.command.line.parser.argument.args.CliTwoParamArgument;
import net.awired.typed.command.line.parser.param.CliParamEnum;
import net.awired.typed.command.line.parser.param.CliParamInetAddress;
import net.awired.typed.command.line.parser.param.CliParamInt;

public class ArgumentTestManager2 extends CliArgumentManager {
    public final CliTwoParamArgument<InetAddress, Integer> address;
    public final CliOneParamArgument<Integer>              port;
    public final CliOneParamArgument<Scenarios>            scenario;
    public final CliNoParamArgument                        loop;
    public final CliNoParamArgument                        verbose;
    public final CliOneParamArgument<Integer>              multicallparamed;

    public enum Transactions {
        TRIP, T001, T002, T006, T246, T247, T244;
    }

    public enum Scenarios {
        TRIP_1_2_6, TRIP_246_244_247;
    }

    public ArgumentTestManager2() {
        super("enumArgumentTest");
        // -a address
        address = new CliTwoParamArgument<InetAddress, Integer>('a',
                new CliParamInetAddress("ip")
                        .setParamDescription("ip very long description to check return to a new line"),
                new CliParamInt("num").setParamDescription("num description"));
        try {
            address.setParamOneDefValue(InetAddress.getByName("127.0.0.1"));
            address.setParamTwoDefValue(23);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        address.setDescription("Set the server address where to connect and this part is long to chdck return to a new line");
        addArg(address);

        // -m
        multicallparamed = new CliOneParamArgument<Integer>('m', new CliParamInt("int"));
        multicallparamed.setMultiCallMax(2);
        addArg(multicallparamed);
        // -v
        verbose = new CliNoParamArgument('v');
        addArg(verbose);

        // -l
        loop = new CliNoParamArgument('l');
        loop.setMultiCallMax(4);
        addArg(loop);

        // -p
        port = new CliOneParamArgument<Integer>('p', new CliParamInt("port"));
        port.setDescription("Set the server port where to connect");
        port.setParamOneDefValue(2006);
        //              port.setMultiCallMin(2);
        //              port.setMultiCallMax(0);
        port.setName("port");
        addArg(port);

        // -s
        scenario = new CliOneParamArgument<Scenarios>('s', new CliParamEnum<Scenarios>("scenario", Scenarios.class));
        scenario.setParamOneDefValue(Scenarios.TRIP_1_2_6);
        addArg(scenario);

    }

}
