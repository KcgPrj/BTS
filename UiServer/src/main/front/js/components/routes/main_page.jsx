import React, {PropTypes} from 'react';
import {ProductAndMemberSidebar} from '../not_page/sidebar_page/product_and_member_sidebar.jsx';

export class MainPage extends React.Component {
  render() {
    return (
      <div>
        <ProductAndMemberSidebar products={this.props.products} member={this.props.member}>
          <div className="plz_select">hogehoge</div>
        </ProductAndMemberSidebar>
      </div>
    );
  }
}

MainPage.propTypes = {
  products: PropTypes.array,
  member: PropTypes.array,
};

// ダミーデータ
MainPage.defaultProps = {
  products: [
    {"productId": 1, "productName": "name", "token": "17b60fff-c2cf-4d35-96b4-f81832f4e30d"},
    {"productId": 2, "productName": "name2", "token": "17b60fff-c2cf-4d35-96b4-222222222222"},
  ],
  member: [
    {"id": 1, "name": "user1", "type": "GITHUB"},
    {"id": 2, "name": "user2", "type": "GITHUB"},
  ],
};