package net.awired.ajsl.cli.param;

import net.awired.ajsl.cli.argument.CliArgumentParseException;

public abstract class CliParam<PARAM_TYPE> {

    private String       paramDescription;
    private final String name;

    public CliParam(String name) {
        this.name = name;
    }

    public abstract PARAM_TYPE parse(String param) throws CliArgumentParseException;

    @Override
    public String toString() {
        return name;
    }

    /**
     * @param paramDescription
     *            the paramDescription to set
     */
    public CliParam<PARAM_TYPE> setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription;
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the paramDescription
     */
    public String getParamDescription() {
        return paramDescription;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
