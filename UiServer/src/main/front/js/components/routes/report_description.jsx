import React, {PropTypes} from 'react';
import {connect} from 'react-redux';

class ProductDescriptionComponent extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const {product} = this.props;
        return (
            <div>
                {product &&
                <div>
                    <input type="text" value={product.productName}/>
                    <div>Token: {product.token}</div>
                    <button>Update</button>
                    <button>Delete</button>
                </div>
                }
            </div>
        );
    }
}

export const ProductDescription = connect(state => {
    return {
        product: state.currentPage.product,
    };
}, dispatch => {
    return {};
})(ProductDescriptionComponent);