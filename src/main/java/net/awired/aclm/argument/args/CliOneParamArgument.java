package net.awired.aclm.argument.args;

import java.util.ArrayList;
import java.util.List;
import net.awired.aclm.argument.CliArgumentDefinitionException;
import net.awired.aclm.argument.CliArgumentParseException;
import net.awired.aclm.param.CliParam;

public class CliOneParamArgument<PARAM_ONE_TYPE> extends CliNoParamArgument {

    private static final int                 NUMBER_OF_PARAMS = 1;

    private List<PARAM_ONE_TYPE>             paramOneDefValues   = new ArrayList<PARAM_ONE_TYPE>();

    private List<PARAM_ONE_TYPE>             paramOneValues      = paramOneDefValues;

    protected final CliParam<PARAM_ONE_TYPE> paramOneArgument;

    // ///////////////////////////////////////////////////////////////////////////////////////////////////

    public CliOneParamArgument(char shortName, CliParam<PARAM_ONE_TYPE> paramOneArgument) {
        super(shortName);
        this.paramOneArgument = paramOneArgument;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void reset() {
        super.reset();
        // set default values
        if (paramOneDefValues != null) {
            paramOneValues = paramOneDefValues;
        }
    }

    @Override
    public String toStringValues(boolean defaultValues, boolean isDefaultArgument) {
        List<PARAM_ONE_TYPE> param1;
        if (defaultValues) {
            param1 = paramOneDefValues;
        } else {
            param1 = paramOneValues;
        }
        if (param1 != null && param1.size() != 0) {
            StringBuilder buff = new StringBuilder();
            for (int i = 0; i < param1.size(); i++) {
                if (i != 0) {
                    buff.append(' ');
                }
                if (!isDefaultArgument) {
                    buff.append(getShortName());
                    buff.append(' ');
                }
                buff.append(param1.get(i));
            }
            return buff.toString();
        }
        return null;
    }

    @Override
    public String toStringArgument() {
        return getParamOneArgument().getName();
    }

    @Override
    public void checkDefinition() {
        super.checkDefinition();
    }

    @Override
    public void parse(List<String> args) throws CliArgumentParseException {
        // get all arguments before set to be able to throw an exception
        // on param parse without modification of this object
        PARAM_ONE_TYPE param1 = paramOneArgument.parse(args.get(0));

        numCall++;
        addParamOneValue(param1);
    }

    /**
     * @param paramOneDefValue
     *            the paramOneDefValue to set
     */
    public void setParamOneDefValue(PARAM_ONE_TYPE paramOneDefValue) {
        this.paramOneDefValues.clear();
        this.paramOneDefValues.add(paramOneDefValue);
    }

    /**
     * @return the paramOneValues
     */
    public PARAM_ONE_TYPE getParamOneValue() {
        if (isMulticall()) {
            throw new CliArgumentDefinitionException("for multicall arguments use getParamOneValues()");
        }
        if (!paramOneValues.isEmpty()) {
            return paramOneValues.get(0);
        }
        return null;
    }

    /**
     * @param paramOneDefValues
     *            the paramOneDefValues to set
     */
    public void setParamOneDefValues(List<PARAM_ONE_TYPE> paramOneDefValues) {
        this.paramOneDefValues.clear();
        this.paramOneDefValues.addAll(paramOneDefValues);
    }

    protected void addParamOneValue(PARAM_ONE_TYPE value) {
        if (paramOneValues == paramOneDefValues) {
            paramOneValues = new ArrayList<PARAM_ONE_TYPE>();
        }
        paramOneValues.add(value);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the paramOneArgument
     */
    public CliParam<PARAM_ONE_TYPE> getParamOneArgument() {
        return paramOneArgument;
    }

    /**
     * @return the paramOneDefValues
     */
    public List<PARAM_ONE_TYPE> getParamOneDefValues() {
        return paramOneDefValues;
    }

    /**
     * @return the paramOneValues
     */
    public List<PARAM_ONE_TYPE> getParamOneValues() {
        return paramOneValues;
    }

    /**
     * @return the numberOfArguments
     */
    public int getNumberOfParams() {
        return NUMBER_OF_PARAMS;
    }
}
