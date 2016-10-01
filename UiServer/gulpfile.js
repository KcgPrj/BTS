const gulp = require('gulp');
const gutil = require('gulp-util');
const sass = require('gulp-sass');

const webpack = require('webpack');

gulp.task('default', ['sass', 'webpack']);

// SASSのコンパイル
gulp.task('sass', () => {
    return gulp.src('./src/main/front/sass/**/*.scss') // src/main/front/sass内の.sassをターゲットにコンパイル
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest('./build/resources/main/static/assets/css')); //コンパイル結果を成果物内に突っ込む
});

// JSファイルのトランスパイル
gulp.task('build', () => {
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