import * as Actions from '../actions/product_description.js';

const initialState = {
    product: {},
};

const productDescription = (state = initialState, action) => {
    console.log(action);
    switch (action.type) {
        case Actions.FETCH_ONE_PRODUCT_SUCCESS: {
            const newState = {
                ...state,
                product: action.data,
            };
            return newState;
        }
        default:
            return state;
    }
};

export default productDescription;