/**
 *
 *     Copyright (C) norad.fr
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
package fr.norad.typed.command.line.parser.argument.args;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fr.norad.typed.command.line.parser.argument.CliArgumentDefinitionException;
import fr.norad.typed.command.line.parser.argument.CliArgumentParseException;
import fr.norad.typed.command.line.parser.param.CliParam;

/**
 * @deprecated Not fully Tested
 */
@Deprecated
@SuppressWarnings("deprecation")
public class CliNParamArgument<PARAM_TYPE> extends CliNoParamArgument {

    private final List<CliParam<?>> paramArguments = new ArrayList<CliParam<?>>();

    private final Map<CliParam<?>, List<Object>> values = new HashMap<CliParam<?>, List<Object>>();

    private Map<CliParam<?>, List<Object>> defaultValues;

    public CliNParamArgument(char shortName) {
        super(shortName);
    }

    // ////////////////////////////////////////////////////////////////////

    public void addParam(CliParam<Object> param) {
        paramArguments.add(param);
        values.put(param, new ArrayList<Object>());
        defaultValues.put(param, null);
    }

    @Override
    public void reset() {
        super.reset();

        // remove old values
        Collection<List<Object>> val = values.values();
        for (List<?> list : val) {
            list.clear();
        }

        // put default values
        if (defaultValues != null) {
            for (CliParam<?> cliArgumentParam : defaultValues.keySet()) {
                if (defaultValues.get(cliArgumentParam) != null) {
                    values.get(cliArgumentParam).addAll(defaultValues.get(cliArgumentParam));
                }
            }
        }
    }

    @Override
    public String toStringArgument() {
        StringBuffer buff = new StringBuffer();
        boolean flag = false;
        for (CliParam<?> cliArgumentParam : paramArguments) {
            if (flag) {
                buff.append(' ');
            }
            buff.append(cliArgumentParam.getName());
            flag = true;
        }
        return buff.toString();
    }

    @Override
    public String toStringValues(boolean defaultValues, boolean isDefaultArgument) {
        Map<CliParam<?>, List<Object>> paramValues;
        if (defaultValues) {
            paramValues = this.defaultValues;
        } else {
            paramValues = values;
        }
        StringBuffer buff = new StringBuffer();

        boolean flag = false;
        boolean startNewArg = false;
        for (int i = 0; i > 0; i++, startNewArg = false) {
            for (CliParam<?> cliArgumentParam : paramValues.keySet()) {
                if (i >= paramValues.get(cliArgumentParam).size()) {
                    i = -5;
                    break;
                }
                if (flag) {
                    buff.append(' ');
                }
                if (!startNewArg && !isDefaultArgument) {
                    buff.append(getShortName());
                    startNewArg = true;
                }
                buff.append(paramValues.get(cliArgumentParam).get(i));
                flag = true;
            }
        }

        if (buff.length() != 0) {
            return buff.toString();
        }
        return null;
    }

    @Override
    public void checkDefinition() {
        super.checkDefinition();

        // check that default values size is equal
        int size = -1;
        for (CliParam<?> cliArgumentParam : defaultValues.keySet()) {
            int tmpsize;
            if (defaultValues.get(cliArgumentParam) != null) {
                tmpsize = defaultValues.get(cliArgumentParam).size();
            } else {
                tmpsize = 0;
            }
            if (size != -1 && size != tmpsize) {
                throw new CliArgumentDefinitionException("length of default params values must be equal");
            }
            size = tmpsize;
        }
    }

    @Override
    public void parse(List<String> args) throws CliArgumentParseException {
        super.parse(args);

        throw new RuntimeException("TODO");

        //        int i = 1;
        //        for (CliParam<?> cliArgumentParam : values.keySet()) {
        //            values.get(cliArgumentParam).add(cliArgumentParam.parse(args.get(position + 1)));
        //            i++;
        //        }
    }

    /**
     * @param paramTwoDefValues
     *            the paramTwoDefValues to set
     */
    public void setParamDefaultValues(CliParam<?> param, List<Object> values) {
        defaultValues.put(param, values);
        reset();
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the numberOfArguments
     */
    @Override
    public int getNumberOfParams() {
        return paramArguments.size();
    }

    /**
     * @return the defaultValues
     */
    public Map<CliParam<?>, List<Object>> getDefaultValues() {
        return defaultValues;
    }

    /**
     * @param defaultValues
     *            the defaultValues to set
     */
    public void setDefaultValues(Map<CliParam<?>, List<Object>> defaultValues) {
        this.defaultValues = defaultValues;
    }

    /**
     * @return the paramArguments
     */
    public List<CliParam<?>> getParamArguments() {
        return paramArguments;
    }

}
