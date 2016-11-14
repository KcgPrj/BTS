import {PAGE_SELECT_TEAM} from '../pages.js';
import {fetchTeams, createTeam as createTeamApi} from './api/team.js';

/**
 * ページを初期化する。
 * チームを取得する。
 * @returns {function()}
 */
export function initSelectTeamPage() {
    return dispatch => {
        return dispatch(fetchTeams(PAGE_SELECT_TEAM))
            .catch(error => console.log(error));
    };
}

/**
 * チームを新しく作成する。
 * @param {string} teamId 作成するチームのチームID
 * @returns {function()}
 */
export function createTeam(teamId) {
    return dispatch => {
        return dispatch(createTeamApi(PAGE_SELECT_TEAM, teamId))
            .then(() => dispatch(fetchTeams(PAGE_SELECT_TEAM)))
            .catch(error => console.log(error));
    };
}

export {FETCH_TEAMS_REQUEST, FETCH_TEAMS_SUCCESS, FETCH_TEAMS_FAILURE} from './api/team.js';