import React from 'react';
import {connect} from 'react-redux';

class SampleRouteComponent extends React.Component {
    render() {
        console.log('state: ', this.props.data);
        return (
            <div>This is sample route.</div>
        );
    }
}

export const SampleRoute = connect(state => {
    return {
        data: state,
    };
})(SampleRouteComponent);