package net.awired.ajsl.cli.argument.interfaces;

import net.awired.ajsl.cli.argument.CliArgumentManager;
import net.awired.ajsl.cli.argument.CliArgumentParseException;


public interface CliErrorManager {

    void usageShowException(String[] args, CliArgumentManager manager, CliArgumentParseException exception);

    boolean isUsagePath();

    void setUsagePath(boolean usagePath);

}
