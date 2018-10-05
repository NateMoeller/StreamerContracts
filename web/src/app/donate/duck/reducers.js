import * as types from './types';

const INITIAL_STATE = {
  curDonationState: null
};

const donateReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case types.REQUEST_INSERT_BOUNTY: {
      return {
        ...state,
        curDonationState: types.DONATION_PROCESSING
      }
    }
    case types.RECEIVE_INSERT_BOUNTY_SUCCESS: {
      return {
        ...state,
        curDonationState: types.DONATION_PROCESSED
      };
    }
    case types.RECEIVE_INSERT_BOUNTY_FAILURE: {
      return {
        ...state,
        curDonationState: types.DONATION_ERROR
      };
    }
    default: {
      return state;
    }
  }
}

export default donateReducer;
