import {PAGE_SELECT_TEAM} from '../pages.js';

export const CREATE_TEAM = 'CREATE_TEAM';

export function createTeam() {
    return {
        page: PAGE_SELECT_TEAM,
        type: CREATE_TEAM,
    };
}