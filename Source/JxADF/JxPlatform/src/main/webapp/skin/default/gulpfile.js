var gulp = require("gulp");
var compass = require('gulp-compass');

var basepath = findDirectoryByName("framework");
function findDirectoryByName(name){
    var path = process.cwd();
    var cursor = path.indexOf(name);
    if(cursor != -1){
        path = path.slice(0,cursor + name.length);
    }
    return path + "/";
}

gulp.task('compass:watch', function() {
    gulp.src('./sass/*.scss')
        .pipe(compass({
            import_path:basepath+"JxPlatform/src/main/webapp/skin/default/sass",
            css: 'css',
            sass: 'sass',
            style: 'compressed',
            task: 'watch'
        }));
});
gulp.task('compass:compile', function() {
    gulp.src('./sass/*.scss')
        .pipe(compass({
            import_path:basepath+"JxPlatform/src/main/webapp/skin/default/sass",
            css: 'css',
            sass: 'sass',
            style: 'compressed'
        }));
});
gulp.task('default',function(){
    console.log("项目目录:"+basepath);
    gulp.start('compass:watch')
});