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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.awired.aclm.argument.CliArgumentParseException;

public class CliParamDate extends CliParam<Date> {

    private SimpleDateFormat simpleDateFormat;
    private final String     format;

    public CliParamDate(String name, String format) {
        super(name);
        this.format = format;
        simpleDateFormat = new SimpleDateFormat(format);
    }

    @Override
    public String getParamDescription() {
        String res = "";
        if (super.getParamDescription() != null) {
            res = super.getParamDescription();
        }
        return res + " format: " + format;
    }

    @Override
    public Date parse(String param) throws CliArgumentParseException {
        try {
            return simpleDateFormat.parse(param);
        } catch (ParseException e) {
            throw new CliArgumentParseException("unparsable date", e);
        }
    }
}
