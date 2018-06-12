import types from './types.js';

const requestApi = () => ({
    type: types.REQUEST_API
    // more stuff can go here
});
const receiveApi = (json) => ({
    type: types.RECEIVE_API,
    response: json
});

export default {
    requestApi,
    receiveApi
};
