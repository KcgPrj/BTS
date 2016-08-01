import 'whatwg-fetch';
import {getOptions, postOptions} from './api_utils.js';

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
    console.log('fetch success');
    return {
        page: page,
        type: FETCH_TEAMS_SUCCESS,
        data: {
            teams: data,
        },
    };
}

function fetchTeamFailure(page, data) {
    console.log('fetch failed');
    console.log(data);
    return {
        page: page,
        type: FETCH_TEAMS_FAILURE,
        data: data,
    }
}

function createTeamRequest(page) {
    return {
        page: page,
        type: CREATE_TEAM_REQUEST,
    };
}

function createTeamSuccess(page, json) {
    console.log('create team success');
    return {
        page: page,
        type: CREATE_TEAM_SUCCESS,
        data: {
            teamId: json.teamId,
        },
    };
}

function createTeamFailure(page, json) {
    console.log('create team failed');
    return {
        page: page,
        type: CREATE_TEAM_FAILURE,
        data: json,
    }
}

/**
 * チームを取得する
 */
export function fetchTeams(page) {
    return dispatch => {
        dispatch(fetchTeamsRequest(page));

        let options = {};
        try {
            options = getOptions();
        } catch (e) {
            location.href = '/';
            return Promise.resolve(dispatch(fetchTeamFailure(page, {})));
        }
        return fetch('http://localhost:18080/team/show/all', {
            ...options,
        })
            .then(response => response.json())
            .then(json => dispatch(fetchTeamsSuccess(page, json)))
            .catch(error => dispatch(fetchTeamFailure(page, error)));
    };
}

/**
 * チームを作成する
 * @param {string} page dispatchしたページ名
 * @param {string} teamId チームID
 * @param {string} teamName チーム名
 * @returns {function()}
 */
export function createTeam(page, teamId, teamName = '') {
    if (teamName === '') {
        teamName = teamId;
    }

    return dispatch => {
        dispatch(createTeamRequest(page));

        let options = {};
        try {
            options = postOptions();
        } catch (e) {
            location.href = '/';
            return Promise.resolve(dispatch(createTeamFailure(page, {error: 'no token in cookie'})));
        }
        return fetch('http://localhost:18080/team/create', {
            ...options,
            body: JSON.stringify({
                teamId: teamId,
                teamName: teamName,
            }),
        })
            .then(response => response.json())
            .then(json => dispatch(createTeamSuccess(page, json)))
            .catch(error => dispatch(createTeamFailure(page, error)));
    };
}