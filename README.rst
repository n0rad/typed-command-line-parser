Awired Command Line Manager
===========================

ACLM is a Command Line Interface management library, where the main functionnality is a argument parser.

Functionalities
===============

* fully typed arguments (no casts : less builds to tests, better code, easy to use in IDE)
* generated helper using informations defined in your manager
* manage argument with multiple params : **./app -s localhost 8080 -s there 8081**
* manage long names : **./app --server localhost 8080**
* allow read arguments to check if its always values ex : read -f to know if its a param of -r or a new argument **./app -r 1 a 2 b -f 3**
* mutiple same argument : **./app -p 8080 -p 8081**
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
===========

How the command line is composed in the argument manager

::

 # ./myApp -v -p 8081 file.txt
   --------------------------- The argument manager scope
           --                  Argument 'v' with no param 
              -------          Argument 'p' that is linked to a param (8081)
                      -------- Default argument (no prefix letter, direct param)
                 ----          Param of 'p' argument

Simple Usage Example
====================

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


Advanced Definition
===================

This part describe how you can configure the Argument manager to match your needs.

Params
------

Available params
 All params included in package start with CliParam*
 Some may have methods to increase check when parse for exemple :
 ``CliParamInt.setZeroable(Boolean);``
 ``CliParamInt.setNegativable(Boolean);``
 ``CliParamFile.setIsDirectory(Boolean);``
 ``CliParamFile.setIsFile(Boolean);``
 ``CliParamFile.setIsHidden(Boolean);``
 ``CliParamFile.setCanExecute(Boolean);``
 ``CliParamFile.setCanRead(Boolean);``
 ``CliParamFile.setCanWrite(Boolean);``

Name
 Params are created with a name in constructor,
 this name is used in the usage : ./myapp [ -v ][ -p **port** ] [ **file** ]
 and also in the helper : 
  -h, --help               This helper
  -p=**port**                  port                : server port number
                           Default Value       : -p 8080
  -v                       put application in verbose mode

Description
 You can add a description to your param with ``param.setDescription(String);``
 this description is used in the helper to describe the param :
  -h, --help               This helper
  -p=port                  port                : **server port number**
                           Default Value       : -p 8080
  -v                       put application in verbose mode

Extend a param
 Extending a param may be needed if you want to add extra parse logic
 just **extend** the param class and override the ``parse(String);`` method

Create a param
 To create a param you just have to create a class that extend **CliParam**
 and implement the method ``parse(String);`` that will transform the String of the cli to your defined type

 **Please create a pull request, if you create params that may be usefull to others**

Arguments
---------

Available arguments
 **CliNoParamArgument** : for arguments with no param like **-l** in ``ls -l`` 
 **CliOneParamArgument** : for arguments with one param like **-f** in ``cut -f 3``
 **CliTwoParamArgument** : for arguments with Two params if you want to associate 2 values, for example in a performance injector
  you will need to associate a scenario to a number of client simulated
 **CliThreeParamArgument** : for arguments with Three params (should not happen ?)
 **CliNparamArgument** : for arguments with more params (should really not happen ?. you lose generics and need to cast to access values)

Shortname
 Shortname (like **l** in ``ls -l``) is mandatory for all arguments in ACLM.
 Available values are **A-Za-z0-9** this means that you cannot have more than 62 arguments (should be enough).
 The default argument is also defined with a shortname (because it work exactly the same way) but is not used at all (61 left ;)).

Name
 You can add a name to your argument to call it with the long form ex : (``ls --all`` for ``ls -a``)
 this name will also be used in the helper to describe the argument
 
Hidden names
 You can add additional names (``argument.addHiddenName(String)``) that will be used to call the argument
 but will not be listed in the helper. An example is the ``CliDefaultHelperArgument`` that is used to print helper by default,
 it have the hidden name ``/?`` to be compatible with windows helpers 

Description
 You can set a description of the argument using ``argument.setDescription(String)`` that will be used in the helper
 to describe the argument

Hide in Helper
 You can call ``argument.setHelpHidden(boolean)`` to mark your argument as hidden/shown in the Helper

Hide in Usage
 You can call ``argument.setHelpHidden(boolean)`` to mark your argument as hidden/shown in the Usage (like -h for helper
 that you don't want to appears in Usage)

Mandatory Arguments 
 You can call ``argument.setMandatory(boolean)`` to tell the parser that this argument must appears in the cli

Multicall
 You can set the argument as multicallable to get an array of values (or number of times called for a CliNoParamArgument)
 by calling ``argument.setMulticallMin(int)`` or ``argument.setMulticallMin(int)`` or even ``argument.setMulticall(int)``
 for an exact match of call. Minimum multicall cannot be set to <1, use ``argument.setMandatory(boolean)`` if you want
 to set your argument as optional.

Needed
 You can tell the parser that an argument needs to be set as the same time as another one with ``argument.addNeededArgument(Argument)``
 
Forbidden
 You can tell the parser that an argument cannot be set as the same time as another one with ``argument.addForbiddenArgument(Argument)``

Default value(s)
 You can set default values to parameters with ``CliOneParamArgument.setParamOneDefValue(PARAM_ONE_TYPE);``,  ``CliTwoParamArgument.setParamTwoDefValue(PARAM_TWO_TYPE);``,
 ``CliThreeParamArgument.setParamThreeDefValue(PARAM_THREE_TYPE);``, ``CliNParamArgument.setParamDefaultValue(Param, PARAM_TYPE);``
 or if you set your argument as multicallable you can set list of values with ``CliOneParamArgument.setParamOneDefValues(List<PARAM_ONE_TYPE>);``,
 ``CliTwoParamArgument.setParamTwoDefValues(List<PARAM_TWO_TYPE>);``, ``CliThreeParamArgument.setParamThreeDefValues(List<PARAM_THREE_TYPE>);``, ``CliNParamArgument.setParamDefaultValues(Param, List<PARAM_TYPE>);``

Usage
-----

Helper
------

Parser
------

Manager
-------


Advanced usage
==============



Advanced functioning
====================

Arguments have check definition methods that are called at the beginning of the ``CliArgumentManager.parse()``
to be sure there is no mistake in the definition. If there is an error a ``CliArgumentDefinitionException`` is raised
and is catch by the default parser to display an error and **exit the JVM**. 


remove default helper