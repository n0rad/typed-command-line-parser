package net.awired.aclm.param;

import net.awired.aclm.argument.CliArgumentParseException;

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
