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
  REQUEST_ACCEPT_BOUNTY,
  RECEIVE_ACCEPT_BOUNTY_SUCCESS,
  RECEIVE_ACCEPT_BOUNTY_FAILURE,
  REQUEST_REMOVE_BOUNTY,
  RECEIVE_REMOVE_BOUNTY_SUCCESS,
  RECEIVE_REMOVE_BOUNTY_FAILURE
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

const receiveMyBounties = (data) => ({
  type: RECEIVE_MY_BOUNTIES,
  data
});

const requestAcceptBounty = () => ({
  type: REQUEST_ACCEPT_BOUNTY
});

const receiveAcceptBountySuccess = () => ({
  type: RECEIVE_ACCEPT_BOUNTY_SUCCESS
});

const receiveAcceptBountyFailure = () => ({
  type: RECEIVE_ACCEPT_BOUNTY_FAILURE
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
  requestAcceptBounty,
  receiveAcceptBountySuccess,
  receiveAcceptBountyFailure,
  requestRemoveBounty,
  receiveRemoveBountySuccess,
  receiveRemoveBountyFailure
};
