import {
  REQUEST_SETTINGS,
  RECEIVE_UPDATE_PAYPAL_EMAIL_SUCCESS,
  RECEIVE_SETTINGS_SUCCESS,
} from './types';

const INITIAL_STATE = {
  settingsLoading: false,
  payPalEmail: null,
  isBusinessEmail: false
};

const settingsReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case REQUEST_SETTINGS: {
      return {
        ...state,
        settingsLoading: true
      };
    }
    case RECEIVE_SETTINGS_SUCCESS: {
      return {
        ...state,
        payPalEmail: action.userSettings.paypalEmail,
        isBusinessEmail: action.userSettings.isBusinessEmail,
        settingsLoading: false
      };
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
