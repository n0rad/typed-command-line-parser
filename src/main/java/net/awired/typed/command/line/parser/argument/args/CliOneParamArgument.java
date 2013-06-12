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

public class CliOneParamArgument<PARAM_ONE_TYPE> extends CliNoParamArgument {

    private static final int NUMBER_OF_PARAMS = 1;

    private List<PARAM_ONE_TYPE> paramOneDefValues = new ArrayList<PARAM_ONE_TYPE>();

    private List<PARAM_ONE_TYPE> paramOneValues = paramOneDefValues;

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

    public void setParamOneDefValue(PARAM_ONE_TYPE paramOneDefValue) {
        this.paramOneDefValues.clear();
        this.paramOneDefValues.add(paramOneDefValue);
    }

    public PARAM_ONE_TYPE getParamOneValue() {
        if (isMulticall()) {
            throw new CliArgumentDefinitionException("for multicall arguments use getParamOneValues()");
        }
        if (!paramOneValues.isEmpty()) {
            return paramOneValues.get(0);
        }
        return null;
    }

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

    public CliParam<PARAM_ONE_TYPE> getParamOneArgument() {
        return paramOneArgument;
    }

    public List<PARAM_ONE_TYPE> getParamOneDefValues() {
        return paramOneDefValues;
    }

    public List<PARAM_ONE_TYPE> getParamOneValues() {
        return paramOneValues;
    }

    @Override
    public int getNumberOfParams() {
        return NUMBER_OF_PARAMS;
    }
}
