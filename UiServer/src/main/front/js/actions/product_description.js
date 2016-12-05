import {PAGE_PRODUCT_DESCRIPTION_PAGE} from '../pages.js';
import {fetchOneProduct} from './api/product.js';

export function initProductDescriptionPage(teamId, productId) {
    return dispatch => {
        dispatch(fetchOneProduct(PAGE_PRODUCT_DESCRIPTION_PAGE, teamId, productId))
            .catch(console.log);
    };
}

export {
    FETCH_ONE_PRODUCT_REQUEST,
    FETCH_ONE_PRODUCT_SUCCESS,
    FETCH_ONE_PRODUCT_FAILURE,
} from './api/product.js';