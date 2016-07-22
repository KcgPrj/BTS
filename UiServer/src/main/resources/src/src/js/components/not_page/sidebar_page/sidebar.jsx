import React from 'react';

export class Sidebar extends React.Component {
    render() {
        return (
            <div>
                <div id="side" className="pst-f">
                    <div className="side_inner">
                        <div className="site_logo">
                            <img src="/assets/img/site_logo.png" alt="Site_logo"/>
                        </div>
                        {/*ロゴ下ボーダー*/}
                        <div className="border"></div>

                        {this.props.children}
                    </div>
                </div>
            </div>
        );
    }
}