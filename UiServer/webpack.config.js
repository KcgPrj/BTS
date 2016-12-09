require('babel-polyfill');
var path = require('path');
var webpack = require('webpack');

var DEBUG = !process.argv.includes('--release');

var plugins = [
  new webpack.optimize.OccurenceOrderPlugin(),
  new webpack.DefinePlugin({ 'process.env.NODE_ENV': '"' + (process.env.NODE_ENV || (DEBUG ? 'development' : 'production')) + '"' })
];

if(!DEBUG){
  plugins.push(
    new webpack.optimize.DedupePlugin(),
    new webpack.optimize.UglifyJsPlugin({ compress: { screw_ie8: true, warnings: true } }),
    new webpack.optimize.AggressiveMergingPlugin()
  );
}

module.exports = {
  cache: DEBUG,

  debug: DEBUG,

  stats: {
    colors: true,
    timings: true,
    hash: true,
    version: true,
    chunks: true,
    chunkModules: true,
    cached: true,
    cachedAssets: true,
    reasons: DEBUG
  },

  entry: [
    './src/main/front/js/app.jsx',
  ],

  output: {
    path: path.resolve(__dirname, './build/resources/main/static/assets'),//ビルド結果の出力先
    filename: 'app.js',
    publicPath: '/assets/',
  },

  target: 'web',

  devtool: DEBUG ? 'eval-source-map' : false,

  plugins: plugins,

  module: {
    loaders: [{
        test: /\.jsx?$/,
        include: [path.resolve(__dirname, 'src/main/front/js')],
        loaders: ['react-hot', 'babel'] }
    ]
  },
  
  devServer: {
    historyApiFallback: true,
    watchOptions: { aggregateTimeout: 300, poll: 1000 },
    port: 3000,
    host: '0.0.0.0',
    proxy: {
    '/': 'http://localhost:8080',
    '/assets/**': 'http://localhost:8080',
    '/login/**': 'http://localhost:8080',
    '/*.html': 'http://localhost:8080',
    '**': 'http://localhost:18080'
    }
  },
};
