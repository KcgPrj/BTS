import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import nock from 'nock';
import expect from 'expect';

import * as actions from '../../../../../../main/front/js/actions/api/team.js';
import {createNock} from '../nock_utils.js';

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);

const DUMMY_PAGE = 'DUMMY_PAGE';

describe('teams api actions', () => {
    afterEach(()=> {
        nock.cleanAll();
    });

    it(`creates ${actions.FETCH_TEAMS_SUCCESS} when fetching teams has been done`, () => {
        const responseBody = [{'teamId': 'team1', 'teamName': 'team1 name'}];

        createNock('http://localhost:18080', '/team/show/all')
            .get('/team/show/all')
            .reply(200, responseBody);

        const expectedActions = [
            {page: DUMMY_PAGE, type: actions.FETCH_TEAMS_REQUEST},
            {
                page: DUMMY_PAGE,
                type: actions.FETCH_TEAMS_SUCCESS,
                data: responseBody,
            },
        ];
        const store = mockStore({currentPage: {teams: []}});

        return store.dispatch(actions.fetchTeams(DUMMY_PAGE))
            .then(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it(`creates ${actions.FETCH_TEAMS_FAILURE} when fetching teams has been failed`, () => {
        createNock('http://localhost:18080', '/team/show/all')
            .get('/team/show/all')
            .reply(401);

        const expectedActions = [
            {page: DUMMY_PAGE, type: actions.FETCH_TEAMS_REQUEST},
            {
                page: DUMMY_PAGE,
                type: actions.FETCH_TEAMS_FAILURE,
                error: new Error(401),
            },
        ];
        const store = mockStore({currentPage: {teams: []}});

        return store.dispatch(actions.fetchTeams(DUMMY_PAGE))
            .catch(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it(`creates ${actions.CREATE_TEAM_SUCCESS} when creating team has been done`, () => {
        const teamId = 'team id';
        const teamName = 'team name';

        createNock('http://localhost:18080', '/team/create')
            .post('/team/create')
            .reply(200, {teamId: teamId, teamName: teamName});

        const expectedActions = [
            {page: DUMMY_PAGE, type: actions.CREATE_TEAM_REQUEST},
            {
                page: DUMMY_PAGE,
                type: actions.CREATE_TEAM_SUCCESS,
                data: {teamId: teamId, teamName: teamName},
            },
        ];
        const store = mockStore({currentPage: {teams: []}});

        return store.dispatch(actions.createTeam(DUMMY_PAGE, teamId, teamName))
            .then(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it(`creates ${actions.CREATE_TEAM_FAILURE} when creating team has been failed`, () => {
        const teamId = 'team id';
        const teamName = 'team name';

        createNock('http://localhost:18080', '/team/create')
            .post('/team/create')
            .reply(401, {});

        const expectedActions = [
            {page: DUMMY_PAGE, type: actions.CREATE_TEAM_REQUEST},
            {
                page: DUMMY_PAGE,
                type: actions.CREATE_TEAM_FAILURE,
                error: new Error(401),
            },
        ];
        const store = mockStore({currentPage: {teams: []}});

        return store.dispatch(actions.createTeam(DUMMY_PAGE, teamId, teamName))
            .catch(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });
});