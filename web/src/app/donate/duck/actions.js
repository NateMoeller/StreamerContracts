import { REQUEST_INSERT_BOUNTY, RECEIVE_INSERT_BOUNTY_SUCCESS, RECEIVE_INSERT_BOUNTY_FAILURE } from './types';

const requestInsertBounty = (payload) => ({
  type: REQUEST_INSERT_BOUNTY,
  payload
});

const receiveInsertBountySuccess = () => ({
  type: RECEIVE_INSERT_BOUNTY_SUCCESS
});

const receiveInsertBountyFailure = () => ({
  type: RECEIVE_INSERT_BOUNTY_FAILURE
});

export {
  requestInsertBounty,
  receiveInsertBountySuccess,
  receiveInsertBountyFailure
};
