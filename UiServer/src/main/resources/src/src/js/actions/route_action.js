export const INIT_PAGE = 'INIT_PAGE';
export const CLEAR_CURRENT_PAGE_STATE = 'CLEAR_CURRENT_PAGE_STATE';

/**
 * ページの初期化
 * @param pageName
 * @returns {{page: string, type: string}}
 */
export function initPage(pageName) {
    return {
        page: pageName,
        type: INIT_PAGE,
    };
}

/**
 * このページのstateを消去する
 * @returns {{type: string}}
 */
export function clearCurrentPageState() {
    return {
        type: CLEAR_CURRENT_PAGE_STATE,
    };
}