export const CLEAR_CURRENT_PAGE_STATE = 'CLEAR_CURRENT_PAGE_STATE';

/**
 * このページのstateを消去する
 * @returns {{type: string}}
 */
export function clearCurrentPageState() {
    return {
        type: CLEAR_CURRENT_PAGE_STATE,
    };
}