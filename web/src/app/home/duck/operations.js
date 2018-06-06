// operations.js
import axios from 'axios';
import Creators from './actions';

const requestApi = Creators.requestApi;
const receiveApi = Creators.receiveApi;


const fetchApi = () => {
  return (dispatch) => {

    dispatch(requestApi());

    return axios.get('/api').then((response) => {
      const responseData = response.data;

      // can post process the data here

        dispatch(receiveApi(responseData));
    });
  }
};

export default {
  fetchApi
};
