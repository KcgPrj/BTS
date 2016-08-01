import React from 'react';
import {Route, Link} from 'react-router';

import {clearCurrentPageState} from '../../actions/route_action.js';

// onEnterとonLeaveを自動で設定するRouteを作ったが上手く動かなかったので
// とりあえずコメントアウト

// class MyRoute extends React.Component {
//     constructor(props) {
//         super(props);
//         this.clearState = this.clearState.bind(this);
//         this.init = this.init.bind(this);
//     }
//
//     /**
//      * storeに保存されたページのstateを消す
//      */
//     clearState() {
//         this.context.store.dispatch(clearCurrentPageState());
//     }
//
//     /**
//      * ページの初期化処理(ajaxによるデータ取得など)
//      */
//     init() {
//         console.log('aa init');
//         if (this.props.initialize) {
//             this.context.store.dispatch(this.props.initialize);
//         }
//     }
//
//     render() {
//         return <Route {...this.props} onEnter={this.init} onLeave={this.clearState}/>;
//     }
// }
// MyRoute.propTypes = {
//     initialize: React.PropTypes.object,
// };
// MyRoute.contextTypes = {
//     store: React.PropTypes.object,
// };

const MyLink = ({...props})=> {
    return <Link {...props} />;
};

export {MyLink};