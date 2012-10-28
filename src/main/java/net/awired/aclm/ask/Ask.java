package net.awired.aclm.ask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import net.awired.aclm.argument.CliArgumentParseException;
import net.awired.aclm.param.CliParam;

public class Ask<PARAM> {

    private final String          question;
    private final CliParam<PARAM> param;
    private InputStream           in      = System.in;
    private OutputStream          out     = System.out;
    private PARAM                 value;
    private PARAM                 defaultValue;
    private boolean               askOnce = false;

    public Ask(String question, CliParam<PARAM> param) {
        this.param = param;
        this.question = question;
    }

    public static <PARAM> PARAM run(String question, CliParam<PARAM> param) {
        Ask<PARAM> ask = new Ask<PARAM>(question, param);
        ask.run();
        return ask.getValue();
    }

    public void showQuestion() {
        try {
            out.write(question.getBytes());
            if (param.getParamDescription() != null) {
                out.write(" (".getBytes());
                out.write(param.getParamDescription().getBytes());
                out.write(") ".getBytes());
            }
            out.write("? ".getBytes());
            if (defaultValue != null) {
                out.write('[');
                out.write(defaultValue.toString().getBytes());
                out.write("] ".getBytes());
            }

            // out.write(System.getProperty("line.separator").getBytes());
        } catch (IOException e) {
            throw new RuntimeException("can not write to output");
        }
    }

    public void run() {
        InputStreamReader converter = new InputStreamReader(in);
        BufferedReader in = new BufferedReader(converter);
        String res;
        try {
            boolean flag = true;
            while (flag) {
                showQuestion();
                res = in.readLine();
                if (res == null) {
                    return;
                }
                res = res.trim();
                if (res.isEmpty() && defaultValue != null) {
                    value = defaultValue;
                    return;
                }
                try {
                    value = param.parse(res);
                    return;
                } catch (CliArgumentParseException e) {
                    out.write(e.getMessage().getBytes());
                    out.write(System.getProperty("line.separator").getBytes());
                }

                // loop on ask to have a response ?
                if (askOnce) {
                    flag = false;
                }
            }
        } catch (IOException e) {
            // TODO:
            e.printStackTrace();
        }
    }

    // public boolean isCaseSensitive() {
    // return caseSensitive;
    // }
    //
    // public void setCaseSensitive(boolean caseSensitive) {
    // this.caseSensitive = caseSensitive;
    // }

    // /////////////////////////////////////////////////////////////////////////

    /**
     * @return the in
     */
    public InputStream getIn() {
        return in;
    }

    /**
     * @param in
     *            the in to set
     */
    public void setIn(InputStream in) {
        this.in = in;
    }

    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @return the param
     */
    public CliParam<PARAM> getParam() {
        return param;
    }

    /**
     * @return the out
     */
    public OutputStream getOut() {
        return out;
    }

    /**
     * @param out
     *            the out to set
     */
    public void setOut(OutputStream out) {
        this.out = out;
    }

    /**
     * @return the defaultValue
     */
    public PARAM getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue
     *            the defaultValue to set
     */
    public void setDefaultValue(PARAM defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the value
     */
    public PARAM getValue() {
        return value;
    }

    /**
     * @return the askOnce
     */
    public boolean isAskOnce() {
        return askOnce;
    }

    /**
     * @param askOnce
     *            the askOnce to set
     */
    public void setAskOnce(boolean askOnce) {
        this.askOnce = askOnce;
    }

}
