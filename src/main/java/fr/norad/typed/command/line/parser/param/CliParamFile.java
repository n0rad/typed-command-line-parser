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

import java.io.File;
import fr.norad.typed.command.line.parser.argument.CliArgumentParseException;

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

    public Boolean getCanRead() {
        return canRead;
    }

    public CliParamFile setCanRead(Boolean canRead) {
        this.canRead = canRead;
        return this;
    }

    public Boolean getCanWrite() {
        return canWrite;
    }

    public CliParamFile setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
        return this;
    }

    public Boolean getCanExecute() {
        return canExecute;
    }

    public CliParamFile setCanExecute(Boolean canExecute) {
        this.canExecute = canExecute;
        return this;
    }

    public Boolean getIsDirectory() {
        return isDirectory;
    }

    public CliParamFile setIsDirectory(Boolean isDirectory) {
        this.isDirectory = isDirectory;
        return this;
    }

    public Boolean getIsFile() {
        return isFile;
    }

    public CliParamFile setIsFile(Boolean isFile) {
        this.isFile = isFile;
        return this;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public CliParamFile setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
        return this;
    }

}
