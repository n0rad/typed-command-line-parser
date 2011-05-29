package net.awired.ajsl.cli.param;

import net.awired.ajsl.cli.argument.CliArgumentParseException;

public class CliParamFloat extends CliParam<Float> {

    public CliParamFloat(String name) {
        super(name);
    }

    @Override
    public Float parse(String param) throws CliArgumentParseException {
        try {
            return Float.parseFloat(param);
        } catch (NumberFormatException e) {
            throw new CliArgumentParseException(param + " is not a valid float");
        }
    }
}
