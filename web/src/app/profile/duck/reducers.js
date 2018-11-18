import {
  RECEIVE_USER_INFO_FAILURE,
  RECEIVE_USER_INFO_SUCCESS,
  REQUEST_USER_INFO,
  REQUEST_MY_DONATIONS,
  RECEIVE_MY_DONATIONS,
  REQUEST_MY_BOUNTIES,
  RECEIVE_MY_BOUNTIES,
} from './types';

const INITIAL_STATE = {
  showSpinner: false,
  redirect: false,
  isLoggedIn: false,
  donations: [],
  bounties: [],
  totalBounties: 0,
  totalDonations: 0
};

/* global sessionStorage */
const profileReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case REQUEST_MY_BOUNTIES:
    case REQUEST_MY_DONATIONS:
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
    case RECEIVE_MY_DONATIONS: {
      return {
        ...state,
        showSpinner: false,
        donations: action.data.content,
        totalDonations: action.data.totalElements
      };
    }
    case RECEIVE_MY_BOUNTIES: {
      return {
        ...state,
        showSpinner: false,
        bounties: action.data.content,
        totalBounties: action.data.totalElements
      }
    }
    default: {
      return state;
    }
  }
};

export default profileReducer;
