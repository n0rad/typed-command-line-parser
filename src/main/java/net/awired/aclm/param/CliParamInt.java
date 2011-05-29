package net.awired.aclm.param;

import net.awired.aclm.argument.CliArgumentParseException;

public class CliParamInt extends CliParam<Integer> {

    private boolean zeroable    = true;

    private boolean negativable = true;

    public CliParamInt(String name) {
        super(name);
    }

    @Override
    public Integer parse(String param) throws CliArgumentParseException {
        int value;
        try {
            value = Integer.parseInt(param);
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

    /**
     * @return the zeroable
     */
    public boolean isZeroable() {
        return zeroable;
    }

    /**
     * @param zeroable
     *            the zeroable to set
     */
    public void setZeroable(boolean zeroable) {
        this.zeroable = zeroable;
    }

    /**
     * @return the negativable
     */
    public boolean isNegativable() {
        return negativable;
    }

    /**
     * @param negativable
     *            the negativable to set
     */
    public void setNegativable(boolean negativable) {
        this.negativable = negativable;
    }

}
