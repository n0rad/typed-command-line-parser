Typed command line parser
===========================

Typed command line parser

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
            super("myApp");

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
        MyAppArgumentManager myAppArgumentManager = new MyAppArgumentManager();
        cutArgumentManager.parse(args);

        Integer port = myAppArgumentManager.portArgument.getParamOneValue();
        boolean verbose = myAppArgumentManager.verboseArgument.isSet();
        File file = myAppArgumentManager.fileArgument.getParamOneValue();       
 }

And it will generate this helper if you call ./myapp -h :

::

 Usage: myApp [ -v ][ -p port ] [ file ]
  -h, --help               This helper
  -p=port                  port                : server port number
                           Default Value       : -p 8080
  -v                       put application in verbose mode


Advanced Definition
===================

This part describe how you can configure the Argument manager to match your needs. All the code in this section used to be in your argument manager constructor.

If you want to customize the manager you will need to know more information about the composition.

Composition of the Helper :

::

 # ./myapp -h
 Usage: myapp [ -vp ] [ file ]                                        <-- UsageDisplayer informations
  -h, --help               This helper                                <-- Helper infos
  -p=port                  port                : server port number   <-- Helper infos with param description
                           Default Value       : -p 8080              <-- Helper infos with default value
  -v                       put application in verbose mode            <-- Helper infos with description


Composition of the ErrorManager :
 
