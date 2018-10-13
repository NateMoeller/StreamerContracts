import { requestInsertBounty, receiveInsertBountySuccess, receiveInsertBountyFailure } from './actions';
import RestClient from '../../RestClient';

const insertBounty = (payload) => (dispatch) => {
  dispatch(requestInsertBounty(payload));

  return RestClient.POST('donations', payload, (response) => {
    const responseData = response.data;
    dispatch(receiveInsertBountySuccess(responseData));
  }, (error) => {
    dispatch(receiveInsertBountyFailure(error));
  });
}

export default {
  insertBounty
};
