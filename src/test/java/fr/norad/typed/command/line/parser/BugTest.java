/**
 *
 *     Copyright (C) norad.fr
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
package fr.norad.typed.command.line.parser;

import static java.net.InetAddress.getByName;
import static junit.framework.Assert.assertEquals;
import java.net.InetAddress;
import org.junit.Test;
import fr.norad.typed.command.line.parser.argument.CliArgumentManager;
import fr.norad.typed.command.line.parser.argument.args.CliOneParamArgument;
import fr.norad.typed.command.line.parser.param.CliParamInetAddress;
import fr.norad.typed.command.line.parser.param.CliParamInt;

public class BugTest {

    @Test
    public void should_be_able_to_read_2_full_param_name() throws Exception {
        ArgumentManager manager = new ArgumentManager();

        boolean b = manager.parseWithSuccess(new String[]{"--db-host=localhost", "--db-port=9042"});

        assertEquals(true, b);
        assertEquals(getByName("localhost"), manager.cassandraHost.getParamOneValue());
        assertEquals((Integer) 9042, manager.cassandraPort.getParamOneValue());
    }

    public class ArgumentManager extends CliArgumentManager {

        public final CliOneParamArgument<InetAddress> cassandraHost;
        public final CliOneParamArgument<Integer> cassandraPort;

        public ArgumentManager() {
            super("TEST");
            cassandraHost = addArg(cassandraHost('H'));
            cassandraPort = addArg(CassandraPort('P', cassandraHost));
        }

        private CliOneParamArgument<InetAddress> cassandraHost(char c) {
            CliOneParamArgument<InetAddress> cassandraHost = new CliOneParamArgument<>(c, new CliParamInetAddress("host"));
            cassandraHost.setName("db-host");
            cassandraHost.setDescription("Host to connect to Cassandra. Start embedded if not provided");
            return cassandraHost;
        }

        private CliOneParamArgument<Integer> CassandraPort(char c, CliOneParamArgument<InetAddress> cassandraHost) {
            CliParamInt paramOneArgument = new CliParamInt("port");
            paramOneArgument.setNegativable(false);
            CliOneParamArgument<Integer> cassandraPort = new CliOneParamArgument<>(c, paramOneArgument);
            cassandraPort.setName("db-port");
            cassandraPort.setDescription("Port to connect to Cassandra");
            cassandraPort.setParamOneDefValue(0);
            cassandraPort.addNeededArgument(cassandraHost);
            return cassandraPort;
        }
    }
}
