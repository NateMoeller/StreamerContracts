import {
  RECEIVE_UPDATE_PAYPAL_EMAIL_SUCCESS,
  RECEIVE_SETTINGS_SUCCESS,
} from './types';

const INITIAL_STATE = {
  payPalEmail: null
};

const settingsReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case RECEIVE_SETTINGS_SUCCESS: {
      return {
        ...state,
        payPalEmail: action.userSettings.paypalEmail
      }
    }
    case RECEIVE_UPDATE_PAYPAL_EMAIL_SUCCESS: {
      return {
        ...state,
        payPalEmail: action.payPalEmail
      }
    }
    default: {
      return state;
    }
  }
}

export default settingsReducer;
