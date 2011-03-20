package net.awired.ajsl.cli.argument.args;

import java.util.ArrayList;
import java.util.List;
import net.awired.ajsl.cli.argument.CliArgumentDefinitionException;
import net.awired.ajsl.cli.argument.CliArgumentParseException;
import net.awired.ajsl.cli.param.CliParam;

public class CliTwoParamArgument<PARAM_ONE_TYPE, PARAM_TWO_TYPE> extends CliOneParamArgument<PARAM_ONE_TYPE> {

    private static final int                 NUMBER_OF_PARAMS = 2;

    private List<PARAM_TWO_TYPE>             paramTwoDefValues   = new ArrayList<PARAM_TWO_TYPE>();

    private List<PARAM_TWO_TYPE>             paramTwoValues      = paramTwoDefValues;

    protected final CliParam<PARAM_TWO_TYPE> paramTwoArgument;

    // ///////////////////////////////////////////////////////////////////////////////////////////////////

    public CliTwoParamArgument(char shortName, CliParam<PARAM_ONE_TYPE> paramOneArgument,
            CliParam<PARAM_TWO_TYPE> paramTwoArgument) {
        super(shortName, paramOneArgument);
        this.paramTwoArgument = paramTwoArgument;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void reset() {
        super.reset();

        // set default values
        if (paramTwoDefValues != null) {
            paramTwoValues = paramTwoDefValues;
        }
    }

    @Override
    public String toStringArgument() {
        return getParamOneArgument().getName() + " " + getParamTwoArgument().getName();
    }

    @Override
    public String toStringValues(boolean defaultValues, boolean isDefaultArgument) {
        List<PARAM_ONE_TYPE> param1;
        List<PARAM_TWO_TYPE> param2;
        if (defaultValues) {
            param1 = getParamOneDefValues();
            param2 = getParamTwoDefValues();
        } else {
            param1 = getParamOneValues();
            param2 = getParamTwoValues();
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
                buff.append(' ');
                buff.append(param2.get(i));
            }
            return buff.toString();
        }
        return null;
    }

    @Override
    public void checkDefinition() {
        super.checkDefinition();
        if ((getParamTwoDefValues() == null && getParamOneDefValues() != null)
                || (getParamTwoDefValues() != null && getParamOneDefValues() == null)
                && getParamTwoDefValues().size() != getParamOneDefValues().size()) {
            throw new RuntimeException("length of default params values must be equal");
        }
    }

    @Override
    public void parse(List<String> args) throws CliArgumentParseException {
        // get all arguments before set to be able to throw an exception
        // on param parse without modification of this object
        PARAM_ONE_TYPE param1 = paramOneArgument.parse(args.get(0));
        PARAM_TWO_TYPE param2 = paramTwoArgument.parse(args.get(1));

        numCall++;
        addParamOneValue(param1);
        addParamTwoValue(param2);
    }

    /**
     * @param paramOneDefValue
     *            the paramOneDefValue to set
     */
    public void setParamTwoDefValue(PARAM_TWO_TYPE paramTwoDefValue) {
        this.paramTwoDefValues.clear();
        this.paramTwoDefValues.add(paramTwoDefValue);
    }

    /**
     * @return the paramOneValues
     */
    public PARAM_TWO_TYPE getParamTwoValue() {
        if (isMulticall()) {
            throw new CliArgumentDefinitionException("for multicall arguments use getParamTwoValues()");
        }
        if (!paramTwoDefValues.isEmpty()) {
            return paramTwoValues.get(0);
        }
        return null;
    }

    /**
     * @param paramTwoDefValues
     *            the paramTwoDefValues to set
     */
    public void setParamTwoDefValues(List<PARAM_TWO_TYPE> paramTwoDefValues) {
        this.paramTwoDefValues.clear();
        this.paramTwoDefValues.addAll(paramTwoDefValues);
    }

    protected void addParamTwoValue(PARAM_TWO_TYPE value) {
        if (paramTwoValues == paramTwoDefValues) {
            paramTwoValues = new ArrayList<PARAM_TWO_TYPE>();
        }
        paramTwoValues.add(value);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the paramOneArgument
     */
    public CliParam<PARAM_TWO_TYPE> getParamTwoArgument() {
        return paramTwoArgument;
    }

    /**
     * @return the paramTwoDefValues
     */
    public List<PARAM_TWO_TYPE> getParamTwoDefValues() {
        return paramTwoDefValues;
    }

    /**
     * @return the paramTwoValues
     */
    public List<PARAM_TWO_TYPE> getParamTwoValues() {
        return paramTwoValues;
    }

    /**
     * @return the numberOfArguments
     */
    public int getNumberOfParams() {
        return NUMBER_OF_PARAMS;
    }
}
