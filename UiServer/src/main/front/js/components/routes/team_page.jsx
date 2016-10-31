import React, {PropTypes} from 'react';
import {connect} from 'react-redux';

import {ProductAndMemberSidebar} from '../not_page/sidebar_page/product_and_member_sidebar.jsx';
import {createProduct} from '../../actions/team_page.js';

class TeamPageComponent extends React.Component {
    render() {
        return (
            <div>
                <ProductAndMemberSidebar
                    teamId={this.props.params.teamId}
                    products={this.props.products}
                    member={this.props.member}
                    onClickCreateProductButton={this.props.createProduct}
                >
                    <div className="plz_select">hogehoge</div>
                </ProductAndMemberSidebar>
            </div>
        );
    }
}

TeamPageComponent.propTypes = {
    // sample: [{"productId":1,"productName":"name","token":"17b60fff-c2cf-4d35-96b4-f81832f4e30d"}]
    products: PropTypes.array,
    // sample: [{"id":1,"name":"user1","type":"GITHUB"}]
    member: PropTypes.array,
    createProduct: PropTypes.func.isRequired,
};

export const TeamPage = connect(state => {
    return {
        products: state.currentPage.products,
        member: state.currentPage.member,
    };
}, dispatch => {
    return {
        createProduct: (teamId, productName) => {
            dispatch(createProduct(teamId, productName));
        },
    };
})(TeamPageComponent);