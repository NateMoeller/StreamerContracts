import { requestTestAlert, receiveTestAlert, receiveUserInfoFailure, receiveUserInfoSuccess, requestUserInfo } from './actions';
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

const testAlert = (alertKey) => (dispatch) => {
  dispatch(requestTestAlert());
  RestClient.POST('alert', alertKey, (response) => {
    dispatch(receiveTestAlert(true));
  }, (error) => {
    dispatch(receiveTestAlert(false));
  });
};

export default {
  getUser,
  testAlert
};
