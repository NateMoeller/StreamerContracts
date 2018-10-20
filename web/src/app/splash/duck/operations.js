import { requestSubmitEmail, receiveSubmitEmail } from './actions';
import RestClient from '../../RestClient';

const addEmail = (payload) => (dispatch) => {
  dispatch(requestSubmitEmail(payload));
  RestClient.POST('email', payload, (response) => {
    dispatch(receiveSubmitEmail());
  });
};

export default {
  addEmail
};
