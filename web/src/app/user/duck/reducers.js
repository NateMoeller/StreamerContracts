import {
  REQUEST_PUBLIC_USER,
  RECEIVE_PUBLIC_USER_SUCCESS,
  RECEIVE_PUBLIC_USER_FAILURE,
  REQUEST_PUBLIC_BOUNTIES,
  RECEIVE_PUBLIC_BOUNITES_SUCCESS
} from './types';

const INITIAL_STATE = {
  getPublicUserLoading: false,
  publicUser: null,
  publicBountiesLoading: false,
  publicBounties: [],
  totalPublicBounties: 0
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
    case RECEIVE_PUBLIC_BOUNITES_SUCCESS: {
      return {
        ...state,
        publicBounties: action.data.content,
        totalPublicBounties: action.data.totalElements
      }
    }
    default: {
      return state;
    }
  }
}

export default commonReducer;
