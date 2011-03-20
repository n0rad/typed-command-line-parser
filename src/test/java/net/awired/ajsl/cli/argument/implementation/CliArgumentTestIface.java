package net.awired.ajsl.cli.argument.implementation;

import net.awired.ajsl.cli.argument.CliArgumentManager;

public interface CliArgumentTestIface<MANAGER extends CliArgumentManager> {

    public String[] getArgs();

    public String getErr();

    public String getOut();

    public void preTest();

    public void postTest();

    public void checkResult(MANAGER argManager, boolean exit, String exception);

    public void checkResult(MANAGER argManager, boolean exit);

    public MANAGER getManager();

}
