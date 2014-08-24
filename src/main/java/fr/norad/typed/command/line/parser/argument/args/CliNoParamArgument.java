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
/**
 * $Id:$
 *
 * This file is part of Business Intranet Collaboratif (BIC).
 *
 * Labo NTIC Production 2008
 * 
 * @author renaudinx
 * @creation 23 oct. 2008
 * @version 1.0
 */
package fr.norad.typed.command.line.parser.argument.args;

import java.util.ArrayList;
import java.util.List;
import fr.norad.typed.command.line.parser.argument.CliArgumentDefinitionException;
import fr.norad.typed.command.line.parser.argument.CliArgumentManager;
import fr.norad.typed.command.line.parser.argument.CliArgumentParseException;
import fr.norad.typed.command.line.parser.argument.interfaces.CliArgument;

/**
 * {@link CliNoParamArgument} represent an argument with no param in the comment line manager
 * {@link CliArgumentManager}.
 * 
 * A good example of a no paramed argument will be '-v' used for to put the application in verbose mode.
 * This class is used also as an abstract class for paramed arguments which will also need same informations like
 * constraint and description. this class can not be used for default argument as default arguments need at least one
 * param.
 * 
 * The manager need a shortName for each argument and it can only exist once per manager this meen you can only use
 * a-z, A-Z and 0-9,
 * and then can only have 62 arguments for a CLI interpreter (hope its enough).
 * 
 * The description and constraints of the argument is used to generate the helper (-h) so set it carfully.
 * 
 * TODO on setname check spcial chars
 * 
 * @author n0rad
 */
public class CliNoParamArgument implements CliArgument {

    /** Number of params for this argument. */
    private static final int NUMBER_OF_PARAMS = 0;
    /** List of hidden names. hidden in helper. */
    private List<String> hiddenNames = new ArrayList<String>();
    /** Name of the argument example : --verbose. */
    private String name;
    /** Forbidden argument for this argument (can not be set as the same time). */
    private final List<CliArgument> forbiddenArguments = new ArrayList<CliArgument>();
    /** Needed arguments for this argument (need to be set as the same time). */
    private final List<CliArgument> neededArguments = new ArrayList<CliArgument>();
    /** Short name of this argument example : -v. */
    private final String shortName;
    /** The unique char representing this argument example : v. */
    private final char charName;
    /** A description for this argument. */
    private String description;
    /** Argument should be hidden in usage ? */
    private boolean usageHidden = false;
    /** Argument should be hidden in helper ? */
    private boolean helpHidden = false;
    /** True if argument is always mandatory. */
    private boolean mandatory = false;
    /** Minimum number of times this argument can be set in the CLI example : -v -v -v. */
    private int multiCallMin = 1;
    /** Maximum number of times this argument can be set in the CLI. */
    private int multiCallMax = 1;

    /** Number of times this argument was set during parsing. */
    protected int numCall = 0;

    // //////////////////////////////////////////////////////////////////////

    /**
     * Argument comparator is used to sort element to be display in order in helper.
     * 
     * Sort is done in ascii order with uppercase just after lowercase of same character.
     * 
     * @param other
     * @return the comparison result
     */
    @Override
    public int compareTo(CliArgument other) {
        // equal
        if (getCharName() == other.getCharName()) {
            return 0;
        }

        char me = getCharName();
        char otherChar = other.getCharName();
        if (me >= 'A' && me <= 'Z') {
            me = (char) (('a' - 'A') + me);
        }
        if (otherChar >= 'A' && otherChar <= 'Z') {
            otherChar = (char) (('a' - 'A') + otherChar);
        }

        int res = me - otherChar;
        if (res == 0) {
            if (other.getCharName() >= 'A' && other.getCharName() <= 'Z') {
                return -1;
            }
            if (getCharName() >= 'A' && getCharName() <= 'Z') {
                return 1;
            }
        }

        return res;
    }

    /**
     * Construction of an argument with the character representing this argument.
     * 
     * @param shortName
     *            the character representing this argument
     */
    public CliNoParamArgument(char shortName) {
        if (!((shortName >= 'A' && shortName <= 'Z') || (shortName >= 'a' && shortName <= 'z') || (shortName >= '0' && shortName <= '9'))) {
            throw new CliArgumentDefinitionException("bad short argument name : " + shortName);
        }
        this.charName = shortName;
        this.shortName = new String(new char[] { '-', shortName });
    }

    /**
     * True if multicall contraints tells this argument can be call more than 1 time.
     * 
     * @return true if this argument can be call more than 1 time
     */
    @Override
    public boolean isMulticall() {
        return getMultiCallMax() > 1 || getMultiCallMax() == 0;
    }

    /**
     * Add an hidden name for this argument. You need to set the full path to this argument hidden name not like the
     * name.
     * '--' will be automatically added to the name but not for hidden names, this is allowed to be able to create
     * windows like names for example : /? /H etc...
     * 
     * @param name
     *            the argument name
     */
    public void addHiddenName(String name) {
        this.hiddenNames.add(name);
    }

    /**
     * Reset the current parse result stored in this argument.
     */
    @Override
    public void reset() {
        numCall = 0;
    }

