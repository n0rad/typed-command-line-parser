package net.awired.aclm.help;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import net.awired.aclm.argument.CliArgumentManager;
import org.junit.rules.ExternalResource;

public class ArgRule<T extends CliArgumentManager> extends ExternalResource {
    public ByteArrayOutputStream outStream;
    public ByteArrayOutputStream errStream;

    public T                     manager;
    private String[]             args = new String[] {};

    public String                out;
    public String                err;
    public boolean               parseSuccess;

    public void runParser() {
        outStream = new ByteArrayOutputStream();
        errStream = new ByteArrayOutputStream();
        manager.setOutputStream(new PrintStream(outStream));
        manager.setErrorStream(new PrintStream(errStream));

        System.out.print("running args :");
        for (String arg : args) {
            System.out.print(" ");
            System.out.print(arg);
        }
        System.out.println();
        parseSuccess = manager.parseWithSuccess(args);

        err = errStream.toString();
        out = outStream.toString();
        System.out.println(out);
        System.err.println(err);
    }

    //////////////////////////////////////////////////////

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public T getManager() {
        return manager;
    }

}
