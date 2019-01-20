import {
  RECEIVE_USER_INFO_FAILURE,
  RECEIVE_USER_INFO_SUCCESS,
  REQUEST_USER_INFO,
  REQUEST_TEST_ALERT,
  RECEIVE_TEST_ALERT,
  REQUEST_MY_DONATIONS,
  RECEIVE_MY_DONATIONS,
  REQUEST_MY_BOUNTIES,
  RECEIVE_MY_BOUNTIES,
  REQUEST_ACTIVATE_BOUNTY,
  RECEIVE_ACTIVATE_BOUNTY_SUCCESS,
  RECEIVE_ACTIVATE_BOUNTY_FAILURE,
  REQUEST_REMOVE_BOUNTY,
  RECEIVE_REMOVE_BOUNTY_SUCCESS,
  RECEIVE_REMOVE_BOUNTY_FAILURE,
  REQUEST_VOTE_BOUNTY,
  RECEIVE_VOTE_BOUNTY,
  REQUEST_ACTIVE_BOUNTY,
  RECEIVE_ACTIVE_BOUNTY,
  REQUEST_DEACTIVATE_BOUNTY,
  RECEIVE_DEACTIVATE_BOUNTY
} from './types';

const requestUserInfo = () => ({
  type: REQUEST_USER_INFO
});

const receiveUserInfoSuccess = userInfo => ({
  type: RECEIVE_USER_INFO_SUCCESS,
  userInfo
});

const receiveUserInfoFailure = error => ({
  type: RECEIVE_USER_INFO_FAILURE,
  error
});

const requestTestAlert = () => ({
  type: REQUEST_TEST_ALERT
});

const receiveTestAlert = (success) => ({
  type: RECEIVE_TEST_ALERT,
  success
});

const requestMyDonations = () => ({
  type: REQUEST_MY_DONATIONS
});

const receiveMyDonations = (data) => ({
  type: RECEIVE_MY_DONATIONS,
  data
});

const requestMyBounties = () => ({
  type: REQUEST_MY_BOUNTIES
});

const receiveMyBounties = (bountyState, data) => ({
  type: RECEIVE_MY_BOUNTIES,
  data,
  bountyState
});

const requestActivateBounty = () => ({
  type: REQUEST_ACTIVATE_BOUNTY
});

const receiveActivateBountySuccess = (activeBounty) => ({
  type: RECEIVE_ACTIVATE_BOUNTY_SUCCESS,
  activeBounty
});

const receiveActivateBountyFailure = () => ({
  type: RECEIVE_ACTIVATE_BOUNTY_FAILURE
});

const requestRemoveBounty = () => ({
  type: REQUEST_REMOVE_BOUNTY
});

const receiveRemoveBountySuccess = () => ({
  type: RECEIVE_REMOVE_BOUNTY_SUCCESS
});

const receiveRemoveBountyFailure = () => ({
  type: RECEIVE_REMOVE_BOUNTY_FAILURE
});

const requestVoteBounty = () => ({
  type: REQUEST_VOTE_BOUNTY
});

const receiveVoteBounty = () => ({
  type: RECEIVE_VOTE_BOUNTY
});

const requestActiveBounty = () => ({
  type: REQUEST_ACTIVE_BOUNTY
});

const receiveActiveBounty = (activeBounty) => ({
  type: RECEIVE_ACTIVE_BOUNTY,
  activeBounty
});

const requestDeactivateBounty = () => ({
  type: REQUEST_DEACTIVATE_BOUNTY
});

const receiveDeactivateBounty = () => ({
  type: RECEIVE_DEACTIVATE_BOUNTY
});

export {
  requestUserInfo,
  receiveUserInfoSuccess,
  receiveUserInfoFailure,
  requestTestAlert,
  receiveTestAlert,
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
};
