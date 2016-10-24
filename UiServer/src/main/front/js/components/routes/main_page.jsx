import React, {PropTypes} from 'react';
import {ProductAndMemberSidebar} from '../not_page/sidebar_page/product_and_member_sidebar.jsx';

export class MainPage extends React.Component {
  render() {
    return (
      <div>
        <ProductAndMemberSidebar products={this.props.products} member={this.props.member}>
          {/*<!--main content-->*/}
                <div id="main" className="fr h100p w100p">
                    <div className="main_inner tal h100p">
                        {/*<!--<div className="plz_select">Please select Product.</div>-->*/}
                        
                        
                        
                        
                        {/*<!--チケット一覧、プロダクトトークン(タブメニュー)要素ここから-->*/}
                        
                        
                        
                        
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
                                            <input type="search" className="search_box" />
                                        </th>
                                        <th>
                                            <a href="">
                                               <span className="search_btn dib">
                                                   <img src="assets/img/search.png" alt="検索" />
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
                                                            <img className="fl" src="assets/img/comment2.png" alt="コメント数"  />
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
                        
                        
                        
                        
                        
                        
                    </div>
                </div>
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