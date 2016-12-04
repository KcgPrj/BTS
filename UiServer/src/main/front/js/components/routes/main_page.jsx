import React, {PropTypes} from 'react';
import {connect} from 'react-redux';

import {ProductAndMemberSidebar} from '../not_page/sidebar_page/product_and_member_sidebar.jsx';
import * as Actions from '../../actions/main_page.js';

export class MainPageComponent extends React.Component {
    render() {
        const currentTab = this.props.currentTab;
        const thisProduct = this.props.products.find(i => {
            return i.productName === this.props.params.productId;
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

                                {/*<!--チケットここから-->*/}
                                <a href="">
                                    <div className="ticket">
                                        <div className="t_title">登録できない</div>
                                        <div className="t_data">
                                            <table>
                                                <tbody>
                                                <tr>
                                                    <th>Blocker</th>
                                                    <th>山田 太郎</th>
                                                    <th>2016/07/12</th>
                                                    <th>Open</th>
                                                    <th>0.1.0</th>
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
                                <a href="">
                                    <div className="ticket">
                                        <div className="t_title">チケット追加に失敗する</div>
                                        <div className="t_data">
                                            <table>
                                                <tbody>
                                                <tr>
                                                    <th>Major</th>
                                                    <th>佐藤 二郎</th>
                                                    <th>2016/11/13</th>
                                                    <th>Closed</th>
                                                    <th>1.2.1</th>
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
                                <a href="">
                                    <div className="ticket">
                                        <div className="t_title">Hogehogehoge</div>
                                        <div className="t_data">
                                            <table>
                                                <tbody>
                                                <tr>
                                                    <th>Blocker</th>
                                                    <th>山田 太郎</th>
                                                    <th>2016/07/12</th>
                                                    <th>Open</th>
                                                    <th>0.1.0</th>
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
                                {/*<!--ここまで-->*/}

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
    currentTab: PropTypes.string.isRequired,
    createProduct: PropTypes.func.isRequired,
    showProductTokenTab: PropTypes.func.isRequired,
    showReportTab: PropTypes.func.isRequired,
};

export const MainPage = connect(state => {
    return {
        products: state.currentPage.products,
        member: state.currentPage.member,
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