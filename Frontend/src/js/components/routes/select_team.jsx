import React, {PropTypes} from 'react';
import {connect} from 'react-redux';
import {Link} from 'react-router';

import {createTeam} from '../../actions/select_team.js';

import {SidebarPage} from '../not_page/sidebar_page/sidebar_page.jsx';


class SelectTeamComponent extends React.Component {
    constructor(props) {
        super(props);
        this.onClick = this.onClick.bind(this);
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
                    <a href="" onClick={this.onClick}>
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

    /**
     * チーム追加ボタンを押したとき
     * @param e
     */
    onClick(e) {
        e.preventDefault();
        const teamId = 'new team' + Math.random();
        this.props.onClickCreateTeamButton(teamId);
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
SelectTeamComponent.propTypes = {
    teams: PropTypes.array,
    onClickCreateTeamButton: PropTypes.func,
};

export const SelectTeam = connect(state => {
    return {
        teams: state.currentPage.teams,
    };
}, dispatch => {
    return {
        onClickCreateTeamButton: (teamId) => {
            dispatch(createTeam(teamId));
        },
    };
})(SelectTeamComponent);