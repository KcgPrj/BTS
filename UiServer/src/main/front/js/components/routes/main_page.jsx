import React, {PropTypes} from 'react';
import {connect} from 'react-redux';

import {ProductAndMemberSidebar} from '../not_page/sidebar_page/product_and_member_sidebar.jsx';
import {createProduct} from '../../actions/main_page.js';

export class MainPageComponent extends React.Component {
    render() {
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
                        <input type="radio" name="nav" id="one" className="tab_input"/>
                        <label htmlFor="one">PRODUCT TOKEN</label>

                        <input type="radio" name="nav" id="two" className="tab_input"/>
                        <label htmlFor="two">REPORT</label>

                        <article className="content one">
                            <h1>Token</h1>
                        </article>
                        <article className="content two">

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
                                        <div className="t_title">Hogehogehoge</div>
                                        <div className="t_data">
                                            <table>
                                                <tbody>
                                                <tr>
                                                    <th>yuusendo</th>
                                                    <th>tantou</th>
                                                    <th>2016/07/12</th>
                                                    <th>joutai</th>
                                                    <th>version</th>
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
    createProduct: PropTypes.func.isRequired,
};

export const MainPage = connect(state => {
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
})(MainPageComponent);