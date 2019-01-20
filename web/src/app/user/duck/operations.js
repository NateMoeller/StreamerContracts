import {
  requestPublicUser,
  receivePublicUserSuccess,
  receivePublicUserFailure,
  requestPublicBounties,
  receivePublicBountiesSuccess,
  receivePublicBountiesFailure,
  requestPublicActiveBounty,
  receivePublicActiveBounty
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
    dispatch(receivePublicBountiesFailure());
  })
};

const getPublicActiveBounty = (username) => (dispatch) => {
  dispatch(requestPublicActiveBounty());
  const page = 0;
  const pageSize = 10;
  const url = `bounties/streamerBounties/${page}/${pageSize}?username=${username}&state=ACTIVE`;

  RestClient.GET(url, (response) => {
    dispatch(receivePublicActiveBounty(response.data.content[0]));
  }); // do nothing on failure
}

export default {
  getPublicUser,
  getPublicBounties,
  getPublicActiveBounty
};
