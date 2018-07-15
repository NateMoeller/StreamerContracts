import axios from 'axios';
import Creators from './actions';

axios.defaults.withCredentials = true;

const requestApi = Creators.requestApi;
const receiveApi = Creators.receiveApi;


const fetchApi = () => {
  return (dispatch) => {
    dispatch(requestApi());

    return axios.get(process.env.REACT_APP_API_HOST + 'user').then((response) => {
      const responseData = response.data;

      // can post process the data here

      dispatch(receiveApi(responseData));
    });
  }
};

export default {
  fetchApi
};
