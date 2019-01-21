import {
  PUSH_NOTIFICATION,
  CLEAR_NOTIFICATION
} from './types';

const INITIAL_STATE = {
  notifications: []
};

const notificationReducer = (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case PUSH_NOTIFICATION: {
      return {
        ...state,
        notifications: [action.notification, ...state.notifications]
      };
    }
    case CLEAR_NOTIFICATION: {
      return {
        ...state,
        notifications: [...state.notifications.slice(0, action.index),...state.notifications.slice(action.index + 1)]
      }
    }
    default: {
      return state;
    }
  }
}

export default notificationReducer;
