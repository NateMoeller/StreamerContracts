import {
  REQUEST_UPDATE_PAYPAL_EMAIL,
  RECEIVE_UPDATE_PAYPAL_EMAIL_SUCCESS,
  RECEIVE_UPDATE_PAYPAL_EMAIL_FAILURE,
  REQUEST_SETTINGS,
  RECEIVE_SETTINGS_SUCCESS,
  RECEIVE_SETTINGS_FAILURE
} from './types';

const requestUpdatePayPalEmail = () => ({
  type: REQUEST_UPDATE_PAYPAL_EMAIL
});

const receiveUpdatePayPalEmailSuccess = (payPalEmail) => ({
  type: RECEIVE_UPDATE_PAYPAL_EMAIL_SUCCESS,
  payPalEmail
});

const receiveUpdatePayPalEmailFailure = () => ({
  type: RECEIVE_UPDATE_PAYPAL_EMAIL_FAILURE
});

const requestSettings = () => ({
  type: REQUEST_SETTINGS
});

const receiveSettingsSuccess = (userSettings) => ({
  type: RECEIVE_SETTINGS_SUCCESS,
  userSettings
});

const receiveSettingsFailure = () => ({
  type: RECEIVE_SETTINGS_FAILURE
});

export {
  requestUpdatePayPalEmail,
  receiveUpdatePayPalEmailSuccess,
  receiveUpdatePayPalEmailFailure,
  requestSettings,
  receiveSettingsSuccess,
  receiveSettingsFailure
};
