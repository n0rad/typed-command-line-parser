/**
 * $Id:$
 *
 * Labo NTIC Production 2008
 * 
 * @author n0rad
 * @creation 19 oct. 2008
 * @version 1.0
 */
package net.awired.aclm.argument;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.awired.aclm.argument.args.CliDefaultHelperArgument;
import net.awired.aclm.argument.args.CliNoParamArgument;
import net.awired.aclm.argument.interfaces.CliArgument;
import net.awired.aclm.argument.interfaces.CliArgumentParser;
import net.awired.aclm.argument.interfaces.CliErrorManager;
import net.awired.aclm.argument.interfaces.CliHelperArgument;
import net.awired.aclm.argument.interfaces.CliUsageDisplayer;

/**
 * TODO: manage adding argument twice
 * TODO: changed default values of argument to string (see default value in helper for inetaddress for exemple)
 * TODO: tostring et tostring pour l'argument default
 * type to go throw parse and be printable in helper
 * 
 * @author n0rad
 */
public class CliArgumentManager {
    /** List of Arguments for this Manager (default argument exclude). */
    private final List<CliArgument> arguments    = new ArrayList<CliArgument>();
    /** Default argument if no -X is set in the cli. */
    private CliArgument             defaultArgument;
    /** Helper argument. */
    private CliHelperArgument       helperArgument;
    /** Argument parser. */
    private CliArgumentParser       parser;

    private CliUsageDisplayer       usageDisplayer;

    private CliErrorManager         errorManager;

    private PrintStream             errorStream  = System.err;
    private PrintStream             outputStream = System.out;
    private String                  newLine      = System.getProperty("line.separator");
    private final String            programName;

    // /////////////////////////////////////////////////////
    /**
     * Default argument manager constructor.
     * The default constructor of {@link CliArgumentManager} add the argument
     * helper --help (-h) to the list of arguments.
     */
    protected CliArgumentManager(String programName) {

        this.programName = programName;
        helperArgument = new CliDefaultHelperArgument('h');
        parser = new CliDefaultParser();
        usageDisplayer = new CliDefaultUsageDisplayer();
        errorManager = new CliDefaultErrorManager();
        arguments.add(helperArgument);
    }

    public void loadAllNeeded(CliArgument current, Set<CliArgument> result) {
        result.addAll(current.getNeededArguments());
        for (CliArgument cliArgument : current.getNeededArguments()) {
            loadAllNeeded(cliArgument, result);
        }
    }

    public void loadAllForbidden(Set<CliArgument> needed, Map<CliArgument, CliArgument> result) {
        for (CliArgument neededArgs : needed) {
            for (CliArgument forbidden : neededArgs.getForbiddenArguments()) {
                result.put(forbidden, neededArgs);
            }
        }
    }

    /**
     * Check that argument that needs argument did not forbid a far needed
     * argument.
     * 
     * @param current
     *            The current argument
     * @param toCheck
     *            Argument to check (used by recursion) for first use set current twice
     */
    public void checkCircularWithForbidden(CliArgument current) {
        // get neededd rec for this argument
        Set<CliArgument> neededRec = new HashSet<CliArgument>();
        loadAllNeeded(current, neededRec);
        // get forbidden rec for this argument and needed args
        Map<CliArgument, CliArgument> forbidRec = new HashMap<CliArgument, CliArgument>();
        loadAllForbidden(neededRec, forbidRec);

        Set<CliArgument> forbidKeySet = forbidRec.keySet();
        // check that needed arguments is not present in forbidden
        for (CliArgument cliArgument : neededRec) {
            if (forbidKeySet.contains(cliArgument)) {
                throw new CliArgumentDefinitionException("argument " + current + " need " + cliArgument
                        + " by dependency but the needed argument" + forbidRec.get(cliArgument) + " forbid it",
                        cliArgument);
            }
        }
        if (forbidKeySet.contains(current)) {
            throw new CliArgumentDefinitionException("argument " + current + " has a needed argument "
                    + forbidRec.get(current) + " that forbit it ", current);
        }
    }

