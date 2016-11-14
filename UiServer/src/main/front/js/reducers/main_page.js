import * as Actions from '../actions/main_page.js';

const initialState = {
    products: [],
    member: [],
};

const selectTeam = (state = initialState, action) => {
    console.log(action);
    switch (action.type) {
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