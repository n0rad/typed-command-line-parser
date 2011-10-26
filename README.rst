Awired Command Line Manager
===========================

ACLM is a Command Line Interface management library, where the main functionnality is a argument parser.

Functionalities
---------------

* fully typed arguments (no casts : less builds to tests, better code, easy to use in IDE)
* generated helper using informations defined in your manager
* manage argument with multiple params : **./app -s localhost 8080 -s there 8081**
* manage long names : **./app --server localhost 8080**
* allow read arguments to check if its always values ex : read -f to know if its a param of -r or a new argument **./app -r 1 a 2 b -f 3**
* mutiple same arguement : **./app -p 8080 -p 8081**
* smart params like file : **./app -f file.txt** (will fail in cli if file.txt does not exists)
* smart params like enum : **./app -v debug** (will fail if the param of '-v' is an enum and debug does not exists in enum)
* scan for short names : **./app -vf 3**
* scan for single param after short name list : **./app -vpf 8080** (means **-v -f -p 8080**)
* scan for short names arguments : **./app -r 1 a -f3**
* scan long names : **./app -r 1 a --file=3**
* show you where is your error in the cli

::

 enumArgumentTest: 127.0.0.1 is not a valid Integer -- [ -p port ]
 enumArgumentTest -lpa 127.0.0.1 43
 ______________________^
 May be the root cause : 
     a is not a valid Integer -- [ -p port ]
       enumArgumentTest -lpa 127.0.0.1 43
 __________________________^
 Usage: enumArgumentTest [ -amvlps ] [ transactions num ]
 Try `enumArgumentTest --help' for more information.

* manager, parser, helper, arguments and params are extensible to match your needs
* more I may not remember :) 

Composition
-----------

How the command line is composed in the argument manager

::

 # ./myApp -v -p 8081 file.txt
   --------------------------- The argument manager scope
           --                  Argument 'v' with no param 
              -------          Argument 'p' that is linked to a param (8081)
                      -------- Default argument (no prefix letter, direct param)
                 ----          Param of 'p' argument

Usage Example
-------------

For this command you will define a manager like that in ACLM :

::

    public class MyAppArgumentManager extends CliArgumentManager {

        public final CliNoParamArgument           verboseArgument;
        public final CliOneParamArgument<Integer> portArgument;
        public final CliOneParamArgument<File>    fileArgument;

        public MyAppArgumentManager() {
            super("myapp");

            verboseArgument = new CliNoParamArgument('v');
            verboseArgument.setDescription("put application in verbose mode");
            addArg(verboseArgument);

            CliParamInt portParam = new CliParamInt("port");
            portParam.setNegativable(false);
            portParam.setZeroable(false);
            portParam.setParamDescription("server port number");
            portArgument = new CliOneParamArgument<Integer>('p', portParam);
            portArgument.setParamOneDefValue(8080);
            addArg(portArgument);

            CliParamFile paramOneArgument = new CliParamFile("file");
            paramOneArgument.setIsFile(true);
            fileArgument = new CliOneParamArgument<File>('0', paramOneArgument);
            setDefaultArgument(fileArgument);
        }
    }

You will use it like this : 

::

 public static void main(String[] args) {
        String[] args = new String[] { "-v", "-p", "8081", "file.txt" };
        MyAppArgumentManager cutArgumentManager = new MyAppArgumentManager();
        cutArgumentManager.parse(args);

        Integer port = cutArgumentManager.portArgument.getParamOneValue();
        boolean verbose = cutArgumentManager.verboseArgument.isSet();
        File file = cutArgumentManager.fileArgument.getParamOneValue();       
 }

And it will generate this helper if you call ./myapp -h :

::

 Usage: myapp [ -v ][ -p port ] [ file ]
  -h, --help               This helper
  -p=port                  port                : server port number
                           Default Value       : -p 8080
  -v                       put application in verbose mode

