@cls
@echo ***************************************************
@echo **     mvn批处理工具                             **
@echo **                                               **
@echo **开发公司：广州健新自动化科技有限公司           **
@echo **开发者：魏明智 wmzsoft@gmail.com               **
@echo **地址：广州天河高新技术产业开发区科韵路18号3楼　**
@echo **电话：020-28812525-2211　                      **
@echo **HomePage：http://osgi.jxtech.net               **
@echo ****************************************************
@echo.
:USEAGE
@echo ------------项目级别--------------------------------------
@echo 0. 编译整个工程    1. 打包整个工程
@echo 2. 创建插件        3. 打包指定的插件
@echo.
@echo.
@echo.
@echo.------------辅助功能--------------------------------------
@echo !. 清理此目录下面所有的target文件夹 mvn clean
@echo k. 自己输入命令  u. 查看使用方法  x. 退出

@set MY_HOME=%~dp0

@echo.
@set /p KEY= 请输入您的选择：
@echo off

@CLS

if %KEY% == 0 goto M0
if %KEY% == 1 goto M1
if %KEY% == 2 goto M2
if %KEY% == 3 goto M3
if %KEY% == 4 goto M4
if %KEY% == 5 goto M5
if %KEY% == 6 goto M6
if %KEY% == 7 goto M7
if %KEY% == 8 goto M8
if %KEY% == a goto MA
if %KEY% == b goto MB
if %KEY% == c goto MC
if %KEY% == d goto MD
if %KEY% == e goto ME
if %KEY% == f goto MF
if %KEY% == g goto MG
if %KEY% == h goto MH
if %KEY% == i goto MI
if %KEY% == j goto MJ
if %KEY% == k goto MK
if %KEY% == l goto ML
if %KEY% == m goto MM
if %KEY% == n goto MN
if %KEY% == o goto MO
if %KEY% == p goto MP
if %KEY% == q goto MQ
if %KEY% == r goto MR
if %KEY% == s goto MS
if %KEY% == t goto MT
if %KEY% == u goto USEAGE
if %KEY% == v goto MV
if %KEY% == w goto MW
if %KEY% == x goto MX
if %KEY% == y goto MY
if %KEY% == z goto MZ
if %KEY% == ! goto CLEAR_TARGET
goto USEAGE

:M0
@call mvn clean compile
goto USEAGE

:M1
@call mvn clean package
goto USEAGE

:M2
@echo a. 在\plugins\ProjectA目录，项目A的插件
@echo b. 在\plugins\ProjectB目录，项目B的插件
@choice /C AB /N /M "请选择[A、B]，不用按回车 "
@if %ERRORLEVEL%==2 goto M2B
cd %MY_HOME%\plugins\ProjectA
@set PARENT_PID=ProjectA
goto M2START
:M2B
cd %MY_HOME%\plugins\ProjectB
@set PARENT_PID=ProjectB
goto M2START
:M2START
@set /p AID= 请输入插件唯一标识：
@set /p PACKNAME= 请输入包名：
  set jbo=%AID%
  set jbo1=%jbo:~0,1%
  if %jbo1%==a set jbo1=A
  if %jbo1%==b set jbo1=B
  if %jbo1%==c set jbo1=C
  if %jbo1%==d set jbo1=D
  if %jbo1%==e set jbo1=E
  if %jbo1%==f set jbo1=F
  if %jbo1%==g set jbo1=G
  if %jbo1%==h set jbo1=H
  if %jbo1%==i set jbo1=I
  if %jbo1%==j set jbo1=J
  if %jbo1%==k set jbo1=K
  if %jbo1%==l set jbo1=L
  if %jbo1%==m set jbo1=M
  if %jbo1%==n set jbo1=N
  if %jbo1%==o set jbo1=O
  if %jbo1%==p set jbo1=P
  if %jbo1%==q set jbo1=Q
  if %jbo1%==r set jbo1=R
  if %jbo1%==s set jbo1=S
  if %jbo1%==t set jbo1=T
  if %jbo1%==u set jbo1=U
  if %jbo1%==v set jbo1=V
  if %jbo1%==w set jbo1=W
  if %jbo1%==x set jbo1=X
  if %jbo1%==y set jbo1=Y
  if %jbo1%==z set jbo1=Z
  set jbo=%jbo1%%jbo:~1%
  set GEN_OPTIONS=-DarchetypeGroupId=com.jxtech -DarchetypeVersion=2.0.0 -DarchetypeArtifactId=jxplugin-archetype -DinteractiveMode=true
  set GEN_OPTIONS=%GEN_OPTIONS% -DgroupId=com.jxtech -Dversion=1.0.0 -DarchetypeCatalog=local 
  set GEN_OPTIONS=%GEN_OPTIONS% -Dpackage=%PACKNAME% -DartifactId=%AID% -Djboname=%jbo% -DparentArtifactId=%PARENT_PID% 
  set GEN_OPTIONS=%GEN_OPTIONS% -Dauthor=%username% -Ddate="%date%"
call mvn archetype:generate %GEN_OPTIONS%
@pause
cd %MY_HOME%
goto USEAGE

:M3
@set /p PLUGIN_NAME= 请输入要打包的插件ID： 
@call mvn clean package -pl :%PLUGIN_NAME%
goto USEAGE


:CLEAR_TARGET
@call mvn clean
goto USEAGE

:MK
@set /p CKEY= #  
CALL %CKEY%
goto USEAGE

:MX
@pause