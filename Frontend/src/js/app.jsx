import React from 'react';
import ReactDOM from 'react-dom';
import {createStore, combineReducers, applyMiddleware} from 'redux';
import thunk from 'redux-thunk';
import {Provider} from 'react-redux';
import {Router, Route, hashHistory} from 'react-router';
import {syncHistoryWithStore, routerReducer} from 'react-router-redux';

import {clearCurrentPageState} from './actions/route_action.js';
import {initSelectTeamPage} from './actions/select_team.js';
import reducers from './reducers/reducers.js';

import {Root} from './components/routes/root.jsx';
import {SelectTeam} from './components/routes/select_team.jsx';
import {SampleRoute} from './components/routes/sample_route.jsx'

const store = createStore(
    combineReducers({
        ...reducers,
        routing: routerReducer
    }),
    applyMiddleware(thunk)
);

const history = syncHistoryWithStore(hashHistory, store);

ReactDOM.render(
    <Provider store={store}>
        <Router history={history}>
            <Route path="/" component={Root}>
                <Route path="select-team" component={SelectTeam}
                       onEnter={() => store.dispatch(initSelectTeamPage())}
                       onLeave={() => store.dispatch(clearCurrentPageState())}/>
                <Route path="sample-route" component={SampleRoute}
                       onLeave={() => store.dispatch(clearCurrentPageState())}/>
            </Route>
        </Router>
    </Provider>,
    document.getElementById('root')
);

