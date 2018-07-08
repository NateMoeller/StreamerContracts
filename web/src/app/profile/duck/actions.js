import { RECEIVE_USER_INFO_FAILURE, RECEIVE_USER_INFO_SUCCESS, REQUEST_USER_INFO } from './types';

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

export {
  requestUserInfo,
  receiveUserInfoSuccess,
  receiveUserInfoFailure
};
