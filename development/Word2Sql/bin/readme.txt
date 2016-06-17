
2013.07 魏明智 采用Spring+Groovy方式重新架构了此工具，现推荐使用全新版本

==========安装配置==================
1.把lib目录下的jacob-1.18-M2-x64.dll添加到系统盘WINDOWS\system32目录下 并且也需要添加到JAVA_HOME的Lib和bin目录下。注意不要改文件名。
2.将bin目录下wordToSql.bat文件中的JAVA_HOME的路径配置为当前环境的jdk路径
=======================================================

===========使用说明=================

[impfile=fileName]导入的word文件名(完整文件路径名),必填
[expfile=fileName]保存的文件名
[log=fileName]输出的文件名
[siteid=JX]默认地点
[orgid=JXKJ]默认组织架构
[help=Y]获得帮助信息				
[framework=J] J表示JxPlatform平台


参考示例1：导出sql语句
执行命令：WordToSql impfile=D:\Work\公司综合管理\开发库\文档库\开发文档\详细设计\DDR20130604-请假管理详细设计说明书.doc expfile=..\sql\leave.sql  framework=J











