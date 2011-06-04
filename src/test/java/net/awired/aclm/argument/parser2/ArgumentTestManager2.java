package net.awired.aclm.argument.parser2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.awired.aclm.argument.CliArgumentManager;
import net.awired.aclm.argument.args.CliNoParamArgument;
import net.awired.aclm.argument.args.CliOneParamArgument;
import net.awired.aclm.argument.args.CliTwoParamArgument;
import net.awired.aclm.param.CliParamEnum;
import net.awired.aclm.param.CliParamInetAddress;
import net.awired.aclm.param.CliParamInt;

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
