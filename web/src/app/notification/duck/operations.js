import {
  pushNotificationAction,
  clearNotificationAction
} from './actions';

const pushNotification = (notification) => (dispatch) => {
  dispatch(pushNotificationAction(notification));
}

const clearNotification = (index) => (dispatch) => {
  dispatch(clearNotificationAction(index));
}

export default {
  pushNotification,
  clearNotification
};
