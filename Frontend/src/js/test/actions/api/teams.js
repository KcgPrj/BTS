import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import nock from 'nock';
import expect from 'expect';

import * as actions from '../../../main/actions/api/team.js';
import {createNock} from '../nock_utils.js';

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);

const dummyPage = 'DUMMY_PAGE';

describe('async actions', () => {
    afterEach(()=> {
        nock.cleanAll();
    });

    it('creates FETCH_TEAMS_SUCCESS when fetching teams has been done', () => {
        createNock('http://localhost:18080', '/team/show/all')
            .get('/team/show/all')
            .reply(200, [{'teamId': 'team1', 'teamName': 'team1 name'}]);

        const expectedActions = [
            {page: dummyPage, type: actions.FETCH_TEAMS_REQUEST},
            {
                page: dummyPage,
                type: actions.FETCH_TEAMS_SUCCESS,
                data: {teams: [{'teamId': 'team1', 'teamName': 'team1 name'}]},
            },
        ];
        const store = mockStore({currentPage: {teams: []}});

        return store.dispatch(actions.fetchTeams(dummyPage))
            .then(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it('creates FETCH_TEAMS_FAILURE when fetching teams has been failed', () => {
        createNock('http://localhost:18080', '/team/show/all')
            .get('/team/show/all')
            .reply(401);

        const expectedActions = [
            {page: dummyPage, type: actions.FETCH_TEAMS_REQUEST},
            {
                page: dummyPage,
                type: actions.FETCH_TEAMS_FAILURE,
                data: new Error(401),
            },
        ];
        const store = mockStore({currentPage: {teams: []}});

        return store.dispatch(actions.fetchTeams(dummyPage))
            .catch(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it('creates CREATE_TEAMS_SUCCESS when creating teams has been done', () => {
        const teamId = 'team id';
        const teamName = 'team name';

        createNock('http://localhost:18080', '/team/create')
            .post('/team/create')
            .reply(200, {teamId: teamId, teamName: teamName});

        const expectedActions = [
            {page: dummyPage, type: actions.CREATE_TEAM_REQUEST},
            {
                page: dummyPage,
                type: actions.CREATE_TEAM_SUCCESS,
                data: {teamId: teamId, teamName: teamName},
            },
        ];
        const store = mockStore({currentPage: {teams: []}});

        return store.dispatch(actions.createTeam(dummyPage, teamId, teamName))
            .then(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it('creates CREATE_TEAMS_Failure when creating teams has been failed', () => {
        const teamId = 'team id';
        const teamName = 'team name';

        createNock('http://localhost:18080', '/team/create')
            .post('/team/create')
            .reply(401, {});

        const expectedActions = [
            {page: dummyPage, type: actions.CREATE_TEAM_REQUEST},
            {
                page: dummyPage,
                type: actions.CREATE_TEAM_FAILURE,
                data: new Error(401),
            },
        ];
        const store = mockStore({currentPage: {teams: []}});

        return store.dispatch(actions.createTeam(dummyPage, teamId, teamName))
            .catch(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });
});