package net.awired.ajsl.cli.param;

public class CliParamString extends CliParam<String> {

    public CliParamString(String name) {
        super(name);
    }

    @Override
    public String parse(String param) {
        return param;
    }

}
