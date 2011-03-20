package net.awired.ajsl.cli.argument.implementation;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import net.awired.ajsl.cli.argument.CliArgumentDefinitionException;
import net.awired.ajsl.cli.argument.CliArgumentManager;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CliArgumentManagerTest {

    public static final String                                                DATA_NAME       = "data_name";

    @SuppressWarnings("unchecked")
    private final Class<CliArgumentTestIface<? extends CliArgumentManager>>[] TEST_CLASSES    = new Class[] {
                                                                                              //ArgumentTestParseEnum.class,
            ArgumentTestDefinitionEnum.class, ArgumentTestEnum.class                         };

    /**
     * We need a var to know if we have a parse in progress to catch only exit from argument manager and not the junit
     * process.
     */
    private boolean                                                           parseInProgress = false;

    public class MyExitException extends SecurityException {
        private static final long serialVersionUID = 1L;
    }

    //////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    @DataProvider(name = DATA_NAME)
    public CliArgumentTestIface<CliArgumentManager>[][] createData(Method m) {

        List<CliArgumentTestIface<? extends CliArgumentManager>> res = new ArrayList<CliArgumentTestIface<? extends CliArgumentManager>>();

        for (Class<CliArgumentTestIface<? extends CliArgumentManager>> clazz : TEST_CLASSES) {
            CliArgumentTestIface<? extends CliArgumentManager>[] enumElement = clazz.getEnumConstants();
            for (CliArgumentTestIface<? extends CliArgumentManager> cliArgumentTestIface : enumElement) {
                res.add(cliArgumentTestIface);
            }
        }

        int i = 0;
        CliArgumentTestIface<CliArgumentManager>[][] realres = new CliArgumentTestIface[res.size()][];
        for (CliArgumentTestIface<? extends CliArgumentManager> cliArgumentTestIface : res) {
            realres[i++] = new CliArgumentTestIface[] { cliArgumentTestIface };
        }
        return realres;
    }

    @Test
    public void checkSingle() {
        CliArgumentTestIface<CliArgumentManager> enumargtest = ArgumentTestDefinitionEnum.CIRCULAR_FORBIDDEN1;
        check(enumargtest);
    }

    //  @Test(dataProvider = DATA_NAME)
    public void check(CliArgumentTestIface<CliArgumentManager> enumargtest) {
        CliArgumentManager manager = enumargtest.getManager();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        manager.setOutputStream(new PrintStream(outContent));
        manager.setErrorStream(new PrintStream(errContent));

        boolean exit = false;
        CliArgumentDefinitionException exception = null;
        enumargtest.preTest();
        try {
            parseInProgress = true;
            System.out.print("running " + enumargtest + " :");
            for (int i = 0; i < enumargtest.getArgs().length; i++) {
                System.out.print(" ");
                System.out.print(enumargtest.getArgs()[i]);
            }
            System.out.println();
            manager.parse(enumargtest.getArgs());
        } catch (MyExitException e) {
            exit = true;
        } catch (CliArgumentDefinitionException e) {
            exception = e;
        } finally {
            parseInProgress = false;
        }
        enumargtest.postTest();

        String error = errContent.toString();
        String output = outContent.toString();
        System.out.println(output);
        System.err.println(error);

        if (exception != null) {
            enumargtest.checkResult(manager, exit, exception.toString());
        } else {
            enumargtest.checkResult(manager, exit);
        }

        Assert.assertEquals(enumargtest.getErr(), error.isEmpty() ? null : error);
        Assert.assertEquals(enumargtest.getOut(), output.isEmpty() ? null : output);
    }

    @BeforeTest
    public void beforeTest() {
        SecurityManager securityManager = new SecurityManager() {
            public void checkPermission(Permission permission) {
                if (parseInProgress && permission instanceof RuntimePermission
                        && permission.getName().startsWith("exitVM")) {
                    throw new MyExitException();
                }
            }
        };
        System.setSecurityManager(securityManager);

        //        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        //
        //        help.setOutput(new PrintStream(outContent));
        //        help.setOutput(new PrintStream(errContent));

    }

    @AfterTest
    public void afterTest() {
        System.setSecurityManager(null);

        System.setErr(null);
        System.setOut(null);
    }

}
