package net.awired.aclm.param;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.awired.aclm.argument.CliArgumentParseException;

public class CliParamInetAddress extends CliParam<InetAddress> {

    public CliParamInetAddress(String name) {
        super(name);
    }

    @Override
    public InetAddress parse(String param) throws CliArgumentParseException {
        try {
            return InetAddress.getByName(param);
        } catch (UnknownHostException e) {
            throw new CliArgumentParseException(e.getMessage());

        }
    }

}
