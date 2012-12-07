package net.awired.aclm.argument.interfaces;

import net.awired.aclm.argument.CliArgumentManager;
import net.awired.aclm.argument.CliArgumentParseException;

public interface CliArgumentParser {
    boolean parseWithSuccess(String[] args, CliArgumentManager manager) throws CliArgumentParseException;

    void setDashIsArgumentOnly(boolean dashIsArgumentOnly);

    boolean isDashIsArgumentOnly();

    void setTypeScanLongName(boolean typeScanLongName);

    boolean isTypeScanLongName();

    void setTypeScanShortName(boolean typeScanShortName);

    boolean isTypeScanShortName();

    void setTypeScanShortNameArguments(boolean typeScanShortNameArguments);

    boolean isTypeScanShortNameArguments();

    void setTypeRead(boolean typeRead);

    boolean isTypeRead();
}
