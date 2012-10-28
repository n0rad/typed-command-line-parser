package net.awired.aclm.argument.interfaces;

import java.io.PrintStream;
import net.awired.aclm.argument.CliArgumentManager;

public interface CliUsageDisplayer {

    void setUsageShort(boolean usageShort);

    boolean isUsageShort();

    void displayUsage(CliArgumentManager manager, PrintStream output);

    void displayInfo(CliArgumentManager manager, PrintStream output);

}
