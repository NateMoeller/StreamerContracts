import React, { Component } from 'react';
import ActiveBounty from './ActiveBounty';
import PropTypes from 'prop-types';
import { publicUserOperations } from '../../user/duck';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

class PublicActiveBountyContainer extends Component {
  componentDidMount() {
    this.props.getPublicActiveBounty(this.props.twitchUserName);
  }

  render() {
    return (
      <ActiveBounty activeBounty={this.props.publicUser.activeBounty} />
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...publicUserOperations }, dispatch);
}

function mapStateToProps(state) {
  return {
    publicUser: state.publicUser
  };
}

PublicActiveBountyContainer.propTypes = {
  twitchUserName: PropTypes.string.isRequired
}

export default connect(mapStateToProps, mapDispatchToProps)(PublicActiveBountyContainer);
