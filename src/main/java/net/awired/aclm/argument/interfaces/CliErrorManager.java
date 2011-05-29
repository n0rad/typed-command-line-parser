package net.awired.aclm.argument.interfaces;

import net.awired.aclm.argument.CliArgumentManager;
import net.awired.aclm.argument.CliArgumentParseException;


public interface CliErrorManager {

    void usageShowException(String[] args, CliArgumentManager manager, CliArgumentParseException exception);

    boolean isUsagePath();

    void setUsagePath(boolean usagePath);

}
