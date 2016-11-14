import {FETCH_TEAMS_SUCCESS, FETCH_TEAMS_FAILURE} from '../actions/select_team.js';

const selectTeam = (state = {teams: []}, action) => {
    switch (action.type) {
        // チームの取得が完了したとき
        case FETCH_TEAMS_SUCCESS: {
            const newState = {
                ...state,
                teams: action.data,
            };
            return newState;
        }
        case FETCH_TEAMS_FAILURE: {
            return state;
        }
        default:
            return state;
    }
};

export default selectTeam;