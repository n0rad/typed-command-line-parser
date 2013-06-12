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

import java.io.File;
import net.awired.aclm.argument.CliArgumentParseException;

public class CliParamFile extends CliParam<File> {

    public CliParamFile(String name) {
        super(name);
    }

    private boolean canRead;
    private boolean canWrite;
    private boolean canExecute;

    private boolean isDirectory;
    private boolean isFile;
    private boolean isHidden;

    @Override
    public File parse(String param) throws CliArgumentParseException {
        // TODO: manage securityexceptions
        File f = new File(param);
        if (canRead && !f.canRead()) {
            throw new CliArgumentParseException(param + " can not be read");
        }
        if (canWrite && !f.canWrite()) {
            throw new CliArgumentParseException(param + " can not be written");
        }
        if (canExecute && !f.canExecute()) {
            throw new CliArgumentParseException(param + " can not be executed");
        }

        if (isDirectory && !f.isDirectory()) {
            throw new CliArgumentParseException(param + " is not a directory");
        }
        if (isFile && !f.isFile()) {
            throw new CliArgumentParseException(param + " is not a file");
        }
        if (isHidden && !f.isHidden()) {
            throw new CliArgumentParseException(param + " is not hidden");
        }
        return f;
    }

    /**
     * @return the canRead
     */
    public Boolean getCanRead() {
        return canRead;
    }

    /**
     * @param canRead
     *            the canRead to set
     */
    public CliParamFile setCanRead(Boolean canRead) {
        this.canRead = canRead;
        return this;
    }

    /**
     * @return the canWrite
     */
    public Boolean getCanWrite() {
        return canWrite;
    }

    /**
     * @param canWrite
     *            the canWrite to set
     */
    public CliParamFile setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
        return this;
    }

    /**
     * @return the canExecute
     */
    public Boolean getCanExecute() {
        return canExecute;
    }

    /**
     * @param canExecute
     *            the canExecute to set
     */
    public CliParamFile setCanExecute(Boolean canExecute) {
        this.canExecute = canExecute;
        return this;
    }

    /**
     * @return the isDirectory
     */
    public Boolean getIsDirectory() {
        return isDirectory;
    }

    /**
     * @param isDirectory
     *            the isDirectory to set
     */
    public CliParamFile setIsDirectory(Boolean isDirectory) {
        this.isDirectory = isDirectory;
        return this;
    }

    /**
     * @return the isFile
     */
    public Boolean getIsFile() {
        return isFile;
    }

    /**
     * @param isFile
     *            the isFile to set
     */
    public CliParamFile setIsFile(Boolean isFile) {
        this.isFile = isFile;
        return this;
    }

    /**
     * @return the isHidden
     */
    public Boolean getIsHidden() {
        return isHidden;
    }

    /**
     * @param isHidden
     *            the isHidden to set
     */
    public CliParamFile setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
        return this;
    }

}
