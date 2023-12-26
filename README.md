# XMC: Extended Money Control
XMC is a small local application to get an overview of your personal finances. The user has to enter account/depot data, transactions, saldi and more. After entering/importing all necessary data, the user can analyse it, for example, via charts.

## Screenshots
- ![Alt text](/xmc.app/src/site/screenshots/dashboard.png?raw=true "Dashboard")
- ![Alt text](/xmc.app/src/site/screenshots/konto-tx.png?raw=true "Cash account")
- ![Alt text](/xmc.app/src/site/screenshots/analyse-1.png?raw=true "Analysis 1")
- ![Alt text](/xmc.app/src/site/screenshots/analyse-2.png?raw=true "Analysis 2")

## Application params
| Parameter            | Description                                                                                                                                                   | Possible Values                                     | Example                                                                    |
| :------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------ |:----------------------------------------------------| :--------------------------------------------------------------------------|
| -Dxmc.home.type      | Use this parameter to specify the application home directory were all data and config will be stored. Default is "home" which will use [USER_HOME]/.xmc       | "home"<br/>"workingdir"<br/>"custom"                | -Dxmc.home.type=workingdir                                                 |
| -Dxmc.home.path      | Can be used in combination with -Dhome.type=custom to specify a custom path.                                                                                  | path                                                | -Dhome.path="/home/user/mypath"<br/>-Dxmc.home.path="C:\\MyData\\XMC"      |
| -Dxmc.language       | Parameter to specify the application language. Default is system language.                                                                                    | language code (see java.util.Locale.forLanguageTag) | -Dxmc.language=de                                                          |
| -Dxmc.style          | Changes the general appearance of the application to dark or light mode.                                                                                      | dark, light                                         | -Dxmc.style=dark                                                           |
| -Djdk.gtk.version    | Use this on linux systems if you have problems with drag&drop.                                                                                                 | 2, 3                                                | -Djdk.gtk.version=2                                                        |

## Development (IntelliJ IDEA)

### Building application JAR
Use following command to build win, linux and mac JARs:
``./build.sh``  

### Update BLZ (Bankleitzahlen) file (blz-data.csv)
A current and updated file can be downloaded from: https://www.bundesbank.de/de/aufgaben/unbarer-zahlungsverkehr/serviceangebot/bankleitzahlen/download-bankleitzahlen-602592 as a XLSX file. Then you have to remove the unnecessary columns and save it as CSV file in UTF-8 format with ; as separator.

## Licence
XMC is licensed under the [GNU GENERAL PUBLIC LICENSE](https://github.com/MatWein/XMC/blob/master/COPYING)