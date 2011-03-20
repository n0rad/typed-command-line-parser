package net.awired.ajsl.cli.argument.interfaces;

import net.awired.ajsl.cli.argument.CliArgumentManager;
import net.awired.ajsl.cli.argument.CliArgumentParseException;


public interface CliArgumentParser {
    void parse(String[] args, CliArgumentManager manager) throws CliArgumentParseException;

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
