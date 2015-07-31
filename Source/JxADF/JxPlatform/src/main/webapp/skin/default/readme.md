使用gulp-compass框架来管理scss和图片
初次使用需要安装软件 http://compass-style.org/install/
1、nodejs
2、ruby
3、compass -> gem install compass
4、gulp -> npm install gulp
5、gulp-compass -> npm install gulp-compass

-注意：
如果需要在scss中使用中文，需要在页面第一行加上@charset 'utf-8' 否则编译会报错。
compass watch有时候会出现"to_tree" undefined的报错，关闭重新执行gulp即可。