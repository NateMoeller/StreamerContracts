import React, { Component } from 'react';
import PropTypes from 'prop-types';
import StreamerDashboard from '../../../../../web/src/app/profile/StreamerDashboard/StreamerDashboard';
import styles from './BroadcasterStyles.scss';

class BroadCasterComponent extends Component {
  render() {
    return (
      <div>
        <StreamerDashboard
          twitchUserName={this.props.user.displayName}
          listStreamerBounties={this.props.listStreamerBounties}
          bounties={this.props.bounties}
          totalBounties={this.props.totalBounties}
          activateBounty={this.props.activateBounty}
          declineBounty={this.props.declineBounty}
          voteBounty={this.props.voteBounty}
          isExtension
          setBountyFilter={this.props.setBountyFilter}
          getSettings={this.props.getSettings}
          settings={this.props.settings}
          loading={this.props.loading}
        />
      </div>
    );
  }
}

BroadCasterComponent.defaultProps = {
  user: null
}

BroadCasterComponent.propTypes = {
  user: PropTypes.object,
  listStreamerBounties: PropTypes.func.isRequired,
  loading: PropTypes.bool.isRequired,
  bounties: PropTypes.array.isRequired,
  totalBounties: PropTypes.number.isRequired,
  activateBounty: PropTypes.func.isRequired,
  declineBounty: PropTypes.func.isRequired,
  voteBounty: PropTypes.func.isRequired,
  setBountyFilter: PropTypes.func.isRequired,
  getSettings: PropTypes.func.isRequired,
  settings: PropTypes.object.isRequired
};

export default BroadCasterComponent;
