import {
  RECEIVE_USER_INFO_FAILURE,
  RECEIVE_USER_INFO_SUCCESS,
  REQUEST_USER_INFO,
  REQUEST_TEST_ALERT,
  RECEIVE_TEST_ALERT
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

export {
  requestUserInfo,
  receiveUserInfoSuccess,
  receiveUserInfoFailure,
  requestTestAlert,
  receiveTestAlert
};
