import { requestInsertBounty, receiveInsertBountySuccess } from './actions';

const insertBounty = (payload) => (dispatch) => {
  dispatch(requestInsertBounty(payload));

  // TODO: call backend endpoint to insert bounty
  dispatch(receiveInsertBountySuccess());
}

export default {
  insertBounty
};
