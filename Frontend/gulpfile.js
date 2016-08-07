const gulp = require('gulp');
const gutil = require('gulp-util');
const sass = require('gulp-sass');

const webpack = require('webpack');

gulp.task('default', ['sass', 'webpack']);

// SASSのコンパイル
gulp.task('sass', () => {
    return gulp.src('./src/sass/**/*.scss')
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest('../UiServer/src/main/resources/static/assets/css'));
});

// JSファイルのトランスパイル
gulp.task('webpack', () => {
    process.argv.push('--release');
    webpack(require('./webpack.config.js')).run(callback => {
        return (err, stats) => {
            if (err) {
                throw new gutil.PluginError('webpack', err);
            }
            gutil.log('[webpack]', stats.toString({}));

            if (callback) {
                callback();
            }
        };
    });
});