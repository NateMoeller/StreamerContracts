import {
  requestTestAlert,
  receiveTestAlert,
  receiveUserInfoFailure,
  receiveUserInfoSuccess,
  requestUserInfo,
  requestOpenContracts,
  receiveOpenContracts
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

const updateContract = (payload) => (dispatch) => {
  RestClient.POST('donations/update', payload, (response) => {
    //TODO: handle success
    console.log(response);
  }, (error) => {
    //TODO: handle error
    console.error(error);
  });
}

const listOpenDonations = (page, pageSize) => (dispatch) => {
  dispatch(requestOpenContracts());
  RestClient.GET('donations/listOpenDonations/' + page + '/' + pageSize, (response) => {
    dispatch(receiveOpenContracts(response.data));
  }, (error) => {
    //TODO: handle error
    console.error(error);
  });
}

export default {
  getUser,
  testAlert,
  updateContract,
  listOpenDonations
};
