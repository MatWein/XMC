# XMC: Extended Money Control
XMC is a small local application to get an overview for your personal finances. The user has to enter account/depot data, transactions, saldi and more. After entering/importing all necessary data, the user can analyse it, for example, via charts.

## Application params
| Parameter            | Description                                                                                                                                                   | Possible Values                                     | Example                                                                    |
| :------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------ |:----------------------------------------------------| :--------------------------------------------------------------------------|
| -Dxmc.home.type      | Use this parameter to specify the application home directory were all data and config will be stored. Default is "home" which will use [USER_HOME]/.xmc       | "home"<br/>"workingdir"<br/>"custom"                | -Dxmc.home.type=workingdir                                                 |
| -Dxmc.home.path      | Can be used in combination with -Dhome.type=custom to specify a custom path.                                                                                  | path                                                | -Dhome.path="/home/user/mypath"<br/>-Dxmc.home.path="C:\\MyData\\XMC"      |
| -Dxmc.language       | Parameter to specify the application language. Default is system language.                                                                                    | language code (see java.util.Locale.forLanguageTag) | -Dxmc.language=de                                                          |

## Development (IntelliJ IDEA)
### Run configuration
To run the application within intellij IDEA you have to download/extract the SDK and specify the following JVM args in the run configuration:  
``-p "C:\Program Files\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml``  
This is not neccessary when running the application via command line (for example with java -jar xmc.jar).

### Internal Scene Builder
To use the internal Scene Builder with Java 11 and ControlsFX do the following:  
(Problem: "ControlsFX 11.0.0 requires at least Java Version 9")
1. Download Scene Builder Kit
2. Go to ``C:\Users\[USER]\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\[Version]\plugins\javaFX\lib\rt``
3. create a directory "java11"
4. copy the Scene Builder Kit JAR to the new folder and rename to "SceneBuilderKit-11.0.0.jar"
5. Add the following JVM argument to Intellij IDEA: ``-Djavafx.version=11.0.0``

### Building application JAR
Use one of the following commands to build a platform specific jar:  
``mvn clean install -Djavafx.platform=win``  
``mvn clean install -Djavafx.platform=linux``  
``mvn clean install -Djavafx.platform=mac``  
If no platform is specified, the current OS will be used.