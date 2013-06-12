/**
 *
 *     Copyright (C) Awired.net
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package net.awired.typed.command.line.parser.argument.args;

import java.util.ArrayList;
import java.util.List;
import net.awired.typed.command.line.parser.argument.CliArgumentDefinitionException;
import net.awired.typed.command.line.parser.argument.CliArgumentParseException;
import net.awired.typed.command.line.parser.param.CliParam;

public class CliTwoParamArgument<PARAM_ONE_TYPE, PARAM_TWO_TYPE> extends CliOneParamArgument<PARAM_ONE_TYPE> {

    private static final int NUMBER_OF_PARAMS = 2;

    private List<PARAM_TWO_TYPE> paramTwoDefValues = new ArrayList<PARAM_TWO_TYPE>();

    private List<PARAM_TWO_TYPE> paramTwoValues = paramTwoDefValues;

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
            throw new CliArgumentDefinitionException("length of default params values must be equal");
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

    public void setParamTwoDefValue(PARAM_TWO_TYPE paramTwoDefValue) {
        this.paramTwoDefValues.clear();
        this.paramTwoDefValues.add(paramTwoDefValue);
    }

    public PARAM_TWO_TYPE getParamTwoValue() {
        if (isMulticall()) {
            throw new CliArgumentDefinitionException("for multicall arguments use getParamTwoValues()");
        }
        if (!paramTwoDefValues.isEmpty()) {
            return paramTwoValues.get(0);
        }
        return null;
    }

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

    public CliParam<PARAM_TWO_TYPE> getParamTwoArgument() {
        return paramTwoArgument;
    }

    public List<PARAM_TWO_TYPE> getParamTwoDefValues() {
        return paramTwoDefValues;
    }

    public List<PARAM_TWO_TYPE> getParamTwoValues() {
        return paramTwoValues;
    }

    @Override
    public int getNumberOfParams() {
        return NUMBER_OF_PARAMS;
    }
}
