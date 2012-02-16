package net.awired.aclm.argument;

import java.util.ArrayList;
import java.util.List;
import net.awired.aclm.argument.args.CliNoParamArgument;
import net.awired.aclm.argument.args.CliOneParamArgument;
import net.awired.aclm.argument.interfaces.CliArgument;
import net.awired.aclm.argument.interfaces.CliArgumentParser;
import net.awired.aclm.argument.interfaces.CliHelperArgument;

/**
 * This class represent the default parser for {@link CliArgumentManager}.
 * 
 * You can extend this class or inject a {@link CliArgumentParser} to change the behavior of the parser.
 * the parser is used by the manager to extract datas from the CLI args set by the user. those datas will feed
 * arguments and default arguments of the manager.
 * The parser can be configured (using type* properties) to work as a strict parser or trying to understand what the
 * user set in the CLI by parsing also parameters : see properties for more informations.
 * 
 * @author n0rad
 * 
 */
public class CliDefaultParser implements CliArgumentParser {

    /**
     * Read arguments to know if its really an argument or a parameter.
     * in this exemple : read -f to know if its a param of -r or a new argument
     * ./toto42 -r 1 a 2 b -f 3
     * 
     * @param programName
     *            your program name printed on helper
     */
    private boolean typeRead = true;

    /**
     * Scan argument in short form to find if other arguments is appended to it (only working if only one argument in
     * the pool is not a {@link CliNoParamArgument} ).
     * ./toto42 -vf 3
     */
    private boolean typeScanShortNameArguments = true;

    /**
     * Scan argument in short form to find if a param is appended to it (only working if argument is a
     * {@link CliOneParamArgument}).
     * ./toto42 -r 1 a -f3
     */
    private boolean typeScanShortName = true;

    /**
     * Scan argument in long form to find if a param is appended to it (only working if argument is a
     * {@link CliOneParamArgument}).
     * ./toto42 -r 1 a --file=3
     */
    private boolean typeScanLongName = true;

    /**
     * Tell the parser that an argument with a dash (-) can only be an argument and can not be a parameter starting by
     * a dash.
     */
    private boolean dashIsArgumentOnly = true;

    private List<CliArgument> arguments;

    private CliArgument defaultArgument;

    private CliHelperArgument helperArgument;

    @Override
    public void parse(String[] args, CliArgumentManager manager) throws CliArgumentParseException {
        this.defaultArgument = manager.getDefaultArgument();
        this.helperArgument = manager.getHelperArgument();
        this.arguments = manager.getArguments();
        // parse
        CliArgument currentArgument = null;
        boolean argumentStop = false;

        try {
            for (int position = 0; position < args.length; position++) {
                // if helper was set during parse its not necessary to continue
                if (helperArgument.isSet()) {
                    break;
                }

                // argument breaker, after that we send everything to default argument
                if (args[position].equals("--")) {
                    argumentStop = true;
                    position++;
                }

                position += parseArgument(args, position, argumentStop);

                //                if (currentArgument == null) {
                //                    throw new CliArgumentParseException("invalid argument -- '" + newArgs.get(i) + "'");
                //                }
                //                if (newArgs.size() - position < currentArgument.getNumberOfArguments()) {
                //                    throw new CliArgumentParseException("Need more params");
                //                }
                //                currentArgument.parse(newArgs);
                //                //TODO
                //                if (currentArgument == helperArgument && currentArgument.isSet()) {
                //                    helperArgument.help(arguments, defaultArgument);
                //                }
            }
        } catch (CliArgumentParseException e) {
            if (e.getCurrentArgument() == null) {
                e.setCurrentArgument(currentArgument);
            }
            manager.getErrorManager().usageShowException(args, manager, e);
        }
        if (manager.getHelperArgument().isSet()) {
            manager.getHelperArgument().help(manager);
        }

    }

