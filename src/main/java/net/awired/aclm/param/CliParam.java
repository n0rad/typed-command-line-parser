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
package net.awired.aclm.param;

import net.awired.aclm.argument.CliArgumentParseException;

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