    public String toStringValues(boolean defaultValues, boolean isDefaultArgument) {
        if (!defaultValues) {
            StringBuffer b = new StringBuffer();
            for (int i = 0; i < numCall; i++) {
                if (i != 0) {
                    b.append(' ');
                }
                b.append(getShortName());
            }
            if (b.length() != 0) {
                return b.toString();
            }
        }
        return null;
    }

    @Override
    public void checkParse(List<CliArgument> arguments) throws CliArgumentParseException {

        // check mandatroy
        if (mandatory && numCall == 0) {
            throw new CliArgumentParseException("argument is mandatory", this);
        }

        // check multicall
        if (numCall > 0 && numCall < multiCallMin) {
            throw new CliArgumentParseException("argument must be called minimum " + multiCallMin + " times", this);
        }

    }

    @Override
    public void parse(List<String> args) throws CliArgumentParseException {
        numCall++;

        if (multiCallMax != 0 && numCall > 0 && numCall > multiCallMax) {
            throw new CliArgumentParseException("argument must be called maximum " + multiCallMax + " times", this);
        }
    }

    @Override
    public void checkDefinition() {
        if (multiCallMin < 1) {
            throw new CliArgumentDefinitionException("multicall minimum can not be less than 1");
        }
        if (multiCallMax < 0) {
            throw new CliArgumentDefinitionException("multicall maximum can not be less than 0");
        }
        if (multiCallMax != 0 && multiCallMin > multiCallMax) {
            throw new CliArgumentDefinitionException("multicall maximum can not be less than minimum");
        }
    }

    public void addForbiddenArgument(CliArgument argument) {
        if (argument == null) {
            throw new NullPointerException("Forbidden argument must exists");
        }
        if (argument == this) {
            throw new CliArgumentDefinitionException("argument can not contain himself as forbidden");
        }
        forbiddenArguments.add(argument);
    }

    public void addNeededArgument(CliArgument argument) {
        if (argument == null) {
            throw new NullPointerException("Needed argument must exists");
        }
        if (argument == this) {
            throw new CliArgumentDefinitionException("argument can not contain himself as needed");
        }
        neededArguments.add(argument);
    }

    @Override
    public String toStringArgument() {
        return null;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        if (!mandatory) {
            s.append("[ ");
        }
        s.append(shortName);

        String res = toStringArgument();
        if (res != null && !res.isEmpty()) {
            s.append(' ');
            s.append(res);
        }
        if (isMulticall()) {
            s.append(" ...");
        }
        if (!mandatory) {
            s.append(" ]");
        }
        return s.toString();
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        if (name.length() > 2 && name.charAt(0) == '-') {
            if (name.charAt(1) == '-') {
                this.name = name;
            } else {
                this.name = "-" + name;
            }
        } else if (name.charAt(0) == '/') {
            this.name = name;
        } else {
            this.name = "--" + name;
        }
    }

    @Override
    public boolean isSet() {
        return !(numCall == 0);
    }

    public void setMulticall(int minAndMax) {
        multiCallMin = minAndMax;
        multiCallMax = minAndMax;
    }

    // //////////////////////////////////////////////////////////////////////

    @Override
    public String getDescription() {
        return this.description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getShortName() {
        return this.shortName;
    }

    @Override
    public boolean isUsageHidden() {
        return this.usageHidden;
    }

    public void setUsageHidden(boolean usageHidden) {
        this.usageHidden = usageHidden;
    }

    @Override
    public boolean isHelpHidden() {
        return this.helpHidden;
    }

    public void setHelpHidden(boolean helpHidden) {
        this.helpHidden = helpHidden;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public List<CliArgument> getForbiddenArguments() {
        return forbiddenArguments;
    }

    @Override
    public List<CliArgument> getNeededArguments() {
        return neededArguments;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the hiddenNames
     */
    @Override
    public List<String> getHiddenNames() {
        return hiddenNames;
    }

    /**
     * @param hiddenNames
     *            the hiddenNames to set
     */
    public void setHiddenNames(List<String> hiddenNames) {
        this.hiddenNames = hiddenNames;
    }

    /**
     * @return the multiCallMin
     */
    @Override
    public int getMultiCallMin() {
        return multiCallMin;
    }

    /**
     * @param multiCallMin
     *            the multiCallMin to set
     */
    @Override
    public void setMultiCallMin(int multiCallMin) {
        this.multiCallMin = multiCallMin;
    }

    /**
     * @return the multiCallMax
     */
    @Override
    public int getMultiCallMax() {
        return multiCallMax;
    }

    /**
     * Set max multicall value, 0 is unlimited.
     * 
     * @param multiCallMax
     *            the multiCallMax to set
     */
    @Override
    public void setMultiCallMax(int multiCallMax) {
        this.multiCallMax = multiCallMax;
    }

    /**
     * @return the numCall
     */
    @Override
    public int getNumCall() {
        return numCall;
    }

    /**
     * @return the numberOfArguments
     */
    @Override
    public int getNumberOfParams() {
        return NUMBER_OF_PARAMS;
    }

    /**
     * @return the charName
     */
    @Override
    public char getCharName() {
        return charName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + charName;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CliNoParamArgument other = (CliNoParamArgument) obj;
        if (charName != other.charName) {
            return false;
        }
        return true;
    }

}
