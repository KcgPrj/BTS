var path = require('path');
var webpack = require('webpack');
var WebpackDevServer = require('webpack-dev-server');
var config = require('./webpack.config.js');

config.entry.push(
    'webpack-dev-server/client?http://localhost:3000',
    'webpack/hot/only-dev-server'
);

config.plugins.push(new webpack.HotModuleReplacementPlugin());

var server = new WebpackDevServer(webpack(config), {
    publicPath: config.output.publicPath,
    hot: true,
    contentBase: '../static/',
    historyApiFallback: {
        rewrites: [
            {from: /react_index.html\/(.*)$/, to: "react_index.html"}
        ]
    }
});

server.use('/', function (request, response) {
    response.sendFile(path.resolve(__dirname, '../', 'static', 'react_index.html'));
});

server.listen(3000, 'localhost', function (err, result) {
    if (err) {
        return console.log(err);
    }
    console.log('Listening at http://localhost:3000/');
});
