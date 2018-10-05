import {
  REQUEST_PUBLIC_USER,
  RECEIVE_PUBLIC_USER_SUCCESS,
  RECEIVE_PUBLIC_USER_FAILURE
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

export {
  requestPublicUser,
  receivePublicUserSuccess,
  receivePublicUserFailure
};
