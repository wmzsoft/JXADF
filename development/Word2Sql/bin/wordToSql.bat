@REM CLS
@echo ***************************************************
@echo **                                               **
@echo **开发公司：广州健新自动化科技有限公司           **
@echo **开发者：魏明智                                 **
@echo **地址：广州天河高新技术产业开发区科韵路18号3楼　**
@echo **电话：020-28812525-2211　                      **
@echo ****************************************************
@echo 注，由于BAT的参数限制，如果参数过多，请直接使用 java -jar WordToSql.jar 加参数
@echo.
@echo off
@REM set JAVA_HOME="C:\Program Files\Java\jdk1.7.0_65"

@set jxclasspath=..\lib\log4j-1.2.8.jar;..\lib\jacob.jar;..\lib\jdom.jar;..\lib\ojdbc14.jar;.;..\lib\jacob.dll;..\lib\antlr-2.7.7.jar;..\lib\asm-4.0.jar;..\lib\commons-attributes-api.jar;..\lib\commons-attributes-compiler.jar;..\lib\commons-logging.jar;..\lib\groovy-2.1.5.jar;..\lib\spring-beans.jar;..\lib\spring-context.jar;..\lib\spring-core.jar
@set JAVA_OPTION = -Dsiteid=JX -Dorgid=JXKJ -Dstoragepartition=MAXIMO_DATA -DstoragepartitionIndex=MAXIMO_INDEX
@SHIFT
@"%JAVA_HOME%\bin\java" -classpath "%jxclasspath%" -jar %JAVA_OPTION% WordToSql-2013.jar %0=%1 %2=%3 %4=%5 %6=%7 %8=%9 
@pause
@echo on
