package net.awired.aclm.help;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Permission;
import net.awired.aclm.argument.CliArgumentManager;
import org.junit.rules.ExternalResource;

public class ArgRule extends ExternalResource {
    private boolean              parseInProgress = false;
    public ByteArrayOutputStream outStream;
    public ByteArrayOutputStream errStream;

    public CliArgumentManager    manager;
    private String[]             args            = new String[] {};

    public String                out;
    public String                err;
    public boolean               exit;

    public void runParser() {
        outStream = new ByteArrayOutputStream();
        errStream = new ByteArrayOutputStream();
        manager.setOutputStream(new PrintStream(outStream));
        manager.setErrorStream(new PrintStream(errStream));

        try {
            parseInProgress = true;
            System.out.print("running args :");
            for (int i = 0; i < args.length; i++) {
                System.out.print(" ");
                System.out.print(args[i]);
            }
            System.out.println();
            manager.parse(args);
        } catch (MyExitException e) {
            exit = true;
        } finally {
            parseInProgress = false;
        }

        err = errStream.toString();
        out = outStream.toString();
        System.out.println(out);
        System.err.println(err);
    }

    @Override
    protected void before() throws Throwable {
        SecurityManager securityManager = new SecurityManager() {
            public void checkPermission(Permission permission) {
                if (parseInProgress && permission instanceof RuntimePermission
                        && permission.getName().startsWith("exitVM")) {
                    throw new MyExitException();
                }
            }
        };
        System.setSecurityManager(securityManager);
    }

    @Override
    protected void after() {
        this.parseInProgress = false;
        System.setSecurityManager(null);
    }

    //////////////////////////////////////////////////////

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public CliArgumentManager getManager() {
        return manager;
    }

}
