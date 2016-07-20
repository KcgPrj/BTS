import React from 'react';
import ReactDOM from 'react-dom';
import {createStore, combineReducers} from 'redux';
import {Provider} from 'react-redux';
import {Router, Route, browserHistory, useRouterHistory} from 'react-router';
import {syncHistoryWithStore, routerReducer} from 'react-router-redux';
import {createHistory} from 'history';

import reducers from './reducers/reducers.js';

import {Root} from './components/routes/root.jsx';
import {SelectTeam} from './components/routes/select_team.jsx';

const store = createStore(
    combineReducers({
        ...reducers,
        routing: routerReducer
    })
);

const historyWithBasename = useRouterHistory(createHistory)({
    basename: '/react_index.html',
});
const history = syncHistoryWithStore(historyWithBasename, store);

ReactDOM.render(
    <Provider store={store}>
        <Router history={history}>
            <Route path="/" component={Root}>
                <Route path="select-team" component={SelectTeam}/>
            </Route>
        </Router>
    </Provider>,
    document.getElementById('root')
);

