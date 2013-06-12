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
package net.awired.typed.command.line.parser.param;

import net.awired.typed.command.line.parser.argument.CliArgumentParseException;

public class CliParamInt extends CliParam<Integer> {

    private boolean zeroable = true;

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
