import {
  requestUpdatePayPalEmail,
  receiveUpdatePayPalEmailSuccess,
  receiveUpdatePayPalEmailFailure,
  requestSettings,
  receiveSettingsSuccess,
  receiveSettingsFailure
} from './actions';

import RestClient from '../../RestClient';

const updatePayPalEmail = (payload) => (dispatch) => {
  dispatch(requestUpdatePayPalEmail());

  RestClient.POST('userSettings', payload, (response) => {
    dispatch(receiveUpdatePayPalEmailSuccess(payload.paypalEmail));
  }, (error) => {
    dispatch(receiveUpdatePayPalEmailFailure());
  })
}

const getSettings =  () => (dispatch) => {
  dispatch(requestSettings());

  RestClient.GET('userSettings', (response) => {
    dispatch(receiveSettingsSuccess(response.data));
  }, (error) => {
    dispatch(receiveSettingsFailure());
  })
}

export default {
  updatePayPalEmail,
  getSettings
};
