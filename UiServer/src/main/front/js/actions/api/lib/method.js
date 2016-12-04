import 'isomorphic-fetch';
import 'babel-polyfill';
import Cookies from 'js-cookie';

/**
 * GETメソッドでHTTP通信する
 * @param {string} url APIのエンドポイント
 * @return {Promise.<*>}
 */
// TODO GETリクエストにbodyがあるのは不自然なので削除する
export async function getRequest(url, body) {
    const options = getOptions();

    let response;
    try {
        response = await fetch(url, {
            body: JSON.stringify(body),
            ...options,
        });
    } catch (err) {
        // アクセストークンが有効でなかったとき
        redirectToLogin();
        return Promise.reject(err);
    }

    return Promise.resolve(response);
}

/**
 * POSTメソッドでHTTP通信する
 * @param {string} url APIのエンドポイント
 * @param {Object.<string, string>} [body={}] リクエストボディとなるJSONオブジェクト
 * @return {Promise.<*>}
 */
export async function postRequest(url, body = {}) {
    const options = postOptions();

    let response;
    try {
        response = await fetch(url, {
            ...options,
            body: JSON.stringify(body),
        });
    } catch (err) {
        // アクセストークンが有効でなかったとき
        redirectToLogin();
        return Promise.reject(err)
    }

    return Promise.resolve(response)
}

/**
 * Fetch APIでGETするときの設定を返す
 * @returns {{method: string, mode: string, headers: {Authorization: string}}}
 */
function getOptions() {
    const auth = genAuthToken();
    return {
        method: 'GET',
        mode: 'cors',
        headers: {
            ...auth
        },
    };
}

/**
 * Fetch APIでPOSTするときの設定を返す
 * @returns {{method: string, mode: string, headers: {Accept: string, Content-Type: string, Authorization: string}}}
 */
function postOptions() {
    const auth = genAuthToken();
    return {
        method: 'POST',
        mode: 'cors',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            ...auth,
        },
    };
}

/**
 * API認証用のトークンを取得する
 * @returns {{}}
 */
function genAuthToken() {
    const token = Cookies.get('access_token');
    return {Authorization: 'Bearer ' + token};
}

/**
 * ログインページに移動する
 */
function redirectToLogin() {
    if (!location) {
        return;
    }
    location.href = '/';
}