    private int parseArgument(String[] args, int position, boolean argStop) throws CliArgumentParseException {

        if (!argStop) {
            for (CliArgument argument : arguments) {
                if (argument == defaultArgument) {
                    continue;
                }

                // shortname
                if (args[position].startsWith(argument.getShortName())) {
                    if (args[position].length() != argument.getShortName().length()) {
                        // -f42 or -xvf filename
                        // FIXME catch exception and continue?
                        return parseShortNameWithAppend(args, position, argument);
                    } else {
                        // -f toto titi (normal form)
                        try {
                            argument.parse(buildParamsForArgument(args, position + 1, argument));
                        } catch (CliArgumentParseException e) {
                            e.setArgsNum(position);
                            e.setArgsPos(1);
                            throw e;
                        }
                        int readedParsed = parseTypeRead(args, position + 1 + argument.getNumberOfParams(), argument);
                        return readedParsed + argument.getNumberOfParams();
                    }
                }

                if (typeScanLongName && argument.getNumberOfParams() == 1) {
                    // long name with appended
                    if (args[position].startsWith(argument.getName() + "=")) {
                        // --file-name=filename.txt
                        return parseLongName(argument, argument.getName(), args[position]);
                    }

                    // hidden names
                    List<String> hnames = argument.getHiddenNames();
                    for (String name : hnames) {
                        if (args[position].startsWith(name + "=")) {
                            return parseLongName(argument, name, args[position]);
                        }
                    }
                }

                // long name
                if (args[position].equals(argument.getName())) {
                    argument.parse(buildParamsForArgument(args, position + 1, argument));
                    int readedParsed = parseTypeRead(args, position + argument.getNumberOfParams(), argument);
                    return readedParsed + argument.getNumberOfParams();
                }

                // hidden names
                List<String> hnames = argument.getHiddenNames();
                for (String name : hnames) {
                    if (args[position].equals(name)) {
                        argument.parse(buildParamsForArgument(args, position + 1, argument));
                        int readedParsed = parseTypeRead(args, position + argument.getNumberOfParams(), argument);
                        return readedParsed + argument.getNumberOfParams();
                    }
                }
            }
        }

        // default argument
        defaultArgument.parse(buildParamsForArgument(args, position, defaultArgument));
        return defaultArgument.getNumberOfParams();
    }

    private List<String> buildParamsForArgument(String[] args, int firstParamPos, CliArgument current)
            throws CliArgumentParseException {
        if (current == null) {
            throw new CliArgumentParseException("no default argument to parse : " + args[firstParamPos]);
        }
        if (args.length < firstParamPos + current.getNumberOfParams()) {
            throw new CliArgumentParseException("not enougth params", current);
        }
        List<String> params = new ArrayList<String>();
        for (int i = 0; i < current.getNumberOfParams(); i++) {
            params.add(args[firstParamPos + i]);
        }
        return params;
    }

    private int parseTypeRead(String[] args, int position, CliArgument current) throws CliArgumentParseException {
        if (position >= args.length) {
            return 0;
        }

        if (typeRead && current.getNumberOfParams() > 0
                && (current.getMultiCallMax() == 0 || current.getNumCall() < current.getMultiCallMax())) {
            // FIXME loop to di that more than once
            List<String> params = new ArrayList<String>();
            for (int i = 0; i < current.getNumberOfParams(); i++) {
                params.add(args[position + i]);
            }
            current.parse(params);
            return current.getNumberOfParams();
        }
        return 0;
    }

    private int parseLongName(CliArgument argument, String name, String arg) throws CliArgumentParseException {
        List<String> params = new ArrayList<String>();
        // +1 for the =
        params.add(arg.substring(name.length() + 1));
        argument.parse(params);
        return argument.getNumberOfParams();
    }

