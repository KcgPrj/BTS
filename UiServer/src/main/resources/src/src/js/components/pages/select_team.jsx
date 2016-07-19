import React from 'react';
import {connect} from 'react-redux';
import {Link} from 'react-router';

import {SidebarPage} from '../not_page/sidebar_page/sidebar_page.jsx';

class SelectTeamComponent extends React.Component {
    sidebarContents() {
        const teams = this.genTeamLi();
        return (
            <div>
                <div className="side_title ml50 mt30">
                    <label htmlFor="">TEAM</label>
                    <a href="">
                        <span>＋</span>
                    </a>
                </div>
                <div id="teams">
                    <ul className="t_list">
                        {teams}
                    </ul>
                </div>
            </div>
        )
    }

    genTeamLi() {
        return this.props.data.teams.map(teamName => {
            return <li key={teamName}><Link to={`team/${teamName}`}>{teamName}</Link></li>;
        });
    }

    mainContents() {
        return (
            <div className="plz_select">Please select team.</div>
        )
    };

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

export const SelectTeam = connect(state => {
    return {
        data: state.selectTeamPage
    }
})(SelectTeamComponent);