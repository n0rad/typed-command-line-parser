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
package net.awired.aclm.argument.args;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.awired.aclm.argument.CliArgumentManager;
import net.awired.aclm.argument.interfaces.CliArgument;
import net.awired.aclm.argument.interfaces.CliHelperArgument;

public class CliDefaultHelperArgument extends CliNoParamArgument implements CliHelperArgument {

    public static final String HELPER_PREAPPEND_NORMAL   = "                           ";
    static final String        HELPER_PREAPPENC_SHIFT    = "                             ";
    public static final int    HELPER_DESCRIPTION_LENGTH = 53;
    public static final String HELPER_DESCRIPTION_NAME   = "%-19s : ";

    private String[]           helperHeader;
    private String[]           helperFooter;

    public CliDefaultHelperArgument(char shortName) {
        super(shortName);

        // default argument definition for an helper
        this.setUsageHidden(true);
        this.setName("help");
        this.addHiddenName("/?");
        this.setDescription("This helper");
    }

    /**
     * Help is used to show the argument helper.
     * Ex : <code>Usage : simul-injecteur [ -a ip num ][ -p port... ][ -s scenario ] [ transactions num ]
  -a=ip num                Set the server address where to connect
                           Needed arguments    :  -p -s
                           ip                  : ip deskription
                           num                 : num description
                           Default Value       : -a /127.0.0.1 23

  -h, --help               This helper

  -p, --port=port...       Set the server port where to connect
                           Needed arguments    :  -t
                           Multicall minimum   : 2
                           Multicall maximum   : 0
                           Default Value       : -p 2006</code>
     */
    @Override
    public void help(CliArgumentManager manager) {
        PrintStream output = manager.getOutputStream();
        // display usage
        manager.getUsageDisplayer().displayUsage(manager, output);

        // sort args
        List<CliArgument> arguments = new ArrayList<CliArgument>(manager.getArguments());
        Collections.sort(arguments);

        // display header
        if (getHelperHeader() != null) {
            for (String line : getHelperHeader()) {
                output.print(line);
                output.print(manager.getNewLine());
            }
        }

        StringBuilder s = new StringBuilder(500);
        for (CliArgument argument : arguments) {
            if (argument.isHelpHidden() || argument == manager.getDefaultArgument()) {
                continue;
            }
            StringBuilder fullname = new StringBuilder();
            List<String> preAppend = new ArrayList<String>();
            fullname.append("  ");
            fullname.append(argument.getShortName());
            if (argument.getName() != null) {
                fullname.append(", ");
                fullname.append(argument.getName());
            }

            String argParams = argument.toStringArgument();
            if (argParams != null) {
                fullname.append('=');
                fullname.append(argParams);
            }
            if (argument.isMulticall()) {
                fullname.append("...");
            }

            String fullnameresult = fullname.toString();
            boolean doneSomething;
            if (fullnameresult.length() > 27) {
                s.append(fullnameresult);
                preAppend.add(manager.getNewLine());
                preAppend.add(HELPER_PREAPPEND_NORMAL);
                doneSomething = addArgumentInformations(manager, s, argument, preAppend);
            } else {
                preAppend.add(String.format("%-27s", fullnameresult));
                doneSomething = addArgumentInformations(manager, s, argument, preAppend);
                if (!doneSomething) {
                    s.append(fullnameresult);
                }
            }
            if (!doneSomething) {
                s.append(manager.getNewLine());
            }
            //  s.append(manager.getNewLine());

        }

        if (manager.getDefaultArgument() != null) {
            ArrayList<String> preappend = new ArrayList<String>();
            preappend.add("  ");
            String fullname = manager.getDefaultArgument().toStringArgument();
            preappend.add(String.format("%-25s", fullname));
            addArgumentInformations(manager, s, manager.getDefaultArgument(), preappend);
        }

        output.print(s.toString());
        output.print(manager.getNewLine());

        // display footer
        if (getHelperHeader() != null) {
            for (String line : getHelperFooter()) {
                output.print(line);
                output.print(manager.getNewLine());
            }
        }
    }

