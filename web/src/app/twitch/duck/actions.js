import {
  REQUEST_TOP_GAMES,
  RECEIVE_TOP_GAMES
} from './types';

const requestTopGames = () => ({
  type: REQUEST_TOP_GAMES
});

const receiveTopGames = (games) => ({
  type: RECEIVE_TOP_GAMES,
  games
});

export {
  requestTopGames,
  receiveTopGames
};
