import React from 'react';
import {Link} from 'react-router';

import {SidebarPage} from './base/sidebar_page.jsx';

// サイドバーにチーム一覧を表示するページ
export class TeamSidebar extends React.Component {
    constructor(props) {
        super(props);

        this.addTeam = this.addTeam.bind(this);
        this.sidebarContents = this.sidebarContents.bind(this);
    }

    /**
     * チーム追加ボタンを押したとき
     * @param e
     */
    addTeam(e) {
        e.preventDefault();
        const teamId = 'new team' + Math.random();
        this.props.onClickCreateTeamButton(teamId);
    }

    /**
     * サイドバーに表示されるHTML
     * @returns {XML}
     */
    sidebarContents() {
        // チームのリストをli要素に変換
        let teams = [];
        if (this.props.teams) {
            teams = this.props.teams.map(({teamId}) => {
                // return <li key={teamId}><Link to={`team/${teamId}`}>{teamId}</Link></li>;
                return <li key={teamId}><Link to={`sample-route`}>{teamId}</Link></li>;
            });
        }

        return (
            <div>
                <div className="side_title ml50 mt30">
                    <label htmlFor="">TEAM</label>
                    <a href="" onClick={this.addTeam}>
                        <span>＋</span>
                    </a>
                </div>
                <div id="teams">
                    <ul className="t_list">
                        {teams}
                    </ul>
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

TeamSidebar.propTypes = {
    teams: React.PropTypes.array.isRequired,
    children: React.PropTypes.node.isRequired,
    onClickCreateTeamButton: React.PropTypes.func.isRequired,
};