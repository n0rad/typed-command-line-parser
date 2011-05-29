package net.awired.ajsl.cli.argument;

import java.io.PrintStream;
import net.awired.ajsl.cli.argument.interfaces.CliErrorManager;

/**
 * This is the default error manager for the argument manager.
 * 
 * 
 * @author n0rad
 * 
 */
public class CliDefaultErrorManager implements CliErrorManager {

    /** Use the path showing the character where there is a probleme. */
    private boolean usagePath = true;

    /////////////////////////////////////////////////////////////////////////////

    /**
     * show the full error messages by writing exceptions (readable parse stacktrace error), usage and information.
     * 
     * @param args
     *            CLI arguments set for this parse
     * @param manager
     *            the {@link CliArgumentManager}
     * @param exception
     *            current exception done by the parse
     */
    @Override
    public void usageShowException(String[] args, CliArgumentManager manager, CliArgumentParseException exception) {
        boolean loop = false;
        Throwable causes = exception;
        int padding = 0;
        while (!loop) {
            if (causes == null) {
                loop = true;
            } else {
                showError(args, causes, padding, manager);
                causes = causes.getCause();
                padding++;
            }
        }

        // show usage
        manager.getUsageDisplayer().displayUsage(manager, manager.getErrorStream());

        // show info
        manager.getUsageDisplayer().displayInfo(manager, manager.getErrorStream());

        System.exit(1); // NOPMD
    }

    /**
     * 
     * Write the current error to the errStream using the current padding.
     * 
     * @param args
     *            CLI arguments set for this parse
     * @param exception
     *            exception done while parsing those args
     * @param padding
     *            current padding to use
     * @param manager
     *            the {@link CliArgumentManager}
     */
    private void showError(String[] args, Throwable exception, int padding, CliArgumentManager manager) {
        PrintStream errStream = manager.getErrorStream();

        if (padding == 0) {
            errStream.print(manager.getProgramName());
            errStream.print(": ");
        } else {
            errStream.print("May be the root cause : ");
            errStream.print(manager.getNewLine());
        }
        errStream.print(getPadding("    ", padding));
        errStream.print(exception.toString());
        errStream.print(manager.getNewLine());
        if (usagePath && exception instanceof CliArgumentParseException) {
            CliArgumentParseException parseException = (CliArgumentParseException) exception;
            showPath(args, parseException.getArgsNum(), parseException.getArgsPos(), padding, manager);
        }
    }

    /**
     * This method is used to create a padding of the padding String.
     * it is used by the path showing the error character.
     * 
     * @param padder
     *            the padding string to spread
     * @param padding
     *            number of padding
     * @return a padding String
     */
    private String getPadding(String padder, int padding) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            builder.append(padder);
        }
        return builder.toString();
    }

    /**
     * 
     * 
     * 
     * @param args
     *            CLI arguments set for this parse
     * @param argsNum
     *            the argument number causing problems
     * @param argsPos
     *            character position in the argument where the problem is
     * @param padding
     *            number of padding
     * @param manager
     *            the {@link CliArgumentManager}
     */
    private void showPath(String[] args, int argsNum, int argsPos, int padding, CliArgumentManager manager) {
        PrintStream errStream = manager.getErrorStream();
        errStream.print(getPadding("    ", padding));
        errStream.print("  ");
        errStream.print(manager.getProgramName());
        for (int i = 0; i < args.length; i++) {
            errStream.print(' ');
            errStream.print(args[i]);
        }
        errStream.print(manager.getNewLine());
        errStream.print(getPadding("____", padding));
        errStream.print("__");
        // spaces
        for (int j = 0; j < manager.getProgramName().length(); j++) {
            errStream.print('_');
        }
        for (int i = 0; i < args.length; i++) {
            errStream.print('_');
            if (i == argsNum) {
                for (int j = 0; j < argsPos; j++) {
                    errStream.print('_');
                }
                errStream.print('^');
                errStream.print(manager.getNewLine());
                break;
            } else {
                for (int j = 0; j < args[i].length(); j++) {
                    errStream.print('_');
                }
            }
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the usagePath
     */
    @Override
    public boolean isUsagePath() {
        return usagePath;
    }

    /**
     * @param usagePath
     *            the usagePath to set
     */
    @Override
    public void setUsagePath(boolean usagePath) {
        this.usagePath = usagePath;
    }
}
