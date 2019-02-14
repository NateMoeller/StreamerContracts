import {
  RECEIVE_HOME_BOUNTIES
} from './types';

const INITIAL_STATE = {
  bounties: [],
  totalBounties: 0
};

const homeReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case RECEIVE_HOME_BOUNTIES: {
      console.log('RECEIVE HOME BOUNTIES');
      return {
        ...state,
        bounties: action.bounties.content,
        totalBounties: action.bounties.totalElements
      };
    }
    default:
      return state;
    }
};

export default homeReducer;