    private boolean addArgumentInformations(CliArgumentManager manager, StringBuilder s, CliArgument argument,
            List<String> preappend) {
        boolean res = false;

        // description
        if (argument.getDescription() != null) {
            res = true;
            dumpArgumentInfo(manager, s, preappend, argument.getDescription());
        }

        // needed arguments
        List<CliArgument> needed = argument.getNeededArguments();
        if (needed != null && needed.size() != 0) {
            StringBuilder neededRes = new StringBuilder();
            neededRes.append(String.format(HELPER_DESCRIPTION_NAME, "Needed arguments"));
            res = true;
            for (CliArgument cliArgument : needed) {
                neededRes.append(' ');
                neededRes.append(cliArgument.getShortName());
            }
            dumpArgumentInfo(manager, s, preappend, neededRes.toString());
        }

        // forbidden arguments
        List<CliArgument> forbidden = argument.getForbiddenArguments();
        if (forbidden != null && forbidden.size() != 0) {
            StringBuilder neededRes = new StringBuilder();
            neededRes.append(String.format(HELPER_DESCRIPTION_NAME, "Forbidden arguments"));
            res = true;
            for (CliArgument cliArgument : forbidden) {
                neededRes.append(' ');
                if (cliArgument == manager.getDefaultArgument()) {
                    neededRes.append("default");
                } else {
                    neededRes.append(cliArgument.getShortName());

                }
            }
            dumpArgumentInfo(manager, s, preappend, neededRes.toString());
        }

        // multicall
        if (argument.isMulticall()) {
            res = true;

            // min
            StringBuilder argdesc = new StringBuilder();
            argdesc.append(String.format(HELPER_DESCRIPTION_NAME, "Multicall minimum"));
            argdesc.append(argument.getMultiCallMin());
            dumpArgumentInfo(manager, s, preappend, argdesc.toString());

            // max
            StringBuilder argdesc2 = new StringBuilder();
            argdesc2.append(String.format(HELPER_DESCRIPTION_NAME, "Multicall maximum"));
            argdesc2.append(argument.getMultiCallMax());
            dumpArgumentInfo(manager, s, preappend, argdesc2.toString());
        }

        // TODO clean
        if (argument instanceof CliOneParamArgument<?>) {

            if (argument instanceof CliOneParamArgument<?>) {
                // description for argument one
                String argumentdesc = ((CliOneParamArgument<?>) argument).getParamOneArgument().getParamDescription();
                if (argumentdesc != null) {
                    res = true;
                    StringBuilder argdesc = new StringBuilder();
                    argdesc.append(String.format(HELPER_DESCRIPTION_NAME, ((CliOneParamArgument<?>) argument)
                            .getParamOneArgument().getName()));
                    argdesc.append(argumentdesc);
                    dumpArgumentInfo(manager, s, preappend, argdesc.toString());
                }
            }

            if (argument instanceof CliTwoParamArgument<?, ?>) {
                // description for argument two
                String argumentdesc = ((CliTwoParamArgument<?, ?>) argument).getParamTwoArgument()
                        .getParamDescription();
                if (argumentdesc != null) {
                    res = true;
                    StringBuilder argdesc = new StringBuilder();
                    argdesc.append(String.format(HELPER_DESCRIPTION_NAME, ((CliTwoParamArgument<?, ?>) argument)
                            .getParamTwoArgument().getName()));
                    argdesc.append(argumentdesc);
                    dumpArgumentInfo(manager, s, preappend, argdesc.toString());
                }
            }

            if (argument instanceof CliThreeParamArgument<?, ?, ?>) {
                // description for argument three
                String argumentdesc = ((CliThreeParamArgument<?, ?, ?>) argument).getParamOneArgument()
                        .getParamDescription();
                if (argumentdesc != null) {
                    res = true;
                    StringBuilder argdesc = new StringBuilder();
                    argdesc.append(String.format(HELPER_DESCRIPTION_NAME, ((CliThreeParamArgument<?, ?, ?>) argument)
                            .getParamThreeArgument().getName()));
                    argdesc.append(argumentdesc);
                    dumpArgumentInfo(manager, s, preappend, argdesc.toString());
                }
            }

            // default value
            String defValue = ((CliOneParamArgument<?>) argument).toStringValues(true,
                    argument == manager.getDefaultArgument());
            if (defValue != null) {
                res = true;
                StringBuilder buff = new StringBuilder();
                buff.append(String.format(HELPER_DESCRIPTION_NAME, "Default Value"));
                buff.append(defValue);
                dumpArgumentInfo(manager, s, preappend, buff.toString());
            }
        }

        return res;
    }

    private void dumpArgumentInfo(CliArgumentManager manager, StringBuilder s, List<String> preappend, String toDump) {
        for (String string : preappend) {
            s.append(string);
        }
        preappend.clear();
        addReturnedDescLine2(manager, s, toDump);
        preappend.add(HELPER_PREAPPEND_NORMAL);
    }

    private void addReturnedDescLine2(CliArgumentManager manager, StringBuilder s, String line) {
        int lastPos = 0;
        for (int i = 0; i < line.length(); lastPos = i) {
            int end;
            if (i == 0) {
                end = i + HELPER_DESCRIPTION_LENGTH;
            } else {
                // on second line there is an indentation for the same
                // information
                end = (i + HELPER_DESCRIPTION_LENGTH) - 2;
            }

            // buffer will not fill all the line
            if (end > line.length() - 1) {
                end = line.length();
            }
            if (i > 0) {
                // on second line we indent the begenning of the line
                s.append(HELPER_PREAPPENC_SHIFT);
            }

            if (end != line.length() && line.charAt(end - 1) != ' ' && line.charAt(end) != ' ') {
                for (int j = end; j > lastPos; j--) {
                    if (line.charAt(j) == ' ') {
                        end = j + 1;
                        break;
                    }
                }
            }

            s.append(line.subSequence(i, end));
            s.append(manager.getNewLine());
            i = end;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the helperHeader
     */
    public String[] getHelperHeader() {
        return helperHeader;
    }

    /**
     * @param helperHeader
     *            the helperHeader to set
     */
    public void setHelperHeader(String[] helperHeader) {
        this.helperHeader = helperHeader;
    }

    /**
     * @return the helperFooter
     */
    public String[] getHelperFooter() {
        return helperFooter;
    }

    /**
     * @param helperFooter
     *            the helperFooter to set
     */
    public void setHelperFooter(String[] helperFooter) {
        this.helperFooter = helperFooter;
    }

}
