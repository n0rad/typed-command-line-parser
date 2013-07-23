package net.awired.typed.command.line.parser.param;

import java.util.UUID;
import net.awired.typed.command.line.parser.argument.CliArgumentParseException;

public class CliParamUuid extends CliParam<UUID> {

    public CliParamUuid(String name) {
        super(name);
    }

    @Override
    public UUID parse(String param) throws CliArgumentParseException {
        try {
            return UUID.fromString(param);
        } catch (Exception e) {
            throw new CliArgumentParseException(param + " is not a valid UUID", e);
        }
    }

}
