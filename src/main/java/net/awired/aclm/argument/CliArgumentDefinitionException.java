package net.awired.aclm.argument;

import net.awired.aclm.argument.interfaces.CliArgument;

public class CliArgumentDefinitionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CliArgument       currentArgument  = null;

    public CliArgumentDefinitionException(String msg) {
        super(msg);
    }

    public CliArgumentDefinitionException(String msg, CliArgument current) {
        super(msg);
        this.currentArgument = current;
    }

    @Override
    public String toString() {
        return currentArgument == null ? getMessage() : getMessage() + " : " + currentArgument.toString();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the currentArgument
     */
    public CliArgument getCurrentArgument() {
        return currentArgument;
    }

}
