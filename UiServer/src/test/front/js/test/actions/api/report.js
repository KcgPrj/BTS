import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import nock from 'nock';
import expect from 'expect';

import * as Actions from '../../../../../../main/front/js/actions/api/report.js';
import {createNock} from '../nock_utils.js';

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);

const DUMMY_PAGE = 'DUMMY_PAGE';
const PRODUCT1_ID = 1;

describe('reports api actions', () => {
    afterEach(() => {
        nock.cleanAll();
    });

    it(`creates ${Actions.FETCH_REPORTS_SUCCESS} when fetching reports has been done`, () => {
        const responseBody = [
            {
                "reportId": 1,
                "title": "hogehoge",
                "description": "",
                "createdAt": "2016-12-05T12:01:12.628+09:00",
                "assign": {
                    "id": 1,
                    "name": "nosoosso",
                    "type": "GITHUB"
                },
                "version": "",
                "stacktrace": "",
                "log": "",
                "runtimeInfo": "",
                "product": {
                    "productId": PRODUCT1_ID,
                    "productName": "new_product2449",
                    "token": "26d054e1-f197-4f3a-bfe9-6c18cbd5d65a"
                },
                "state": "opened"
            }
        ];

        createNock('http://localhost:18080', '/report/list')
            .get(`/report/list?productId=${PRODUCT1_ID}`)
            .reply(200, responseBody);

        const expectedActions = [
            {page: DUMMY_PAGE, type: Actions.FETCH_REPORTS_REQUEST},
            {
                page: DUMMY_PAGE,
                type: Actions.FETCH_REPORTS_SUCCESS,
                data: responseBody,
            },
        ];
        const store = mockStore({currentPage: {}});

        return store.dispatch(Actions.fetchReports(DUMMY_PAGE, PRODUCT1_ID))
            .then(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it(`creates ${Actions.FETCH_REPORTS_FAILURE} when fetching products has been failed`, () => {
        createNock('http://localhost:18080', '/report/list')
            .get(`/report/list?productId=${PRODUCT1_ID}`)
            .reply(401);

        const expectedActions = [
            {page: DUMMY_PAGE, type: Actions.FETCH_REPORTS_REQUEST},
            {
                page: DUMMY_PAGE,
                type: Actions.FETCH_REPORTS_FAILURE,
                error: new Error(401),
            },
        ];
        const store = mockStore({currentPage: {}});

        return store.dispatch(Actions.fetchReports(DUMMY_PAGE, PRODUCT1_ID))
            .catch(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });
});