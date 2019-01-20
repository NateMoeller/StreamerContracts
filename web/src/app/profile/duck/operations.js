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
  requestActivateBounty,
  receiveActivateBountySuccess,
  receiveActivateBountyFailure,
  requestRemoveBounty,
  receiveRemoveBountySuccess,
  receiveRemoveBountyFailure,
  requestVoteBounty,
  receiveVoteBounty,
  requestActiveBounty,
  receiveActiveBounty,
  requestDeactivateBounty,
  receiveDeactivateBounty
} from './actions';
import { ACTIVE } from '../../BountyState';
import RestClient from '../../RestClient';

/* global sessionStorage */
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
  const user = JSON.parse(sessionStorage.getItem('user'));
  const votePayload = { contractId: payload.contract.contractId, flagCompleted: payload.flagCompleted };

  if (payload.contract.state === ACTIVE && user.displayName === payload.contract.streamerName) {
    dispatch(requestDeactivateBounty());
    RestClient.PUT('bounties/deactivate', { contractId: payload.contract.contractId }, () => {
      dispatch(receiveDeactivateBounty());
      dispatch(requestVoteBounty());

      RestClient.POST('bounties/vote', votePayload, (response) => {
        dispatch(receiveVoteBounty());
        if (callback) {
          callback();
        }
      }, (error) => {
        console.error(error);
      });
    });
  } else {
    dispatch(requestVoteBounty());
    RestClient.POST('bounties/vote', votePayload, (response) => {
      dispatch(receiveVoteBounty());
      if (callback) {
        callback();
      }
    }, (error) => {
      console.error(error);
    });
  }
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
    dispatch(receiveMyBounties(state, response.data));
  }, (error) => {
    // TODO handle error
    console.error(error);
  })
}

const activateBounty = (contractId, callback = null) => (dispatch) => {
  const payload = { contractId };
  dispatch(requestActivateBounty());

  RestClient.PUT('bounties/activate', payload, (response) => {
    dispatch(receiveActivateBountySuccess(response.data));
    if (callback) {
      callback();
    }
  }, (error) => {
    dispatch(receiveActivateBountyFailure());
  })
}

const declineBounty = (contractId, callback = null) => (dispatch) => {
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

const getMyActiveBounties = (username) => (dispatch) => {
  dispatch(requestActiveBounty());
  const page = 0;
  const pageSize = 10;
  const url = `bounties/streamerBounties/${page}/${pageSize}?username=${username}&state=ACTIVE`;

  RestClient.GET(url, (response) => {
    dispatch(receiveActiveBounty(response.data.content[0]));
  }); // do nothing on error
}

export default {
  getUser,
  testAlert,
  voteBounty,
  listDonorBounties,
  listStreamerBounties,
  activateBounty,
  declineBounty,
  getMyActiveBounties
};
