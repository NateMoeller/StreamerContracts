import {
  PUSH_NOTIFICATION,
  CLEAR_NOTIFICATION
} from './types';

const pushNotificationAction = (notification) => ({
  type: PUSH_NOTIFICATION,
  notification
});

const clearNotificationAction = (index) => ({
  type: CLEAR_NOTIFICATION,
  index
})

export {
  pushNotificationAction,
  clearNotificationAction
};
