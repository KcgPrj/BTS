import React, {PropTypes} from 'react';
import {Link} from 'react-router';
import {SidebarPage} from '../not_page/sidebar_page/sidebar_page.jsx';

export class TeamPage extends React.Component {

    constructor(props) {
        super(props);

        this.sidebarContents = this.sidebarContents.bind(this);
        this.mainContents = this.mainContents.bind(this);
    }

    /**
     * サイドバーに表示されるHTML
     * @returns {XML}
     */
    sidebarContents() {
        let products = [];
        if (this.props.products) {
            products = this.props.products.map(p => {
                //TODO: リンクを適切に書き換える
                return (
                    <li key={p.productId} className="li-count"><Link to={`/`}>{p.productName}</Link></li>
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
                        <a href="">
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

    /**
     * ページのメインコンテンツ
     * @returns {XML}
     */
    mainContents() {
        return (
            <div className="plz_select">Please select team.</div>
        );
    }

    render() {
        const sidebarContents = this.sidebarContents();
        const mainContents = this.mainContents();
        return (
            <div>
                <SidebarPage sidebarContents={sidebarContents} mainContents={mainContents}/>
            </div>
        );
    }
}

TeamPage.propTypes = {
    //たぶん/teamId/products/showが使われる？
    // sample: [{"productId":1,"productName":"name","token":"17b60fff-c2cf-4d35-96b4-f81832f4e30d"}]
    products: PropTypes.array,
    //たぶん/team/member/showが使われる？
    // sample: [{"id":1,"name":"user1","type":"GITHUB"}]
    member: PropTypes.array,
};

// ダミーデータ
TeamPage.defaultProps = {
    products: [
        {"productId": 1, "productName": "name", "token": "17b60fff-c2cf-4d35-96b4-f81832f4e30d"},
        {"productId": 2, "productName": "name2", "token": "17b60fff-c2cf-4d35-96b4-222222222222"},
    ],
    member: [
        {"id": 1, "name": "user1", "type": "GITHUB"},
        {"id": 2, "name": "user2", "type": "GITHUB"},
    ],
};