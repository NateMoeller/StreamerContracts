import { REQUEST_SUBMIT_EMAIL, RECEIVE_SUBMIT_EMAIL } from './types';

const requestSubmitEmail = (payload) => ({
  type: REQUEST_SUBMIT_EMAIL,
  payload
});

const receiveSubmitEmail = () => ({
  type: RECEIVE_SUBMIT_EMAIL
});

export {
  requestSubmitEmail,
  receiveSubmitEmail
};
