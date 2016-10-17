import React, {PropTypes} from 'react';

import {Contents} from './contents.jsx';
import {Sidebar} from './sidebar.jsx';

/**
 * サイドバーのあるページ
 */
export class SidebarPage extends React.Component {
    render() {
        return (
            <div id="wrapper" className="pst-a h100p w100p">
                <Sidebar>
                    {this.props.sidebarContents}
                </Sidebar>
                <Contents>
                    {this.props.children}
                </Contents>
            </div>
        );
    }
}

SidebarPage.propTypes = {
    sidebarContents: PropTypes.node.isRequired,
    children: PropTypes.node.isRequired,
};