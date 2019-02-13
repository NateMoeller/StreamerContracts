import {
  REQUEST_HOME_BOUNTIES,
  RECEIVE_HOME_BOUNTIES
} from './types';

const requestHomeBounties = () => ({
  type: REQUEST_HOME_BOUNTIES
});
const receiveHomeBounties = (bounties) => ({
  type: RECEIVE_HOME_BOUNTIES,
  bounties
});

export {
  requestHomeBounties,
  receiveHomeBounties
};
