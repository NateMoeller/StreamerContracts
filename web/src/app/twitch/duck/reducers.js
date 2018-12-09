import {
  REQUEST_TOP_GAMES,
  RECEIVE_TOP_GAMES
} from './types';

const INITIAL_STATE = {
  loading: false,
  topGames: []
};

const twitchReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case REQUEST_TOP_GAMES: {
      return {
        ...state,
        loading: true,
      }
    }
    case RECEIVE_TOP_GAMES: {
      return {
        ...state,
        loading: false,
        topGames: action.games
      }
    }
    default: {
      return state;
    }
  }
}

export default twitchReducer;
