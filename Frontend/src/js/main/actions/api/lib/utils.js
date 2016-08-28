/**
 * サーバーから返ってきたレスポンスのステータスコードを判定し処理する。
 * 成功している場合はJSONファイルをPromiseで返す。
 * 失敗している場合はPromise.reject()で返す。
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

