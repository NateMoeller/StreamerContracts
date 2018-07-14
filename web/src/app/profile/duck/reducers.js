import { RECEIVE_USER_INFO_FAILURE, RECEIVE_USER_INFO_SUCCESS, REQUEST_USER_INFO } from './types';

const INITIAL_STATE = {
  showSpinner: false,
  redirect: false,
  isLoggedIn: false
};

/* global sessionStorage */
const profileReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
  case REQUEST_USER_INFO: {
    return {
      ...state,
      showSpinner: true
    };
  }
  case RECEIVE_USER_INFO_FAILURE: {
    sessionStorage.removeItem('user');

    return {
      ...state,
      showSpinner: false,
      redirect: true
    };
  }
  case RECEIVE_USER_INFO_SUCCESS: {
    sessionStorage.setItem('user', JSON.stringify(action.userInfo));

    return {
      ...state,
      showSpinner: false,
      isLoggedIn: true
    };
  }
  default: {
    return state;
  }
  }
};

export default profileReducer;