::

 # ./myapp -p -v
 myapp: -v is not a valid Integer          <-- parsing of the param in error
   myapp -p -v                             <-- ErrorManager usagePath showing where is the error
 _________^                                <-- ErrorManager usagePath showing where is the error
 Usage: myapp [ -vp ] [ file ]             <-- UsageDisplayer informations
 Try `myapp --help' for more information.  <-- UsageDisplayer informations
 

Params
------

Available params
 All params included in the lib start with CliParam*
 Some may have methods to increase check when parse for example :
 
 ::

  CliParamInt.setZeroable(Boolean);
  CliParamInt.setNegativable(Boolean);
  CliParamFile.setIsDirectory(Boolean);
  CliParamFile.setIsFile(Boolean);
  CliParamFile.setIsHidden(Boolean);
  CliParamFile.setCanExecute(Boolean);
  CliParamFile.setCanRead(Boolean);
  CliParamFile.setCanWrite(Boolean);

Name
 Params are created with a name in constructor,
 this name is used in the usage : ./myapp [ -v ][ -p **port** ] [ **file** ]
 and also in the helper 
 
 ::

  -h, --help               This helper
  -p=port                  port                : server port number
                           Default Value       : -p 8080
  -v                       put application in verbose mode

Description
 You can add a description to your param with ``param.setDescription(String);``
 this description is used in the helper to describe the param :

 ::

  -h, --help               This helper
  -p=port                  port                : **server port number**
                           Default Value       : -p 8080
  -v                       put application in verbose mode

Extend a param
 Extending a param may be needed if you want to add extra parse logic
 just **extend** the param class and override the ``parse(String);`` method

 ::
 
  

Create a param
 To create a param you just have to create a class that extend **CliParam**
 and implement the method ``parse(String);`` that will transform the String of the cli to your defined type

 **Please create a pull request, if you create params that may be usefull to others**

Arguments
---------

Available arguments

::

  CliNoParamArgument    : arguments with no param like -l in ``ls -l`` 
  CliOneParamArgument   : arguments with one param like -f in ``cut -f 3``
  CliTwoParamArgument   : arguments with Two params if you want to associate 2 values, for example in a performance injector you will need to associate a scenario to a number of client simulated
  CliThreeParamArgument : arguments with Three params (should not happen ?)
  CliNparamArgument     : arguments with more params (should really not happen ?. you lose generics and need to cast to access values)

Shortname
 Shortname (like **l** in ``ls -l``) is mandatory for all arguments in ACLM.
 Available values are **A-Za-z0-9** this means that you cannot have more than 62 arguments (should be enough).
 The default argument is also defined with a shortname (because it work exactly the same way) but is not used at all (61 left ;)).

Name
 You can add a name to your argument to call it with the long form ex : (``ls --all`` for ``ls -a``)
 This name will also be used in the helper to describe the argument.
 
 ::

  argument.setName(String);

Hidden names
 You can add additional names that will be used to call the argument but will not be listed in the helper.
 An example is the ``CliDefaultHelperArgument`` that is used to print helper by default, it have the hidden name ``/?`` to be compatible with windows helpers 

 ::

  argument.addHiddenName(String);

Description
 You can set a description of the argument that will be used in the helper to describe the argument

 ::

  argument.setDescription(String);

Hide in Helper
 You can mark your argument as hidden/shown in the Helper

 ::

  argument.setHelpHidden(boolean);

Hide in Usage
 You can  mark your argument as hidden/shown in the Usage (like -h for helper that you don't want to appears in Usage)

 ::

  argument.setHelpHidden(boolean);

Mandatory Arguments 
 You can tell the parser that this argument must appears in the cli. **By default every arguments are optional ** 

 ::

  argument.setMandatory(boolean);

Multicall
 You can set the argument as multicallable to get an array of values (or number of times called for a CliNoParamArgument)
 by calling ``argument.setMulticallMin(int)`` or ``argument.setMulticallMin(int)`` or even ``argument.setMulticall(int)``
 for an exact match of call. Minimum multicall cannot be set to <1, use ``argument.setMandatory(boolean)`` if you want
 to set your argument as optional. ** 1 is min and Max default values, meaning no multicall **

Needed
 You can tell the parser that an argument needs to be set as the same time as another
 
 ::

  argument.addNeededArgument(Argument);

Forbidden
 You can tell the parser that an argument cannot be set as the same time as another one

 ::

  argument.addForbiddenArgument(Argument)

Default value(s)
 You can set default values to parameters that you will get if the param is not set by users

 ::

  CliOneParamArgument.setParamOneDefValue(PARAM_ONE_TYPE);
  CliTwoParamArgument.setParamTwoDefValue(PARAM_TWO_TYPE);
  CliThreeParamArgument.setParamThreeDefValue(PARAM_THREE_TYPE);
  CliNParamArgument.setParamDefaultValue(Param, PARAM_TYPE);

 or if you set your argument as multicallable you can set list of values

 ::

  CliOneParamArgument.setParamOneDefValues(List<PARAM_ONE_TYPE>);
  CliTwoParamArgument.setParamTwoDefValues(List<PARAM_TWO_TYPE>);
  CliThreeParamArgument.setParamThreeDefValues(List<PARAM_THREE_TYPE>);
  CliNParamArgument.setParamDefaultValues(Param, List<PARAM_TYPE>);

Usage
-----

Usage is a class used by the manager to display information on how to use the application.

Short Usage
 If you have a lot of arguments in you manager you may use the short argument to transform a usage like this : ``Usage: myApp [ -v ][ -p port ] [ file ]`` to this ``Usage: myapp [ -vp ] [ file ]``

 ::

  manager.getUsageDisplayer().setUsageShort(boolean)

Error manager
------------

Error path
 ErrorManager is used by the manager to display informations when the an error occured in parsing. You can disable the path of the error display

 :: 

  getErrorManager().setUsagePath(boolean);
 
Helper
------

Custom helper
 The helper is a special argument that stop parsing and display informations about how to use the application. By default the manager
 already have a default helper bind on ``-h``, ``--help`` and ``/?``. This helper is ``CliDefaultHelperArgument``, and display informations and the stop the JVM. 
 If you want to change this helper you can do it with ``setHelperArgument(new YourHelperArgument());`` see: **Advanced functionning** to build a helper.

Parser
------

You can customize how your parser will work. By default everything is activated ( set...(true) )

Read
 Read arguments to know if its really an argument or a parameter. in this exemple : read -f to know if its a param of -r or a new argument
 ``./toto42 -r 1 a 2 b -f 3``

 ::

  getParser().setTypeRead(boolean);


Scan shortname
 Scan argument in short form to find if a param is appended to it (only working if argument is a ``CliOneParamArgument``) ``./toto42 -r 1 a -f3``
 
 ::

  getParser().setTypeScanShortName(boolean);
     
Scan shortname argument
 Scan argument in short form to find if other arguments is appended to it (only working if only one argument in the pool is not a ``CliNoParamArgument`` ). ``./toto42 -vf 3``

 ::
 
  getParser().setTypeScanShortNameArguments(boolean);
 
Scan long name
 Scan argument in long form to find if a param is appended to it (only working if argument is a ``CliOneParamArgument`` ``./toto42 -r 1 a --file=3``

 ::

  getParser().setTypeScanLongName(boolean);

Dash is argument only
 Tell the parser that an argument with a dash (-) can only be an argument and can not be a parameter starting by a dash.

 ::

  getParser().setDashIsArgumentOnly(boolean);

Manager
-------

Error stream
 Error Stream is System.err by default, but you can redirect the cli error stream 

 :: 

  setErrorStream(PrintStream);

Output stream
 Output stream is System.out by default, but you can redirect the cli output stream
 
 ::
 
  setOutputStream(PrintStream);

New line characters
 The newLine character used in the cli is ``System.getProperty("line.separator")`` but you can change it 

 ::

  setNewLine(String);

Advanced usage
==============

This part describe how you can use the result of a parsing. All the code in this section used to be in your application with access to the manager.


Argument
--------

isSet
 You can check if an argument is set in the cli
 
 ::

  boolean verbose = myAppArgumentManager.verboseArgument.isSet();

numcall
 You can know how many times an argument was set in the cli

 ::

  int numcall = myAppArgumentManager.verboseArgument.getNumCall();



get param value
 You can get a param value in the cli 

 ::

  Integer port = myAppArgumentManager.portArgument.getParamOneValue();
 
 or if you are using a CliTwoParamArgument :
 
 :: 
 
  InetAddress port = myAppArgumentManager.hostPortArgument.getParamOneValue();
  Integer port = myAppArgumentManager.hostPortArgument.getParamTwoValue();

 **If you are getting values of a param that is not set you will get Null, but if a default value is set you will get the default value.**



get param values
 If your changed the multicall value to be more than 1, you **have to** get a list of values instead of a value.
 
 ::

  List<Integer> ports = myAppArgumentManager.portArgument.getParamOneValues();
 
 or if you are using a CliTwoParamArgument :
 
 :: 
 
  List<InetAddress> port = myAppArgumentManager.hostPortArgument.getParamOneValues();
  List<Integer> port = myAppArgumentManager.hostPortArgument.getParamTwoValues();



Advanced functioning
====================

Arguments have check definition methods that are called at the beginning of the ``CliArgumentManager.parse()``
to be sure there is no mistake in the definition. If there is an error a ``CliArgumentDefinitionException`` is raised
and is catch by the default parser to display an error and **exit the JVM**. 


