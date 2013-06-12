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
package net.awired.typed.command.line.parser.help;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import net.awired.typed.command.line.parser.argument.CliArgumentManager;
import org.junit.rules.ExternalResource;

public class ArgRule<T extends CliArgumentManager> extends ExternalResource {
    public ByteArrayOutputStream outStream;
    public ByteArrayOutputStream errStream;

    public T                     manager;
    private String[]             args = new String[] {};

    public String                out;
    public String                err;
    public boolean               parseSuccess;

    public void runParser() {
        outStream = new ByteArrayOutputStream();
        errStream = new ByteArrayOutputStream();
        manager.setOutputStream(new PrintStream(outStream));
        manager.setErrorStream(new PrintStream(errStream));

        System.out.print("running args :");
        for (String arg : args) {
            System.out.print(" ");
            System.out.print(arg);
        }
        System.out.println();
        parseSuccess = manager.parseWithSuccess(args);

        err = errStream.toString();
        out = outStream.toString();
        System.out.println(out);
        System.err.println(err);
    }

    //////////////////////////////////////////////////////

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public T getManager() {
        return manager;
    }

}
