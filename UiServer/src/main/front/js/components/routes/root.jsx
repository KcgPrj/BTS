import React from 'react';

export class Root extends React.Component {
    render() {
        return (
            <div>
                {/*背景*/}
                <div className="full_bg pst-f">
                    <div className="polygon_img">
                        <img src="/assets/img/Polygon_bg.png" alt="polygon_bg"/>
                    </div>
                </div>
                {this.props.children}
            </div>
        );
    }
}