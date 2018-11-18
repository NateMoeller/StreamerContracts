import {
  requestTestAlert,
  receiveTestAlert,
  receiveUserInfoFailure,
  receiveUserInfoSuccess,
  requestUserInfo,
  requestMyDonations,
  receiveMyDonations,
  requestMyBounties,
  receiveMyBounties,
  requestAcceptBounty,
  receiveAcceptBountySuccess,
  receiveAcceptBountyFailure,
  requestRemoveBounty,
  receiveRemoveBountySuccess,
  receiveRemoveBountyFailure
} from './actions';
import RestClient from '../../RestClient';

const getUser = () => (dispatch) => {
  dispatch(requestUserInfo());

  RestClient.GET('user', (response) => {
    const responseData = response.data;
    dispatch(receiveUserInfoSuccess(responseData));
  }, (error) => {
    dispatch(receiveUserInfoFailure(error));
  });
};

const testAlert = (alertChannelId) => (dispatch) => {
  dispatch(requestTestAlert());
  RestClient.POST('alert', alertChannelId, (response) => {
    dispatch(receiveTestAlert(true));
  }, (error) => {
    dispatch(receiveTestAlert(false));
  });
};

const voteContract = (payload) => (dispatch) => {
  RestClient.POST('bounties/vote', payload, (response) => {
    //TODO: handle success
    console.log(response);
  }, (error) => {
    //TODO: handle error
    console.error(error);
  });
}

const listMyDonations = (page, pageSize) => (dispatch) => {
  dispatch(requestMyDonations());

  RestClient.GET('donations/listDonations/' + page + '/' + pageSize, (response) => {
    dispatch(receiveMyDonations(response.data));
  }, (error) => {
    //TODO: handle error
    console.error(error);
  });
}

const listMyBounties = (page, pageSize, username, state = null) => (dispatch) => {
  dispatch(requestMyBounties());

  let url = `bounties/listBounties/${page}/${pageSize}?username=${username}`;
  if (state) {
    url += `&state=${state}`;
  }

  RestClient.GET(url, (response) => {
    dispatch(receiveMyBounties(response.data));
  }, (error) => {
    // TODO handle error
    console.error(error);
  })
}

const acceptBounty = (contractId) => (dispatch) => {
  const payload = { contractId };
  dispatch(requestAcceptBounty());

  RestClient.POST('bounties/accept', payload, (response) => {
    dispatch(receiveAcceptBountySuccess());
  }, (error) => {
    dispatch(receiveAcceptBountyFailure());
  })
}

const removeBounty = (contractId) => (dispatch) => {
  dispatch(requestRemoveBounty());

  RestClient.DELETE(`bounties/remove/${contractId}`, (response) => {
    dispatch(receiveRemoveBountySuccess());
  }, (error) => {
    dispatch(receiveRemoveBountyFailure());
  })
}

export default {
  getUser,
  testAlert,
  voteContract,
  listMyDonations,
  listMyBounties,
  acceptBounty,
  removeBounty
};
