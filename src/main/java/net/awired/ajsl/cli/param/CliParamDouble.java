package net.awired.ajsl.cli.param;

import net.awired.ajsl.cli.argument.CliArgumentParseException;

public class CliParamDouble extends CliParam<Double> {

    private boolean zeroable    = true;

    private boolean negativable = true;

    public CliParamDouble(String name) {
        super(name);
    }

    @Override
    public Double parse(String param) throws CliArgumentParseException {
        double value;
        try {
            value = Double.parseDouble(param);
        } catch (NumberFormatException e) {
            throw new CliArgumentParseException(param + " is not a valid double");
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
