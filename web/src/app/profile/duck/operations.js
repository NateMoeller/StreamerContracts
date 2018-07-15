import { receiveUserInfoFailure, receiveUserInfoSuccess, requestUserInfo } from './actions';
import axios from 'axios';

axios.defaults.withCredentials = true;

const getUser = () => (dispatch) => {
  dispatch(requestUserInfo());

  return axios.get(process.env.REACT_APP_API_HOST + 'user').then((response) => {
    const responseData = response.data;
    const userData = responseData.userAuthentication.details.data[0];
    dispatch(receiveUserInfoSuccess(userData));
  }).catch((error) => {
    dispatch(receiveUserInfoFailure(error));
  });
};

export default {
  getUser
};
