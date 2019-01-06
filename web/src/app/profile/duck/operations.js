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
  receiveRemoveBountyFailure,
  requestVoteBounty,
  receiveVoteBounty,
  requestReportBounty,
  receiveReportBounty
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

const voteBounty = (payload, callback = null) => (dispatch) => {
  dispatch(requestVoteBounty());

  RestClient.POST('bounties/vote', payload, (response) => {
    dispatch(receiveVoteBounty());
    if (callback) {
      callback();
    }
  }, (error) => {
    //TODO: handle error
    console.error(error);
  });
}

const listDonorBounties = (page, pageSize) => (dispatch) => {
  dispatch(requestMyDonations());

  RestClient.GET('bounties/donorBounties/' + page + '/' + pageSize, (response) => {
    dispatch(receiveMyDonations(response.data));
  }, (error) => {
    //TODO: handle error
    console.error(error);
  });
}

const listStreamerBounties = (page, pageSize, username, state = null) => (dispatch) => {
  dispatch(requestMyBounties());

  let url = `bounties/streamerBounties/${page}/${pageSize}?username=${username}`;
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

const acceptBounty = (contractId, callback = null) => (dispatch) => {
  const payload = { contractId };
  dispatch(requestAcceptBounty());

  RestClient.PUT('bounties/accept', payload, (response) => {
    dispatch(receiveAcceptBountySuccess());
    if (callback) {
      callback();
    }
  }, (error) => {
    dispatch(receiveAcceptBountyFailure());
  })
}

const removeBounty = (contractId, callback = null) => (dispatch) => {
  const payload = { contractId };
  dispatch(requestRemoveBounty());

  RestClient.PUT('bounties/decline', payload, (response) => {
    dispatch(receiveRemoveBountySuccess());
    if (callback) {
      callback();
    }
  }, (error) => {
    dispatch(receiveRemoveBountyFailure());
  })
}

const reportBounty = (contactEmail, report, reportedContractId = null) => (dispatch) => {
  const payload = {
    report,
    contactEmail,
    reportedContractId
  };
  dispatch(requestReportBounty());

  RestClient.POST('report', payload, (response) => {
    dispatch(receiveReportBounty());
  });
}

export default {
  getUser,
  testAlert,
  voteBounty,
  listDonorBounties,
  listStreamerBounties,
  acceptBounty,
  removeBounty,
  reportBounty
};
