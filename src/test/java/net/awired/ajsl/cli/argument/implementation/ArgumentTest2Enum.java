package net.awired.ajsl.cli.argument.implementation;


public enum ArgumentTest2Enum implements CliArgumentTestIface<ArgumentTestManager> {

    MULTICALL(new String[] { "-l", "-l", "-l", "-l" }, null, null) {
        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit) {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkResult(ArgumentTestManager argManager, boolean exit, String exception) {
            // TODO Auto-generated method stub

        }
    },

    ;

    @Override
    public void preTest() {
    }

    @Override
    public void postTest() {

    }

    private final String   err;
    private final String   out;
    private final String[] args;

    private ArgumentTest2Enum(String[] args, String err, String out) {
        this.args = args;
        this.out = out;
        this.err = err;
    }

    public ArgumentTestManager getManager() {
        return new ArgumentTestManager();
    }

    /**
     * @return the args
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * @return the err
     */
    public String getErr() {
        return err;
    }

    /**
     * @return the out
     */
    public String getOut() {
        return out;
    }

}
