var gulp = require("gulp");
var compass = require('gulp-compass');

gulp.task('compass', function() {
    gulp.src('./src/*.scss')
        .pipe(compass({
            config_file: './config.rb',
            css: 'css',
            sass: 'sass',
            style: 'expanded',
            task: 'watch'
        }))
});
gulp.task('default',function(){
    gulp.start('compass')
});