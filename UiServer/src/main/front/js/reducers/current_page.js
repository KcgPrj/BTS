import {CLEAR_CURRENT_PAGE_STATE} from '../actions/route_action.js';
import {PAGE_SELECT_TEAM, PAGE_TEAM_INDEX} from '../pages.js';
import selectTeam from './select_team.js';
import teamPage from './team_page.js';

/**
 * 現在いるページの状態を保持するstate
 * @param state
 * @param action
 * @returns {*}
 */
const currentPage = (state = {}, action) => {
    if (action.type === CLEAR_CURRENT_PAGE_STATE) {
        return {};
    }

    // それぞれのページに合わせたstateの処理をする
    switch (action.page) {
        case PAGE_SELECT_TEAM: {
            return selectTeam(state, action);
        }
        case PAGE_TEAM_INDEX: {
            return teamPage(state, action);
        }
        default:
            return state;
    }
};

export default currentPage;