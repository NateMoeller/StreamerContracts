import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';

import logger from 'redux-logger';
import thunk from 'redux-thunk';
import { createStore, applyMiddleware } from 'redux';
import rootReducer from './reducers';

import './index.css';
import App from './app/App';
import registerServiceWorker from './registerServiceWorker';

const middleware = applyMiddleware(thunk, logger);
const store = createStore(rootReducer, middleware);

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
  document.getElementById('root')
);
registerServiceWorker();
