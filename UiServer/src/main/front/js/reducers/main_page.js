import * as Actions from '../actions/main_page.js';

const initialState = {
    products: [],
    member: [],
    currentTab: 'productToken',
};

const selectTeam = (state = initialState, action) => {
    console.log(action);
    switch (action.type) {
        case Actions.INIT_STATE: {
            return initialState;
        }
        case Actions.SHOW_PRODUCT_TOKEN_TAB: {
            const newState = {
                ...state,
                currentTab: 'productToken',
            };
            return newState;
        }
        case Actions.SHOW_REPORT_TAB: {
            const newState = {
                ...state,
                currentTab: 'report',
            };
            return newState;
        }
        case Actions.FETCH_PRODUCTS_SUCCESS: {
            const newState = {
                ...state,
                products: action.data,
            };
            return newState;
        }
        case Actions.FETCH_MEMBER_SUCCESS: {
            const newState = {
                ...state,
                member: action.data,
            };
            return newState;
        }
        default:
            return state;
    }
};

export default selectTeam;