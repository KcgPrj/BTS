import 'whatwg-fetch';
import 'babel-polyfill';
import {getOptions, postOptions, checkStatus, redirectToLogin} from './api_utils.js';

export const FETCH_TEAMS_REQUEST = 'FETCH_TEAMS_REQUEST';
export const FETCH_TEAMS_SUCCESS = 'FETCH_TEAMS_SUCCESS';
export const FETCH_TEAMS_FAILURE = 'FETCH_TEAMS_FAILURE';
export const CREATE_TEAM_REQUEST = 'CREATE_TEAM_REQUEST';
export const CREATE_TEAM_SUCCESS = 'CREATE_TEAM_SUCCESS';
export const CREATE_TEAM_FAILURE = 'CREATE_TEAM_FAILURE';

function fetchTeamsRequest(page) {
    return {
        page: page,
        type: FETCH_TEAMS_REQUEST,
    };
}

function fetchTeamsSuccess(page, data) {
    return {
        page: page,
        type: FETCH_TEAMS_SUCCESS,
        data: {
            teams: data,
        },
    };
}

function fetchTeamFailure(page, data) {
    return {
        page: page,
        type: FETCH_TEAMS_FAILURE,
        data: data,
    };
}

function createTeamRequest(page) {
    return {
        page: page,
        type: CREATE_TEAM_REQUEST,
    };
}

function createTeamSuccess(page, json) {
    return {
        page: page,
        type: CREATE_TEAM_SUCCESS,
        data: {
            teamId: json.teamId,
        },
    };
}

function createTeamFailure(page, json) {
    return {
        page: page,
        type: CREATE_TEAM_FAILURE,
        data: json,
    };
}

/**
 * チームを取得する
 * @param {string} page dispatchしたページ名
 * @return {function(*)}
 */
export function fetchTeams(page) {
    return async dispatch => {
        dispatch(fetchTeamsRequest(page));

        let options = {};
        try {
            options = getOptions();
        } catch (e) {
            redirectToLogin();
            return Promise.resolve(dispatch(fetchTeamFailure(page, new Error('no token'))));
        }

        let response;
        try {
            response = await fetch('http://localhost:18080/team/show/all', {
                ...options,
            });
        } catch (e) {
            redirectToLogin();
            return Promise.reject();
        }

        Promise.resolve(response)
            .then(res => checkStatus(res))
            .then(json => dispatch(fetchTeamsSuccess(page, json)))
            .catch(error => {
                dispatch(fetchTeamFailure(page, error));
                return Promise.reject(error);
            });
    };
}

/**
 * チームを作成する
 * @param {string} page dispatchしたページ名
 * @param {string} teamId チームID
 * @param {string} teamName チーム名
 * @returns {function(*)}
 */
export function createTeam(page, teamId, teamName = '') {
    if (teamName === '') {
        teamName = teamId;
    }

    return async dispatch => {
        dispatch(createTeamRequest(page));

        let options = {};
        try {
            options = postOptions();
        } catch (e) {
            redirectToLogin();
            return Promise.resolve(dispatch(createTeamFailure(page, new Error('no token'))));
        }

        let response;
        try {
            response = await fetch('http://localhost:18080/team/create', {
                ...options,
                body: JSON.stringify({
                    teamId: teamId,
                    teamName: teamName,
                }),
            });
        } catch (error) {
            redirectToLogin();
            return Promise.reject(error)
        }

        Promise.resolve(response)
            .then(response => checkStatus(response))
            .then(json => dispatch(createTeamSuccess(page, json)))
            .catch(error => {
                return dispatch(createTeamFailure(page, error))
            });
    };
}