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
package net.awired.typed.command.line.parser.argument.interfaces;

import java.util.List;
import net.awired.typed.command.line.parser.argument.CliArgumentParseException;

public interface CliArgument extends Comparable<CliArgument> {
    String getShortName();

    List<CliArgument> getNeededArguments();

    List<CliArgument> getForbiddenArguments();

    String getName();

    char getCharName();

    void reset();

    String getDescription();

    boolean isMulticall();

    int getNumCall();

    String toStringArgument();

    boolean isMandatory();

    List<String> getHiddenNames();

    void checkDefinition();

    int getNumberOfParams();

    boolean isSet();

    boolean isHelpHidden();

    boolean isUsageHidden();

    int getMultiCallMax();

    int getMultiCallMin();

    void checkParse(List<CliArgument> arguments) throws CliArgumentParseException;

    void parse(List<String> params) throws CliArgumentParseException;

    void setMultiCallMax(int multiCallMax);

    void setMultiCallMin(int multiCallMin);

}
