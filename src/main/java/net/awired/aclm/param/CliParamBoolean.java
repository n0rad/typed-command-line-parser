package net.awired.aclm.param;

import net.awired.aclm.argument.CliArgumentParseException;

public class CliParamBoolean extends CliParam<Boolean> {

    public CliParamBoolean(String name) {
        super(name);
    }

    @Override
    public Boolean parse(String res) throws CliArgumentParseException {
        if (res == null) {
            return false;
        }
        if (res.equalsIgnoreCase("yes") || res.equalsIgnoreCase("y") || res.equalsIgnoreCase("o")
                || res.equalsIgnoreCase("oui") || res.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

}
