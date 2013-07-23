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
package fr.norad.typed.command.line.parser.param;

import fr.norad.typed.command.line.parser.argument.CliArgumentParseException;

public class CliParamEnum<E extends Enum<E>> extends CliParam<E> {

    private Class<E> theEnum;

    public CliParamEnum(String name, Class<E> theEnum) {
        super(name);
        this.theEnum = theEnum;
    }

    @Override
    public E parse(String param) throws CliArgumentParseException {
        for (E e : theEnum.getEnumConstants()) {
            if (e.toString().equals(param)) {
                return e;
            }
        }
        throw new CliArgumentParseException(param + " is not a valid argument");
    }

    @Override
    public String getParamDescription() {
        StringBuffer buffer = new StringBuffer();
        if (super.getParamDescription() != null) {
            buffer.append(super.getParamDescription());
            buffer.append(' ');
        }
        boolean flag = false;
        for (E e : theEnum.getEnumConstants()) {
            if (flag) {
                buffer.append(" | ");
            }
            buffer.append(e.toString());
            flag = true;
        }
        buffer.toString();
        return buffer.toString();
    }

}
