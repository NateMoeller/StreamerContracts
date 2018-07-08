import { combineReducers } from 'redux';
import homeReducer from './app/home/duck';
import profileReducer from './app/profile/duck';

// add reducers here
const rootReducer = combineReducers({
  home: homeReducer,
  profile: profileReducer
});

export default rootReducer;
