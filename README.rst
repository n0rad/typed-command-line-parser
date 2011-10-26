Awired Command Line Manager
===========================

ACLM is a Command Line Interface management library, where the main functionnality is a argument parser.

Functionalities provided by the parser are :

* fully typed arguments (no casts : less builds to tests, better code, easy to use in IDE)
* generated helper using informations defined in your manager
* manage argument with multiple params : ``./app -s localhost 8080 -s there 8081``
* manage long names : ``./app --server localhost 8080``
* allow read arguments to check if its always values ex : read -f to know if its a param of -r or a new argument ``./toto42 -r 1 a 2 b -f 3``
* mutiple same arguement : ``./app -p 8080 -p 8081``
* smart params like file : ``./app -f file.txt`` (will fail in cli if file.txt does not exists)
* smart params like enum : ``./app -v debug`` (will fail if the param of '-v' is an enum and debug does not exists in enum)
* scan for short names : ``./app -vf 3``
* scan for single param after short name list : ``./app -vpf 8080`` (means ``-v -f -p 8080``)
* scan for short names arguments : ``./app -r 1 a -f3``
* scan long names : ``./app -r 1 a --file=3``
* show you where is your error in the cli

::

 enumArgumentTest: 127.0.0.1 is not a valid Integer -- [ -p port ]
 enumArgumentTest -lpa 127.0.0.1 43
 ________________________^
 May be the root cause : 
     a is not a valid Integer -- [ -p port ]
       enumArgumentTest -lpa 127.0.0.1 43
 __________________________^
 Usage: enumArgumentTest [ -amvlps ] [ transactions num ]
 Try `enumArgumentTest --help' for more information.

* manager, parser, helper, arguments and params are extensible to match your needs
* more I may not remember :) 
                

How the command line is composed in the argument manager

::

 # cut -d ' ' -f 2 file.txt
   ------------------------ the argument manager scope
       ------               Argument 'd' that is linked to a param (' ') 
              ----          Argument 'f' that is linked to a param (2-3)
                     ------ Default argument of the arguement manager (this mean that this param is not link to a argument)
          ---               Param of 'd' argument
                 -          Param of 'f' argument

For this command you will define the current manager like that in ACLM :

::

 public class CutArgumentManager extends CliArgumentManager {

    public final CliOneParamArgument<Integer>   fieldArgument;
    public final CliOneParamArgument<Character> delimiterArgument;
    public final CliOneParamArgument<File>      fileArgument;

    public CutArgumentManager() {
        super("cut");
        fieldArgument = new CliOneParamArgument<Integer>('f', new CliParamInt("field"));
        delimiterArgument = new CliOneParamArgument<Character>('d', new CliParamChar("delimiter"));
        delimiterArgument.setParamOneDefValue('\t');
        CliParamFile paramOneArgument = new CliParamFile("file");
        paramOneArgument.setIsFile(true);
        fileArgument = new CliOneParamArgument<File>('0', paramOneArgument);
        addArg(fieldArgument);
        addArg(delimiterArgument);
        setDefaultArgument(fileArgument);
    }
 }

and you will use it like this : 

::

 public static void main(String[] args) {
    args = new String[] { "-d", " ", "-f", "2", "file.txt" };
    CutArgumentManager cutArgumentManager = new CutArgumentManager();
    cutArgumentManager.parse(args);

    Integer field = cutArgumentManager.fieldArgument.getParamOneValue();
    Character delimiter = cutArgumentManager.delimiterArgument.getParamOneValue();
    File file = cutArgumentManager.fileArgument.getParamOneValue();        
 }
