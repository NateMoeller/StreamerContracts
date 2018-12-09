import {
  requestTopGames,
  receiveTopGames
} from './actions';

import RestClient from '../../RestClient';

const getTopGames = () => (dispatch) => {
  dispatch(requestTopGames());

  RestClient.GET('twitch/topGames', (response) => {
    dispatch(receiveTopGames(response.data ? response.data.data : []));
  }, (error) => {
    console.error('Could not get the top games from twitch');
    dispatch(receiveTopGames([]));
  });
};

export default {
  getTopGames
};
