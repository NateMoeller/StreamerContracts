import axios from 'axios';

axios.defaults.withCredentials = true;
const BASE_URL = process.env.REACT_APP_API_HOST;

function GET(url, successCallback, failureCallback) {
  axios.get(BASE_URL + url).then((response) => {
    successCallback(response);
  }).catch((error) => {
    failureCallback(error);
  });
}

function POST(url, payload, successCallback, failureCallback) {
  axios.post(BASE_URL + url, payload).then((response) => {
    successCallback(response);
  }).catch((error) => {
    failureCallback(error);
  });
}

function DELETE(url, successCallback, failureCallback) {
  axios.delete(BASE_URL + url).then((response) => {
    successCallback(response);
  }).catch((error) => {
    failureCallback(error);
  });
}

function PUT(url, payload, successCallback, failureCallback) {
  axios.put(BASE_URL + url, payload).then((response) => {
    successCallback(response);
  }).catch((error) => {
    failureCallback(error);
  });
}

function PATCH(url, payload, successCallback, failureCallback) {
  axios.patch(BASE_URL + url, payload).then((response) => {
    successCallback(response);
  }).catch((error) => {
    failureCallback(error);
  });
}

const RestClient = {
  GET,
  POST,
  DELETE,
  PUT,
  PATCH
};

export default RestClient;
