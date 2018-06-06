import { combineReducers } from 'redux';
import  homeReducer  from './app/home/duck/reducers';

// add reducers here
const rootReducer = combineReducers({
  home: homeReducer
});

export default rootReducer;
