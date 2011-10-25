package net.awired.aclm.param;

import net.awired.aclm.argument.CliArgumentParseException;

public class CliParamChar extends CliParam<Character> {

    public CliParamChar(String name) {
        super(name);
    }

    @Override
    public Character parse(String param) throws CliArgumentParseException {
        if (param.length() > 1) {
            throw new CliArgumentParseException(param + " is not a valid argument");
        }
        return param.charAt(0);
    }

}
