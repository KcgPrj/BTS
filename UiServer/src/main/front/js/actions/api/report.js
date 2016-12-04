import 'isomorphic-fetch';
import 'babel-polyfill';
import {getRequest} from './lib/method.js';
import {checkStatus} from './lib/utils.js';

export const FETCH_REPORTS_REQUEST = 'FETCH_REPORTS_REQUEST';
export const FETCH_REPORTS_SUCCESS = 'FETCH_REPORTS_SUCCESS';
export const FETCH_REPORTS_FAILURE = 'FETCH_REPORTS_FAILURE';

function fetchReportsRequest(page) {
    return {
        page: page,
        type: FETCH_REPORTS_REQUEST,
    };
}

function fetchReporsSuccess(page, json) {
    return {
        page: page,
        type: FETCH_REPORTS_SUCCESS,
        data: json,
    };
}

function fetchReportsFailure(page, error) {
    return {
        page: page,
        type: FETCH_REPORTS_FAILURE,
        error: error,
    };
}

export function fetchReports(page, productId, productToken) {
    return async dispatch => {
        dispatch(fetchReportsRequest(page));

        const response = getRequest(`http://localhost:18080/reports/list`, {
            productId: productId,
            productToken: productToken,
        });

        return response
            .then(res => checkStatus(res))
            .then(json => dispatch(fetchReporsSuccess(page, json)))
            .catch(error => {
                dispatch(fetchReportsFailure(page, error));
                return Promise.reject(error);
            });
    }
}