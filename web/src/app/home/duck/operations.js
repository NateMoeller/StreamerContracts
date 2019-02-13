import {
  requestHomeBounties,
  receiveHomeBounties
} from './actions';
import RestClient from '../../RestClient';

const getHomeBounties = (page, pageSize) => (dispatch) => {
  dispatch(requestHomeBounties());

  let url = `bounties/streamerBounties/${page}/${pageSize}`;
  RestClient.GET(url, (response) => {
    dispatch(receiveHomeBounties(response.data));
  }, (error) => {
    console.error(error);
  });
}

export default {
  getHomeBounties
};
