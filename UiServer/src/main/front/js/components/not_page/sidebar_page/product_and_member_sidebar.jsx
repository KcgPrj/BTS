import React from 'react';
import {Link} from 'react-router';

import {SidebarPage} from './base/sidebar_page.jsx';

// サイドバーにProductとMemberを表示するページ
export class ProductAndMemberSidebar extends React.Component {
    constructor(props) {
        super(props);

        this.handleClickNewProduct = this.handleClickNewProduct.bind(this);
    }

    handleClickNewProduct() {
        const newProductName = 'new_product' + Math.floor(Math.random() * 10000);
        this.props.onClickCreateProductButton(this.props.teamId, newProductName);
    }

    /**
     * サイドバーに表示されるHTML
     * @returns {XML}
     */
    sidebarContents() {
        let products = [];
        if (this.props.products) {
            products = this.props.products.map(p => {
                return (
                    <li key={p.productId} className="li-count">
                        <Link to={`${this.props.teamId}/${p.productId}`}>{p.productName}</Link>
                    </li>
                );
            });
        }

        let member = [];
        if (this.props.member) {
            member = this.props.member.map(m => {
                //TODO: リンクを適切に書き換える
                return (
                    <li key={m.id}>
                        <Link to={`/`}>{m.name}</Link>
                        <div className="mem_border"></div>
                    </li>
                );
            });
        }

        return (
            <div>
                <div id="products">
                    <div className="side_title ml50 mt30">
                        <label htmlFor="Panel1">
                            PRODUCT
                            <img src="assets/img/arrow.png" alt="展開"/>
                        </label>
                        <a onClick={this.handleClickNewProduct}>
                            <span>＋</span>
                        </a>
                    </div>
                    <input type="checkbox" id="Panel1" className="on-off"/>
                    <ol className="p_list">
                        {products}
                    </ol>
                </div>

                <div id="members">
                    <div className="side_title ml50 mt30">
                        <label htmlFor="Panel2" className="member_label">
                            MEMBER
                            <img src="assets/img/arrow.png" alt="展開"/>
                        </label>
                        <span>＋</span>
                    </div>
                    <input type="checkbox" id="Panel2" className="on-off"/>
                    <ol className="m_list">
                        {member}
                    </ol>
                </div>

            </div>
        );
    }

    render() {
        const sidebar = this.sidebarContents();
        return (
            <SidebarPage sidebarContents={sidebar}>
                {this.props.children}
            </SidebarPage>
        );
    }
}

ProductAndMemberSidebar.propTypes = {
    // チームID
    teamId: React.PropTypes.string.isRequired,
    products: React.PropTypes.array,
    member: React.PropTypes.array,
    onClickCreateProductButton: React.PropTypes.func.isRequired,
    children: React.PropTypes.node.isRequired,
};