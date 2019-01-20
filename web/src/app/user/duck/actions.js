import {
  REQUEST_PUBLIC_USER,
  RECEIVE_PUBLIC_USER_SUCCESS,
  RECEIVE_PUBLIC_USER_FAILURE,
  REQUEST_PUBLIC_BOUNTIES,
  RECEIVE_PUBLIC_BOUNITES_SUCCESS,
  RECEIVE_PUBLIC_BOUNTIES_FAILURE,
  REQUEST_PUBLIC_ACTIVE_BOUNTY,
  RECEIVE_PUBLIC_ACTIVE_BOUNTY
} from './types';

const requestPublicUser = () => ({
  type: REQUEST_PUBLIC_USER
});

const receivePublicUserSuccess = publicUser => ({
  type: RECEIVE_PUBLIC_USER_SUCCESS,
  publicUser
});

const receivePublicUserFailure = () => ({
  type: RECEIVE_PUBLIC_USER_FAILURE
});

const requestPublicBounties = () => ({
  type: REQUEST_PUBLIC_BOUNTIES
});

const receivePublicBountiesSuccess = (data) => ({
  type: RECEIVE_PUBLIC_BOUNITES_SUCCESS,
  data
});

const receivePublicBountiesFailure = () => ({
  type: RECEIVE_PUBLIC_BOUNTIES_FAILURE
});

const requestPublicActiveBounty = () => ({
  type: REQUEST_PUBLIC_ACTIVE_BOUNTY
});

const receivePublicActiveBounty = (activeBounty) => ({
  type: RECEIVE_PUBLIC_ACTIVE_BOUNTY,
  activeBounty
});

export {
  requestPublicUser,
  receivePublicUserSuccess,
  receivePublicUserFailure,
  requestPublicBounties,
  receivePublicBountiesSuccess,
  receivePublicBountiesFailure,
  requestPublicActiveBounty,
  receivePublicActiveBounty
};
