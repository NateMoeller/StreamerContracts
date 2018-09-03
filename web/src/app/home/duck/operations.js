import Creators from './actions';
import RestClient from '../../RestClient';

const requestApi = Creators.requestApi;
const receiveApi = Creators.receiveApi;

const fetchApi = () => {
  return (dispatch) => {
    dispatch(requestApi());

    RestClient.GET('user', (response) => {
      const responseData = response.data;

      // can post process the data here
      dispatch(receiveApi(responseData));
    });
  }
};

export default {
  fetchApi
};
