import 'isomorphic-fetch';
import 'babel-polyfill';
import {getRequest, postRequest} from './lib/method.js';
import {checkStatus} from './lib/utils.js';

export const FETCH_MEMBER_REQUEST = 'FETCH_MEMBER_REQUEST';
export const FETCH_MEMBER_SUCCESS = 'FETCH_MEMBER_SUCCESS';
export const FETCH_MEMBER_FAILURE = 'FETCH_MEMBER_FAILURE';

function fetchMemberRequest(page) {
    return {
        page: page,
        type: FETCH_MEMBER_REQUEST,
    };
}

function fetchMemberSuccess(page, json,) {
    return {
        page: page,
        type: FETCH_MEMBER_SUCCESS,
        data: json,
    };
}

function fetchMemberFailure(page, error) {
    return {
        page: page,
        type: FETCH_MEMBER_FAILURE,
        error: error,
    };
}

export function fetchMember(page, teamId) {
    return async dispatch => {
        dispatch(fetchMemberRequest(page));

        const response = getRequest(`http://localhost:18080/team/member/show?teamId=${teamId}`);

        return response
            .then(res => checkStatus(res))
            .then(json => dispatch(fetchMemberSuccess(page, json)))
            .catch(error => {
                dispatch(fetchMemberFailure(page, error));
                return Promise.reject(error);
            });
    }
}