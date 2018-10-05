import {
  REQUEST_PUBLIC_USER,
  RECEIVE_PUBLIC_USER_SUCCESS,
  RECEIVE_PUBLIC_USER_FAILURE
} from './types';

const INITIAL_STATE = {
  getPublicUserLoading: false,
  publicUser: null
};

const commonReducer = (state = INITIAL_STATE, action) => {
  switch(action.type) {
    case REQUEST_PUBLIC_USER: {
      return {
        ...state,
        getPublicUserLoading: true
      }
    }
    case RECEIVE_PUBLIC_USER_SUCCESS: {
      return {
        ...state,
        getPublicUserLoading: false,
        publicUser: action.publicUser
      }
    }
    case RECEIVE_PUBLIC_USER_FAILURE: {
      return {
        ...state,
        getPublicUserLoading: false,
        publicUser: null
      }
    }
    default: {
      return state;
    }
  }
}

export default commonReducer;
