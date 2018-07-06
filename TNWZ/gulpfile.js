const gulp = require('gulp');
const less = require('gulp-less');
const Path = require('path');
// const map = require('map-stream');
const rename = require('gulp-rename');
console.log(__dirname);

let lessGlobalVars = {
    globalVars: {
        root: generatePath('/assets'),
        icon: generatePath('/assets/style/icon.less')
    }
};

function generatePath (pathFromRoot) {
    return '"' + Path.join(__dirname, pathFromRoot) + '"';
}

gulp.task('default', function () {
    gulp.src(['./**/*.less', '!**/node_modules/**/*.less', '!./assets/**/*.less'])
        // .pipe(map(function (a) {
        //     console.log(a);
        // }))
        .pipe(less(lessGlobalVars))
        .pipe(rename(function (path) {
            console.log(path);
            path.dirname = './' + path.dirname;
            path.extname = '.wxss';
        }))
        .pipe(gulp.dest(Path.resolve(__dirname)))
        .on('error', console.error.bind(console));
});

gulp.watch(['./**/*.less', '!**/node_modules/**/*.less', '!./assets/**/*.less'], function (evt) {
    console.log(evt);
    gulp.src(evt.path, {base: __dirname})
        // .pipe(map(function (a) {
        //     console.log(a);
        // }))
        .pipe(less(lessGlobalVars))
        .pipe(rename(function (path) {
            console.log(path);
            path.dirname = './' + path.dirname;
            path.extname = '.wxss';
        }))
        .pipe(gulp.dest(Path.resolve(__dirname)))
        .on('error', console.error.bind(console));

});
