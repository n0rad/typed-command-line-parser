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
package net.awired.aclm.argument;

import java.io.PrintStream;
import java.util.List;
import net.awired.aclm.argument.interfaces.CliArgument;
import net.awired.aclm.argument.interfaces.CliUsageDisplayer;

/**
 * This class represent a usage Helper generator.
 * 
 * It is used by the manager to generate a Usage String representation of arguments present in the manager.
 * If you want to use your own generator implement the {@link CliUsageDisplayer} or extend this class and set it to the
 * manager.
 * 
 * @author n0rad
 * 
 */
public class CliDefaultUsageDisplayer implements CliUsageDisplayer {

    /** Usage format. */
    public static final String HELPER_USAGE = "Usage: %s %s";
    /** Info format. */
    public static final String HELPER_INFO  = "Try `%s --help' for more information.";
    /** Use a short representation of usage. */
    private boolean            usageShort   = false;

    @Override
    public void displayUsage(CliArgumentManager manager, PrintStream output) {
        output.printf(HELPER_USAGE, manager.getProgramName(), usage(manager));
        output.print(manager.getNewLine());
    }

    @Override
    public void displayInfo(CliArgumentManager manager, PrintStream output) {
        output.printf(HELPER_INFO, manager.getProgramName());
        output.print(manager.getNewLine());
    }

    /**
     * Create a usage String representing the manager.
     * 
     * It will create a string like this :
     * "Usage: enumArgumentTest [ -amvlps ] [ transactions num ]"
     * 
     * @param manager
     *            the current manager
     * @return the usage representation
     */
    private String usage(CliArgumentManager manager) {
        StringBuilder result = new StringBuilder();
        List<CliArgument> arguments = manager.getArguments();
        CliArgument defaultArg = manager.getDefaultArgument();

        if (usageShort) {
            // short version
            StringBuilder mandatory = new StringBuilder();
            boolean flag = false;
            for (CliArgument argument : arguments) {
                if (argument.isUsageHidden() || argument == defaultArg) {
                    continue;
                }

                if (argument.isMandatory()) {
                    mandatory.append(argument.getShortName().charAt(1));
                } else {
                    if (!flag) {
                        result.append("[ -");
                        flag = true;
                    }
                    result.append(argument.getShortName().charAt(1));
                }
            }

            if (flag) {
                result.append(" ]");
            }
            if (mandatory.length() != 0) {
                result.append(" -");
                result.append(mandatory);
            }
        } else {
            // full version
            for (CliArgument argument : arguments) {
                if (argument.isUsageHidden() || argument == defaultArg) {
                    continue;
                }
                result.append(argument.toString());
            }
        }

        if (defaultArg != null) {
            result.append(' ');
            if (!defaultArg.isMandatory()) {
                result.append("[ ");
            }
            result.append(defaultArg.toStringArgument());
            if (!defaultArg.isMandatory()) {
                result.append(" ]");
            }
        }
        return result.toString();
    }

    ////////////////////////////////////////////////////////////////////////

    /**
     * @return the usageShort
     */
    @Override
    public boolean isUsageShort() {
        return usageShort;
    }

    /**
     * @param usageShort
     *            the usageShort to set
     */
    @Override
    public void setUsageShort(boolean usageShort) {
        this.usageShort = usageShort;
    }

}
