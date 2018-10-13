import {
  RECEIVE_USER_INFO_FAILURE,
  RECEIVE_USER_INFO_SUCCESS,
  REQUEST_USER_INFO,
  RECEIVE_OPEN_CONTRACTS
} from './types';

const INITIAL_STATE = {
  showSpinner: false,
  redirect: false,
  isLoggedIn: false,
  openContracts: [],
  totalOpenDonations: 0
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
  case RECEIVE_OPEN_CONTRACTS: {
    return {
      ...state,
      openContracts: action.data.content,
      totalOpenDonations: action.data.totalElements
    };
  }
  default: {
    return state;
  }
  }
};

export default profileReducer;
