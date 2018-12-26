import {
  requestPublicUser,
  receivePublicUserSuccess,
  receivePublicUserFailure,
  requestPublicBounties,
  receivePublicBountiesSuccess,
  receivePublicBountiesFailure
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

const getPublicBounties = (page, pageSize, twitchUsername, state = null) => (dispatch) => {
  dispatch(requestPublicBounties());
  let url = `bounties/streamerBounties/${page}/${pageSize}?username=${twitchUsername}`;
  if (state) {
    url += `&state=${state}`;
  }

  RestClient.GET(url, (response) => {
    dispatch(receivePublicBountiesSuccess(response.data));
  }, (error) => {
    console.error(error);
    dispatch(receivePublicBountiesFailure());
  })
};

export default {
  getPublicUser,
  getPublicBounties
};
