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
package fr.norad.typed.command.line.parser.argument.interfaces;

import fr.norad.typed.command.line.parser.argument.CliArgumentManager;
import fr.norad.typed.command.line.parser.argument.CliArgumentParseException;

public interface CliArgumentParser {
    boolean parseWithSuccess(String[] args, CliArgumentManager manager) throws CliArgumentParseException;

    void setDashIsArgumentOnly(boolean dashIsArgumentOnly);

    boolean isDashIsArgumentOnly();

    void setTypeScanLongName(boolean typeScanLongName);

    boolean isTypeScanLongName();

    void setTypeScanShortName(boolean typeScanShortName);

    boolean isTypeScanShortName();

    void setTypeScanShortNameArguments(boolean typeScanShortNameArguments);

    boolean isTypeScanShortNameArguments();

    void setTypeRead(boolean typeRead);

    boolean isTypeRead();
}