    private int parseShortNameWithAppend(String[] args, int position, CliArgument current)
            throws CliArgumentParseException {
        // typeScanShortName
        if (typeScanShortName && current.getNumberOfParams() == 1) {
            try {
                // -f42
                List<String> params = new ArrayList<String>();
                params.add(args[position].substring(current.getShortName().length()));
                current.parse(params);
                return 0;
            } catch (CliArgumentParseException e) {

                e.setCurrentArgument(current);
                e.setArgsNum(position);
                e.setArgsPos(1);
                if (!typeScanShortNameArguments) {
                    throw e;
                }
            }
        }

        if (!typeScanShortNameArguments) {
            throw new CliArgumentParseException("Argument can not be parsed as a sortname with appended elements");
        }

        // -xvfz filename
        CliArgument paramedArg = null;
        String paramedArgStickedParam = null;
        int paramedArgPosition = 0;
        CliArgumentParseException checkException = null;

        // first we need to find if we have a paramed argument in list
        // because we need to parse it first
        // we also check arguments as same time
        try {
            for (int i = current.getShortName().length() - 1; i < args[position].length(); i++) {
                boolean found = false;
                for (CliArgument argument2 : arguments) {
                    if (argument2 == defaultArgument) {
                        continue;
                    }

                    if (argument2.getCharName() == args[position].charAt(i)) {
                        found = true;
                        if (argument2.getNumberOfParams() >= 1) {
                            if (paramedArg != null) {
                                throw new CliArgumentParseException(
                                        "Sticked arguments can have only one paramed argument", argument2, position,
                                        i);
                            }
                            paramedArgPosition = i;
                            paramedArg = argument2;
                            if (typeScanShortName && argument2.getNumberOfParams() == 1
                                    && i < args[position].length() - 1) {
                                paramedArgStickedParam = args[position].substring(i + 1);
                            }
                        }
                        break;
                    }
                }
                if (!found) {
                    throw new CliArgumentParseException("Argument does not exists : '" + args[position].charAt(i)
                            + "'", position, i);
                }
            }
        } catch (CliArgumentParseException e) {
            if (paramedArgStickedParam == null) {
                // there is no params sticked so we can not recover from this error
                throw e;
            }
            checkException = e;
        }

        int argsToSkipForNext = 0;
        // parse paramed first as it may fail
        if (paramedArg != null) {
            // we have a paramed arg
            CliArgumentParseException stickedException = null;
            if (paramedArgStickedParam != null) {
                // sticked paramed argument
                List<String> params = new ArrayList<String>();
                // parse using sticked params
                params.add(paramedArgStickedParam);

                // try to parse sticked argument
                try {
                    paramedArg.parse(params);
                } catch (CliArgumentParseException e) {
                    stickedException = e;
                    stickedException.setCurrentArgument(paramedArg);
                    stickedException.setArgsNum(position);
                    // set position of param just after argument as its a sticked param
                    stickedException.setArgsPos(paramedArgPosition + 1);
                }
            }

            if (stickedException != null || paramedArgStickedParam == null) {
                // if we can not parse sticked param or if its not a sticked param
                if (position + paramedArg.getNumberOfParams() > args.length - 1) {
                    // not enougth params after argument to call the parser
                    throw new CliArgumentParseException("Param(s) not found after argument", paramedArg, position,
                            paramedArgPosition);
                }
                argsToSkipForNext = paramedArg.getNumberOfParams();

                List<String> params = new ArrayList<String>();
                for (int i = 0; i < paramedArg.getNumberOfParams(); i++) {
                    params.add(args[position + 1 + i]);
                }

                try {
                    paramedArg.parse(params);
                } catch (CliArgumentParseException e) {
                    e.setCurrentArgument(paramedArg);
                    int paramFromParse = 0;
                    if (e.getArgsNum() != null) {
                        paramFromParse = e.getArgsNum();
                    }
                    e.setArgsNum(position + 1 + paramFromParse);
                    e.setArgsPos(0);
                    if (stickedException != null) {
                        e.initCause(stickedException);
                    }
                    throw e;
                }
            }
        }

        if (checkException != null && argsToSkipForNext != 0) {
            // check part fail with an exception that was not recover by parsing paramed argument
            throw checkException;
        }

        boolean afterParamed = false;
        // parse no params arguments
        for (int i = current.getShortName().length() - 1; !afterParamed && i < args[position].length(); i++) {
            boolean parsed = false;
            for (CliArgument argument2 : arguments) {
                if (argument2 == defaultArgument) {
                    continue;
                }

                if (argument2.getCharName() == args[position].charAt(i)) {
                    parsed = true;
                    if (argument2 == paramedArg) {
                        // we are on the paramed arg
                        if (argsToSkipForNext == 0) {
                            // param of paramedArg is sticked to arguments so we must stop here
                            afterParamed = true;
                        }
                        break;
                    }
                    try {
                        argument2.parse(null);
                    } catch (CliArgumentParseException e) {
                        e.setArgsNum(position);
                        e.setArgsPos(i);
                        throw e;
                    }
                    break;
                }
            }
            if (!parsed) {
                throw new CliArgumentParseException("Argument does not exists : '" + args[position].charAt(i) + "'",
                        position, i);
            }
        }
        return argsToSkipForNext;

    }

    /**
     * @return the typeRead
     */
    @Override
    public boolean isTypeRead() {
        return typeRead;
    }

    /**
     * @param typeRead
     *            the typeRead to set
     */
    @Override
    public void setTypeRead(boolean typeRead) {
        this.typeRead = typeRead;
    }

    /**
     * @return the typeScanShortNameArguments
     */
    @Override
    public boolean isTypeScanShortNameArguments() {
        return typeScanShortNameArguments;
    }

    /**
     * @param typeScanShortNameArguments
     *            the typeScanShortNameArguments to set
     */
    @Override
    public void setTypeScanShortNameArguments(boolean typeScanShortNameArguments) {
        this.typeScanShortNameArguments = typeScanShortNameArguments;
    }

    /**
     * @return the typeScanShortName
     */
    @Override
    public boolean isTypeScanShortName() {
        return typeScanShortName;
    }

    /**
     * @param typeScanShortName
     *            the typeScanShortName to set
     */
    @Override
    public void setTypeScanShortName(boolean typeScanShortName) {
        this.typeScanShortName = typeScanShortName;
    }

    /**
     * @return the typeScanLongName
     */
    @Override
    public boolean isTypeScanLongName() {
        return typeScanLongName;
    }

    /**
     * @param typeScanLongName
     *            the typeScanLongName to set
     */
    @Override
    public void setTypeScanLongName(boolean typeScanLongName) {
        this.typeScanLongName = typeScanLongName;
    }

    /**
     * @return the dashIsArgumentOnly
     */
    @Override
    public boolean isDashIsArgumentOnly() {
        return dashIsArgumentOnly;
    }

    /**
     * @param dashIsArgumentOnly
     *            the dashIsArgumentOnly to set
     */
    @Override
    public void setDashIsArgumentOnly(boolean dashIsArgumentOnly) {
        this.dashIsArgumentOnly = dashIsArgumentOnly;
    }

}
