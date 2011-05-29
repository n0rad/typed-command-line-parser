package net.awired.ajsl.cli.argument.interfaces;

import java.util.List;
import net.awired.ajsl.cli.argument.CliArgumentParseException;

public interface CliArgument extends Comparable<CliArgument> {
    String getShortName();

    List<CliArgument> getNeededArguments();

    List<CliArgument> getForbiddenArguments();

    String getName();

    char getCharName();

    void reset();

    String getDescription();

    boolean isMulticall();

    int getNumCall();

    String toStringArgument();

    boolean isMandatory();

    List<String> getHiddenNames();

    void checkDefinition();

    int getNumberOfParams();

    boolean isSet();

    boolean isHelpHidden();

    boolean isUsageHidden();

    int getMultiCallMax();

    int getMultiCallMin();

    void checkParse(List<CliArgument> arguments) throws CliArgumentParseException;

    void parse(List<String> params) throws CliArgumentParseException;

    void setMultiCallMax(int multiCallMax);

    void setMultiCallMin(int multiCallMin);

}
