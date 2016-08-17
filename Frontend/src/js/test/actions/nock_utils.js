import nock from 'nock';

/**
 * nockオブジェクトを初期化して生成する
 * @param url {String} 通信をインターセプトするURL
 * @param path {String} '/api/path'のようなアクセス先となる文字列
 * @return {Scope}
 */
export function createNock(url, path) {
    return nock(url).intercept(path, 'OPTIONS')
        .reply(200, '', {
            'Access-Control-Allow-Credentials': true,
            'Access-Control-Allow-Headers': 'authorization',
            'Access-Control-Allow-Methods': 'GET,HEAD,POST',
            'Access-Control-Allow-Origin': 'http://localhost:8080',
        });
}