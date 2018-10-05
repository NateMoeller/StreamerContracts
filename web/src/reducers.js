import { combineReducers } from 'redux';
import publicUserReducer from './app/user/duck';
import donateReducer from './app/donate/duck';
import homeReducer from './app/home/duck';
import profileReducer from './app/profile/duck';

// add reducers here
const rootReducer = combineReducers({
  home: homeReducer,
  profile: profileReducer,
  publicUser: publicUserReducer,
  donate: donateReducer
});

export default rootReducer;
