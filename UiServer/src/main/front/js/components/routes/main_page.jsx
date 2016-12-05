import React, {PropTypes} from 'react';
import {connect} from 'react-redux';
import moment from 'moment';

import {ProductAndMemberSidebar} from '../not_page/sidebar_page/product_and_member_sidebar.jsx';
import * as Actions from '../../actions/main_page.js';

export class MainPageComponent extends React.Component {
    constructor(props) {
        super(props);

        this.genReportTickets = this.genReportTickets.bind(this);
    }

    genReportTickets() {
        if (!this.props.reports) {
            return false;
        }
        return this.props.reports.map(i => {
            return (
                <a href="" key={i.reportId}>
                    <div className="ticket">
                        <div className="t_title">{i.title}</div>
                        <div className="t_data">
                            <table>
                                <tbody>
                                <tr>
                                    <th>{i.assign.name}</th>
                                    <th>{moment(i.createdAt).format('YYYY-MM-DD HH:mm:ss')}</th>
                                    <th>{i.state}</th>
                                    <th>{i.version}</th>
                                    <th>
                                        <div className="comment">
                                            <img className="fl" src="assets/img/comment2.png"
                                                 alt="コメント数"/>
                                            <span>6</span>
                                        </div>
                                    </th>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div className="t_border"></div>
                    </div>
                </a>
            );
        });
    }

    render() {
        const currentTab = this.props.currentTab;
        const thisProduct = this.props.products.find(i => {
            return i.productId === parseInt(this.props.params.productId);
        });

        return (
            <div>
                <ProductAndMemberSidebar
                    teamId={this.props.params.teamId}
                    products={this.props.products}
                    member={this.props.member}
                    onClickCreateProductButton={this.props.createProduct}
                >
                    {/*<!--main content-->*/}

                    {/*<!-- TAB CONTROLLERS -->*/}
                    <div id="tab_menu">
                        <label onClick={this.props.showProductTokenTab}
                               className={currentTab === 'productToken' && 'active-tab'}>PRODUCT TOKEN</label>
                        <label onClick={this.props.showReportTab}
                               className={currentTab === 'report' && 'active-tab'}>REPORT</label>

                        {currentTab === 'productToken' &&
                        <article className="content">
                            <h1>{thisProduct ? thisProduct.token : ''}</h1>
                        </article>
                        }
                        {currentTab === 'report' &&
                        <article className="content">

                            <div id="search">
                                <table>
                                    <tbody>
                                    <tr>
                                        <th>
                                            <span>Search </span>
                                        </th>
                                        <th>
                                            <input type="search" className="search_box"/>
                                        </th>
                                        <th>
                                            <a href="">
                                               <span className="search_btn dib">
                                                   <img src="assets/img/search.png" alt="検索"/>
                                               </span>
                                            </a>
                                        </th>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                            <div className="ticket_area m0a">

                                {this.genReportTickets()}

                            </div>
                        </article>
                        }
                    </div>
                    {/*<!--チケット一覧、プロダクトトークン(タブメニュー)-->*/}
                </ProductAndMemberSidebar>
            </div>
        );
    }
}

MainPageComponent.propTypes = {
    products: PropTypes.array,
    member: PropTypes.array,
    reports: PropTypes.array,
    currentTab: PropTypes.string.isRequired,
    createProduct: PropTypes.func.isRequired,
    showProductTokenTab: PropTypes.func.isRequired,
    showReportTab: PropTypes.func.isRequired,
};

export const MainPage = connect(state => {
    return {
        products: state.currentPage.products,
        member: state.currentPage.member,
        reports: state.currentPage.reports,
        currentTab: state.currentPage.currentTab,
    };
}, dispatch => {
    return {
        showProductTokenTab: () => {
            dispatch(Actions.showProductTokenTab());
        },
        showReportTab: () => {
            dispatch(Actions.showReportTab());
        },
        createProduct: (teamId, productName) => {
            dispatch(Actions.createProduct(teamId, productName));
        },
    };
})(MainPageComponent);