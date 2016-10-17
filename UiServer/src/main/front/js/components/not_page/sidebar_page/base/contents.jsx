import React from 'react';


export class Contents extends React.Component {
    render() {
        return (
                <div id="main"  className="fr h100p w100p">
                    <div className="main_inner tal h100p">
                        {this.props.children}
                    </div>
                </div>
        );
    }
}