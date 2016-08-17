import React, {PropTypes} from 'react';

import {Contents} from './contents.jsx';
import {Sidebar} from './sidebar.jsx';

/**
 * サイドバーのあるページ
 */
export class SidebarPage extends React.Component {
    render() {
        return (
            <div id="wrapper" className="pst-a">
                <Sidebar>
                    {this.props.sidebarContents}
                </Sidebar>
                <Contents>
                    {this.props.mainContents}
                </Contents>
            </div>
        );
    }
}

SidebarPage.propTypes = {
    sidebarContents: PropTypes.node.isRequired,
    mainContents: PropTypes.node.isRequired,
};