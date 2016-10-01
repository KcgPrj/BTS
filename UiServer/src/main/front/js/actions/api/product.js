import 'isomorphic-fetch';
import 'babel-polyfill';
import {getRequest, postRequest} from './lib/method.js';
import {checkStatus} from './lib/utils.js';

export const FETCH_PRODUCTS_REQUEST = 'FETCH_PRODUCTS_REQUEST';
export const FETCH_PRODUCTS_SUCCESS = 'FETCH_PRODUCTS_SUCCESS';
export const FETCH_PRODUCTS_FAILURE = 'FETCH_PRODUCTS_FAILURE';
export const CREATE_PRODUCT_REQUEST = 'CREATE_PRODUCT_REQUEST';
export const CREATE_PRODUCT_SUCCESS = 'CREATE_PRODUCT_SUCCESS';
export const CREATE_PRODUCT_FAILURE = 'CREATE_PRODUCT_FAILURE';

function fetchProductsRequest(page) {
    return {
        page: page,
        type: FETCH_PRODUCTS_REQUEST,
    };
}

function fetchProductsSuccess(page, json) {
    return {
        page: page,
        type: FETCH_PRODUCTS_SUCCESS,
        data: json,
    };
}

function fetchProductsFailure(page, error) {
    return {
        page: page,
        type: FETCH_PRODUCTS_FAILURE,
        error: error,
    };
}

function createProductRequest(page) {
    return {
        page: page,
        type: CREATE_PRODUCT_REQUEST,
    };
}

function createProductSuccess(page, json) {
    return {
        page: page,
        type: CREATE_PRODUCT_SUCCESS,
        data: json,
    }
}

function createProductFailure(page, error) {
    return {
        page: page,
        type: CREATE_PRODUCT_FAILURE,
        error: error,
    }
}

/**
 * プロダクトを取得する
 * @param {string} page dispatchしたページ名
 * @param {string} teamId 取得するプロダクトのチームID
 * @return {function(*)}
 */
export function fetchProducts(page, teamId) {
    return async dispatch => {
        dispatch(fetchProductsRequest(page));

        const response = getRequest(`http://localhost:18080/${teamId}/products/show`);

        return response
            .then(res => checkStatus(res))
            .then(json => dispatch(fetchProductsSuccess(page, json)))
            .catch(error => {
                dispatch(fetchProductsFailure(page, error));
                return Promise.reject(error);
            });
    }
}

/**
 * プロダクトを作成する
 * @param {string} page dispatchしたページ名
 * @param {string} teamId プロダクトを作成するチームのチームID
 * @param {string} productName 新しく作成するプロダクト名
 * @return {function(*)}
 */
export function createProduct(page, teamId, productName) {
    return async dispatch => {
        dispatch(createProductRequest(page));

        const body = {
            productName: productName,
        };
        const response = postRequest(`http://localhost:18080/${teamId}/products/create`, body);

        return response
            .then(res => checkStatus(res))
            .then(json => dispatch(createProductSuccess(page, json)))
            .catch(error => {
                dispatch(createProductFailure(page, error));
                return Promise.reject(error);
            });
    }
}