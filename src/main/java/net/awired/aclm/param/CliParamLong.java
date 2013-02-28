package net.awired.aclm.param;

import net.awired.aclm.argument.CliArgumentParseException;

public class CliParamLong extends CliParam<Long> {

    private boolean zeroable = true;

    private boolean negativable = true;

    public CliParamLong(String name) {
        super(name);
    }

    @Override
    public Long parse(String param) throws CliArgumentParseException {
        long value;
        try {
            value = Long.parseLong(param);
        } catch (NumberFormatException e) {
            throw new CliArgumentParseException(param + " is not a valid Integer");
        }

        if (!zeroable && value == 0) {
            throw new CliArgumentParseException("can not be == 0");
        }

        if (!negativable && value < 0) {
            throw new CliArgumentParseException("can not be < 0");
        }
        return value;
    }

    //////////////////////////////////////////////////////////

    public boolean isZeroable() {
        return zeroable;
    }

    public void setZeroable(boolean zeroable) {
        this.zeroable = zeroable;
    }

    public boolean isNegativable() {
        return negativable;
    }

    public void setNegativable(boolean negativable) {
        this.negativable = negativable;
    }

}
