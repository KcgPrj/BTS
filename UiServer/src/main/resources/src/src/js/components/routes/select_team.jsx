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
            teams = this.props.teams.map(teamName => {
                // return <li key={teamName}><Link to={`team/${teamName}`}>{teamName}</Link></li>;
                return <li key={teamName}><Link to={`sample-route`}>{teamName}</Link></li>;
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
        this.props.onClickCreateTeamButton();
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
    teams: PropTypes.array.isRequired,
    onClickCreateTeamButton: PropTypes.func.isRequired,
};

export const SelectTeam = connect(state => {
    return {
        teams: state.currentPage.teams,
    };
}, dispatch => {
    return {
        onClickCreateTeamButton: () => {
            dispatch(createTeam());
        },
    };
})(SelectTeamComponent);