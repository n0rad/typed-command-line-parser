package net.awired.aclm.argument.definition;
//package net.awired.ajsl.cli.argument.definition;
//
//import net.awired.ajsl.cli.argument.CliArgumentDefinitionException;
//import net.awired.ajsl.cli.argument.CliArgumentManager;
//import net.awired.ajsl.cli.argument.args.CliNoParamArgument;
//
//public enum ArgumentTestParseEnum implements CliArgumentTestIface<CliArgumentManager> {
//
//    SS(new String[] { "-s", "-d" }) {
//        @Override
//        public CliArgumentManager getManager() {
//            return new CliArgumentManager("argumentTestParse") {
//
//                private final CliNoParamArgument arg1;
//                private final CliNoParamArgument arg2;
//
//                {
//                    arg1 = new CliNoParamArgument('s');
//                    addArg(arg1);
//
//                    arg2 = new CliNoParamArgument('d');
//                    addArg(arg2);
//
//                    arg1.addForbiddenArgument(arg2);
//                }
//            };
//        }
//    }
//
//    ;
//
//    private final String                         err;
//    private final String                         out;
//    private final String[]                       args;
//    private final CliArgumentDefinitionException exception;
//
//    private ArgumentTestParseEnum(String[] args) {
//        this.args = args;
//        this.out = null;
//        this.err = null;
//        this.exception = null;
//    }
//
//    @Override
//    public void postTest() {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void preTest() {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void checkResult(CliArgumentManager argManager, boolean exit) {
//        //TODO it
//        //      throw new RuntimeException("not implemented yet");
//    }
//
//    @Override
//    public void checkResult(CliArgumentManager argManager, boolean exit, String exception) {
//        //TODO it
//        //        throw new RuntimeException("not implemented yet");
//    }
//
//    ////////////////////////////////////////////////////////
//
//    /**
//     * @return the err
//     */
//    public String getErr() {
//        return err;
//    }
//
//    /**
//     * @return the out
//     */
//    public String getOut() {
//        return out;
//    }
//
//    /**
//     * @return the args
//     */
//    public String[] getArgs() {
//        return args;
//    }
//
//    /**
//     * @return the exception
//     */
//    public CliArgumentDefinitionException getException() {
//        return exception;
//    }
//}
