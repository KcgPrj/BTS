import configureMockStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import nock from 'nock';
import expect from 'expect';

import * as actions from '../../../../../../main/front/js/actions/api/product.js';
import {createNock} from '../nock_utils.js';

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);

const DUMMY_PAGE = 'DUMMY_PAGE';
const TEAM1 = 'TEAM1';
const PRODUCT1 = 'PRODUCT1';

describe('products api actions', () => {
    afterEach(() => {
        nock.cleanAll();
    });

    it(`creates ${actions.FETCH_PRODUCTS_SUCCESS} when fetching products has been done`, () => {
        const responseBody = [{
            "productId": 1,
            "productName": "PRODUCT1",
            "token": "2bdf0f49-55b4-4275-92d9-f4a9890ef35c",
        }];

        createNock('http://localhost:18080', `/${TEAM1}/products/show`)
            .get(`/${TEAM1}/products/show`)
            .reply(200, responseBody);

        const expectedActions = [
            {page: DUMMY_PAGE, type: actions.FETCH_PRODUCTS_REQUEST},
            {
                page: DUMMY_PAGE,
                type: actions.FETCH_PRODUCTS_SUCCESS,
                data: responseBody,
            },
        ];
        const store = mockStore({currentPage: {}});

        return store.dispatch(actions.fetchProducts(DUMMY_PAGE, TEAM1))
            .then(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it(`creates ${actions.FETCH_PRODUCTS_FAILURE} when fetching products has been failed`, () => {
        createNock('http://localhost:18080', `/${TEAM1}/products/show`)
            .get(`/${TEAM1}/products/show`)
            .reply(401);

        const expectedActions = [
            {page: DUMMY_PAGE, type: actions.FETCH_PRODUCTS_REQUEST},
            {
                page: DUMMY_PAGE,
                type: actions.FETCH_PRODUCTS_FAILURE,
                error: new Error(401),
            },
        ];
        const store = mockStore({currentPage: {}});

        return store.dispatch(actions.fetchProducts(DUMMY_PAGE, TEAM1))
            .catch(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it(`creates ${actions.CREATE_PRODUCT_SUCCESS} when creating product has been done`, () => {
        const responseBody = {
            "teamId": "TEAM1",
            "teamName": "TEAM1",
            "owner": {
                "id": 1, "name": "myname", "type": "GITHUB",
            },
            "member": [{"id": 1, "name": "myname", "type": "GITHUB"}],
            "product": [{"productId": 1, "productName": "PRODUCT1", "token": "93baf4f9-0b3e-4d2a-905f-fa28f8b8e7c2"}],
        };

        createNock('http://localhost:18080', `/${TEAM1}/products/create`)
            .post(`/${TEAM1}/products/create`)
            .reply(200, responseBody);

        const expectedActions = [
            {page: DUMMY_PAGE, type: actions.CREATE_PRODUCT_REQUEST},
            {
                page: DUMMY_PAGE,
                type: actions.CREATE_PRODUCT_SUCCESS,
                data: responseBody,
            },
        ];
        const store = mockStore({currentPage: {}});

        return store.dispatch(actions.createProduct(DUMMY_PAGE, TEAM1, PRODUCT1))
            .then(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });

    it(`creates ${actions.CREATE_PRODUCT_FAILURE} when creating product has been failed`, () => {
        createNock('http://localhost:18080', `/${TEAM1}/products/create`)
            .post(`/${TEAM1}/products/create`)
            .reply(401, {});

        const expectedActions = [
            {page: DUMMY_PAGE, type: actions.CREATE_PRODUCT_REQUEST},
            {
                page: DUMMY_PAGE,
                type: actions.CREATE_PRODUCT_FAILURE,
                error: new Error(401),
            },
        ];
        const store = mockStore({currentPage: {}});

        return store.dispatch(actions.createProduct(DUMMY_PAGE, TEAM1, PRODUCT1))
            .catch(() => {
                expect(store.getActions()).toEqual(expectedActions);
            });
    });
});