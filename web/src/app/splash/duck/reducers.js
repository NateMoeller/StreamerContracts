import { REQUEST_SUBMIT_EMAIL, RECEIVE_SUBMIT_EMAIL } from './types';

const INITIAL_STATE = {
  showSpinner: false,
  emailSubmitted: false
};

const emailReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case REQUEST_SUBMIT_EMAIL: {
      return {
        ...state,
        showSpinner: true
      };
    }
    case RECEIVE_SUBMIT_EMAIL: {
      return {
        ...state,
        showSpinner: false,
        emailSubmitted: true
      };
    }
    default: {
      return state;
    }
  }
}

export default emailReducer;
