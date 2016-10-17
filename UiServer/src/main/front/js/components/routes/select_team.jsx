import React, {PropTypes} from 'react';
import {connect} from 'react-redux';

import {createTeam} from '../../actions/select_team.js';

import {TeamSidebar} from '../not_page/sidebar_page/team_sidebar.jsx';


class SelectTeamComponent extends React.Component {
    render() {
        return (
            <div>
                <TeamSidebar teams={this.props.teams} onClickCreateTeamButton={this.props.onClickCreateTeamButton}>
                    <div className="plz_select">Please select team.</div>
                </TeamSidebar>
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