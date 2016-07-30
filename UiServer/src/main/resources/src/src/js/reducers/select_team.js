import {INIT_PAGE} from '../actions/route_action.js';
import {CREATE_TEAM} from '../actions/select_team.js';

const selectTeam = (state = {teams: []}, action) => {
    switch (action.type) {
        case INIT_PAGE:
        {
            const initialState = {
                teams: ['team1', 'team2', 'team3', 'team4'],
            };
            return initialState;
        }
        case CREATE_TEAM:
        {
            const newState = {teams: state.teams.concat('new team' + (state.teams.length + 1))};
            return newState;
        }
        default:
            return state;
    }
};

export default selectTeam;