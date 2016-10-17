import React from 'react';


export class Contents extends React.Component {
    render() {
        return (
                <div id="main">
                    <div className="main_inner">
                        {this.props.children}
                    </div>
                </div>
        );
    }
}