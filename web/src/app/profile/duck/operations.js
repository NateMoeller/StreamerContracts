import { requestTestAlert, receiveTestAlert, receiveUserInfoFailure, receiveUserInfoSuccess, requestUserInfo } from './actions';
import RestClient from '../../RestClient';

const getUser = () => (dispatch) => {
  dispatch(requestUserInfo());

  RestClient.GET('user', (response) => {
    const responseData = response.data;
    const userData = responseData.userAuthentication.details.data[0];
    dispatch(receiveUserInfoSuccess(userData));
  }, (error) => {
    dispatch(receiveUserInfoFailure(error));
  });
};

const testAlert = () => (dispatch) => {
  dispatch(requestTestAlert());

  RestClient.POST('alert', null, (response) => {
    dispatch(receiveTestAlert(true));
  }, (error) => {
    dispatch(receiveTestAlert(false));
  });
};

export default {
  getUser,
  testAlert
};
