import {
  requestPublicUser,
  receivePublicUserSuccess,
  receivePublicUserFailure
} from './actions';
import RestClient from '../../RestClient';

const getPublicUser = (twitchUsername) => (dispatch) => {
  dispatch(requestPublicUser());

  RestClient.GET(`user/${twitchUsername}`, (response) => {
    dispatch(receivePublicUserSuccess(response.data));
  }, (error) => {
    dispatch(receivePublicUserFailure());
  });
};

export default {
  getPublicUser
};
