import axios from 'axios';

axios.defaults.withCredentials = true;
axios.defaults.headers.post['Content-Type'] = 'application/json';

const BASE_URL = process.env.REACT_APP_API_HOST;

function GET(url, successCallback, failureCallback) {
  axios.get(BASE_URL + url).then((response) => {
    handleSuccess(successCallback, response);
  }).catch((error) => {
    handleError(failureCallback, error);
  });
}

function POST(url, payload, successCallback, failureCallback) {
  axios.post(BASE_URL + url, payload).then((response) => {
    handleSuccess(successCallback, response);
  }).catch((error) => {
    handleError(failureCallback, error);
  });
}

function DELETE(url, successCallback, failureCallback) {
  axios.delete(BASE_URL + url).then((response) => {
    handleSuccess(successCallback, response);
  }).catch((error) => {
    handleError(failureCallback, error);
  });
}

function PUT(url, payload, successCallback, failureCallback) {
  axios.put(BASE_URL + url, payload).then((response) => {
    handleSuccess(successCallback, response);
  }).catch((error) => {
    handleError(failureCallback, error);
  });
}

function handleSuccess(successCallback, response) {
  if(successCallback) {
    successCallback(response);
  }
}

function handleError(failureCallback, error) {
  console.error(error);
  if(failureCallback) {
    failureCallback(error);
  }
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
