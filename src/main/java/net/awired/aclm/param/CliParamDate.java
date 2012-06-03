package net.awired.aclm.param;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.awired.aclm.argument.CliArgumentParseException;

public class CliParamDate extends CliParam<Date> {

    private SimpleDateFormat simpleDateFormat;
    private final String     format;

    private CliParamDate(String name, String format) {
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
