import { receiveUserInfoFailure, receiveUserInfoSuccess, requestUserInfo } from './actions';
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

export default {
  getUser
};
