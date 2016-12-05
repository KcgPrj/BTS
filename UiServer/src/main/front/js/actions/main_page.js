import {PAGE_MAIN_PAGE} from '../pages.js';
import {fetchProducts, createProduct as createProductApi} from './api/product.js';
import {fetchMember} from './api/member.js';
import {fetchReports} from './api/report.js';

export const INIT_STATE = 'INIT_STATE';
export const SHOW_PRODUCT_TOKEN_TAB = 'SHOW_PRODUCT_TOKEN_TAB';
export const SHOW_REPORT_TAB = 'SHOW_REPORT_TAB';

/**
 * ページを初期化する。
 * チームを取得する。
 * @returns {function()}
 */
export function initMainPage(teamId, productId) {
    return dispatch => {
        dispatch(initState());
        Promise.all([
            dispatch(fetchProducts(PAGE_MAIN_PAGE, teamId)),
            dispatch(fetchMember(PAGE_MAIN_PAGE, teamId)),
            dispatch(fetchReports(PAGE_MAIN_PAGE, productId)),
        ])
            .catch(error => console.log(error));
    };
}

/**
 * プロダクトを新しく作成する
 * @param teamId {string} チームID
 * @param productName {string} 新しく作るプロダクトの名前
 * @returns {function(*)}
 */
export function createProduct(teamId, productName) {
    return dispatch => {
        return dispatch(createProductApi(PAGE_MAIN_PAGE, teamId, productName))
            .then(() => {
                return dispatch(fetchProducts(PAGE_MAIN_PAGE, teamId));
            })
            .catch(error => console.log(error));
    };
}

export function initState() {
    return {
        type: INIT_STATE,
        page: PAGE_MAIN_PAGE,
    };
}

export function showProductTokenTab() {
    return {
        type: SHOW_PRODUCT_TOKEN_TAB,
        page: PAGE_MAIN_PAGE,
    };
}

export function showReportTab() {
    return {
        type: SHOW_REPORT_TAB,
        page: PAGE_MAIN_PAGE,
    };
}

export {
    FETCH_PRODUCTS_REQUEST,
    FETCH_PRODUCTS_SUCCESS,
    FETCH_PRODUCTS_FAILURE,
    CREATE_PRODUCT_REQUEST,
    CREATE_PRODUCT_SUCCESS,
    CREATE_PRODUCT_FAILURE,
} from './api/product.js';

export {
    FETCH_MEMBER_REQUEST,
    FETCH_MEMBER_SUCCESS,
    FETCH_MEMBER_FAILURE,
} from './api/member.js';

export {
    FETCH_REPORTS_REQUEST,
    FETCH_REPORTS_SUCCESS,
    FETCH_REPORTS_FAILURE,
} from './api/report.js';