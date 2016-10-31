import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import nock from 'nock';
import expect from 'expect';

import * as actions from '../../../../../../main/front/js/actions/api/member.js';
import {createNock} from '../nock_utils.js';

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);

const MEMBER1 = 'MEMBER1';
const TEAM1 = 'TEAM1';
const DUMMY_PAGE = 'DUMMY_PAGE';

describe('member api actions', () => {
    afterEach(() => {
        nock.cleanAll();
    });

    it(`creates ${actions.FETCH_MEMBER_SUCCESS} when fetching members has been done`, () => {
        const responseBody = [{
            "id": 1,
            "name": MEMBER1,
            "type": "GITHUB",
        }];

        createNock('http://localhost:18080', `/team/member/show?teamId=${TEAM1}`)
            .get(`/team/member/show?teamId=${TEAM1}`)
            .reply(200, responseBody);

        const expectedActions = [
            {page: DUMMY_PAGE, type: actions.FETCH_MEMBER_REQUEST},
            {
                page: DUMMY_PAGE,
                type: actions.FETCH_MEMBER_SUCCESS,
                data: responseBody,
            },
        ];
        const store = mockStore({currentPage: {}});

        return store.dispatch(actions.fetchMember(DUMMY_PAGE, TEAM1))
            .then(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it(`creates ${actions.FETCH_MEMBER_FAILURE} when fetching members has been failed`, () => {
        createNock('http://localhost:18080', `/team/member/show?teamId=${TEAM1}`)
            .get(`/team/member/show?teamId=${TEAM1}`)
            .reply(401, {});

        const expectedActions = [
            {page: DUMMY_PAGE, type: actions.FETCH_MEMBER_REQUEST},
            {
                page: DUMMY_PAGE,
                type: actions.FETCH_MEMBER_FAILURE,
                error: new Error(401),
            },
        ];
        const store = mockStore({currentPage: {}});

        return store.dispatch(actions.fetchMember(DUMMY_PAGE, TEAM1))
            .catch(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });
});