    /**
     * Check that current argument manager definition is correct.
     * 
     * If a problem is found a {@link CliArgumentDefinitionException} will be throw.
     * It will check that needed and forbidden arguments exists in current manager, that needed arguments are not also
     * in forbidden list, duplication names, and will call checkDefinition() of arguments.
     */
    public void checkDefinition() {
        for (CliArgument cliArgument : arguments) {
            for (CliArgument needed : cliArgument.getNeededArguments()) {
                // check that needed args exists in manager
                if (!arguments.contains(needed)) {
                    throw new CliArgumentDefinitionException("argument " + cliArgument + " contain needed arg "
                            + needed + " that is not referenced in manager");
                }
                // check that argument is not in needed and forbidden
                if (cliArgument.getForbiddenArguments().contains(needed)) {
                    throw new CliArgumentDefinitionException("argument " + needed
                            + " can not be needed and forbidden for argument " + cliArgument);
                }
            }
            // check that forbidden args exists in manager
            for (CliArgument forbidden : cliArgument.getForbiddenArguments()) {
                if (!arguments.contains(forbidden)) {
                    throw new CliArgumentDefinitionException("argument " + cliArgument.getShortName()
                            + " contain forbidden arg " + forbidden + " that is not referenced in manager");
                }
            }
            for (CliArgument cliArgument2 : arguments) {
                if (cliArgument != cliArgument2) {
                    // check shortname duplication
                    if (cliArgument.getShortName().equals(cliArgument2.getShortName())) {
                        throw new CliArgumentDefinitionException("shortname duplication : "
                                + cliArgument.getShortName());
                    }

                    // check name duplication
                    if (cliArgument.getName() != null && cliArgument.getName().equals(cliArgument2.getName())) {
                        throw new CliArgumentDefinitionException("name duplication : " + cliArgument.getShortName());
                    }

                    // check hiddenName duplication
                    for (String cliHiddenName : cliArgument.getHiddenNames()) {
                        if (cliArgument2.getHiddenNames().contains(cliHiddenName)) {
                            throw new CliArgumentDefinitionException("hidden name duplication : " + cliHiddenName);
                        }
                    }
                }
            }
            // call argument checkDefinition
            cliArgument.checkDefinition();
            // check forbidden conflict with needed on
            checkCircularWithForbidden(cliArgument);
        }
        // default arg should not have names and should not be a cliArgument
        if (getDefaultArgument() != null) {
            if (getDefaultArgument().getName() != null) {
                throw new CliArgumentDefinitionException("default argument could not have a name");
            }
            if (getDefaultArgument().getHiddenNames() != null && !getDefaultArgument().getHiddenNames().isEmpty()) {
                throw new CliArgumentDefinitionException("default argument could not have hidden names");
            }
            if (CliNoParamArgument.class == getDefaultArgument().getClass()) {
                throw new CliArgumentDefinitionException("default argument could not be a CliArgument");
            }
        }
    }

    /**
     * @return false when you have to stop cause parse fail
     */
    public boolean parse(String[] args) {

        try {
            checkDefinition();

            for (CliArgument argument : arguments) {
                argument.reset();
            }
            //TODO: clear arguments

            if (!parser.parse(args, this)) {
                return false;
            }
            checkParse();
            return true;
        } catch (CliArgumentParseException e) {
            // call helper to show usage
            errorManager.usageShowException(args, this, e);
            return false;
        }
    }

    protected void checkParse() throws CliArgumentParseException {
        for (CliArgument argument : arguments) {
            // self argument check parse
            argument.checkParse(arguments);
            if (argument.isSet()) {
                // check needed list
                for (CliArgument needed : argument.getNeededArguments()) {
                    if (!needed.isSet()) {
                        throw new CliArgumentParseException("argument " + needed + " is needed", argument);
                    }
                }
                // ckeck forbidden list
                for (CliArgument forbidden : argument.getForbiddenArguments()) {
                    if (forbidden.isSet()) {
                        throw new CliArgumentParseException("argument " + forbidden + " is forbidden", argument);
                    }
                }
            }
        }
    }

    /**
     * Add an argument to argument list.
     * 
     * @param argument
     */
    public final void addArg(CliArgument argument) {
        arguments.add(argument);
    }

    /**
     * @param defaultArgument
     *            the defaultArgument to set
     */
    public void setDefaultArgument(CliArgument defaultArgument) {
        this.defaultArgument = defaultArgument;
        this.addArg(defaultArgument);
    }

    /**
     * @param helperArgument
     *            the helperArgument to set
     */
    public void setHelperArgument(CliHelperArgument helperArgument) {
        this.arguments.remove(this.helperArgument);
        this.helperArgument = helperArgument;
        this.addArg(helperArgument);
    }

    // //////////////////////////////////////////////////////

    /**
     * @return the defaultArgument
     */
    public CliArgument getDefaultArgument() {
        return this.defaultArgument;
    }

    /**
     * @return the arguments
     */
    public List<CliArgument> getArguments() {
        return arguments;
    }

    /**
     * @return the helperArgument
     */
    public CliHelperArgument getHelperArgument() {
        return helperArgument;
    }

    /**
     * @return the parser
     */
    public CliArgumentParser getParser() {
        return parser;
    }

    /**
     * @param parser
     *            the parser to set
     */
    public void setParser(CliArgumentParser parser) {
        this.parser = parser;
    }

    /**
     * @return the usageDisplayer
     */
    public CliUsageDisplayer getUsageDisplayer() {
        return usageDisplayer;
    }

    /**
     * @param usageDisplayer
     *            the usageDisplayer to set
     */
    public void setUsageDisplayer(CliUsageDisplayer usageDisplayer) {
        this.usageDisplayer = usageDisplayer;
    }

    /**
     * @return the errorStream
     */
    public PrintStream getErrorStream() {
        return errorStream;
    }

    /**
     * @param errorStream
     *            the errorStream to set
     */
    public void setErrorStream(PrintStream errorStream) {
        this.errorStream = errorStream;
    }

    /**
     * @return the outputStream
     */
    public PrintStream getOutputStream() {
        return outputStream;
    }

    /**
     * @param outputStream
     *            the outputStream to set
     */
    public void setOutputStream(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * @return the programName
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * @return the errorManager
     */
    public CliErrorManager getErrorManager() {
        return errorManager;
    }

    /**
     * @param errorManager
     *            the errorManager to set
     */
    public void setErrorManager(CliErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * @return the newLine
     */
    public String getNewLine() {
        return newLine;
    }

    /**
     * @param newLine
     *            the newLine to set
     */
    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

}
