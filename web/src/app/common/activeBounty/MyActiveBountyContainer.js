import React, { Component } from 'react';
import ActiveBounty from './ActiveBounty';
import PropTypes from 'prop-types';
import { profileOperations } from '../../profile/duck';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

class MyActiveBountyContainer extends Component {
  componentDidMount() {
    this.props.getMyActiveBounties(this.props.twitchUserName);
  }

  render() {
    return (
      <ActiveBounty
        activeBounty={this.props.profile.activeBounty}
        voteBounty={this.props.voteBounty}
      />
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...profileOperations }, dispatch);
}

function mapStateToProps(state) {
  return {
    profile: state.profile
  };
}

MyActiveBountyContainer.propTypes = {
  twitchUserName: PropTypes.string.isRequired
}

export default connect(mapStateToProps, mapDispatchToProps)(MyActiveBountyContainer);
