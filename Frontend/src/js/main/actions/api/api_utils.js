import Cookies from 'js-cookie';

/**
 * API認証用のトークンを取得する
 * @throws {Error} トークンがクッキーに存在しなかった場合
 * @returns {*}
 */
export function genAuthToken() {
    const token = Cookies.get('access_token');
    if (token === undefined) {
        throw new Error('no token in cookie');
    }
    return {Authorization: 'Bearer ' + token};
}

/**
 * Fetch APIでGETするときの設定を返す
 * @throws {Error} トークンがクッキーに存在しなかった場合
 * @returns {{method: string, mode: string, headers: {Authorization: string}}}
 */
export function getOptions() {
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
 * @throws {Error} トークンがクッキーに存在しなかった場合
 * @returns {{method: string, mode: string, headers: {Accept: string, Content-Type: string, Authorization: string}}}
 */
export function postOptions() {
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
 * サーバーから返ってきたレスポンスのステータスコードを判定し処理する。
 * 失敗している場合はPromise.reject()で返す。
 * 成功している場合はJSONファイルをPromiseで返す。
 * @param {Response} response Fetch APIのResponseオブジェクト
 * @returns {Promise}
 */
export function checkStatus(response) {
    if (!response.ok) {
        return Promise.reject(new Error(response.status));
    } else {
        return response.json();
    }
}

/**
 * ログインページに移動する
 */
export function redirectToLogin() {
    location.href = '/';
}