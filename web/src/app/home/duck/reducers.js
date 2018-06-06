// reducers.js
import types from './types';

const INITIAL_STATE = {
  showSpinner: false,
}
const homeReducer = (state=INITIAL_STATE, action) => {
  switch(action.type) {
    case types.REQUEST_API: {
      return {
        ...state,
        showSpinner: true
      }
    }
    case types.RECEIVE_API: {
      return {
        ...state,
        showSpinner: false
      }
    }
    default:
      return state;
  }
}

export default homeReducer;
