import React, { Component } from 'react';
import LoadingComponent from '../common/loading/LoadingComponent';
import ProfileComponent from './ProfileComponent';
import PropTypes from 'prop-types';
import { Redirect } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { profileOperations } from './duck';
import { settingsOperations } from '../settings/duck';

/* global sessionStorage */
class ProfileContainer extends Component {
  componentDidMount() {
    const { getUser } = this.props;
    getUser();
  }

  render() {
    if (this.props.profile.redirect) {
      return (
        <Redirect to={{ pathname: '/login', state: { from: this.props.location } }} />
      );
    }
    const user = JSON.parse(sessionStorage.getItem('user'));

    if (!user) {
      return <LoadingComponent />;
    }

    return (
      <ProfileComponent
        user={user}
        testAlert={this.props.testAlert}
        alertChannelId={user.alertChannelId}
        listDonorBounties={this.props.listDonorBounties}
        listStreamerBounties={this.props.listStreamerBounties}
        voteBounty={this.props.voteBounty}
        bounties={this.props.profile.bounties}
        donations={this.props.profile.donations}
        totalBounties={this.props.profile.totalBounties}
        totalDonations={this.props.profile.totalDonations}
        showSpinner={this.props.profile.showSpinner}
        activateBounty={this.props.activateBounty}
        declineBounty={this.props.declineBounty}
        getSettings={this.props.getSettings}
        settings={this.props.settings}
      />
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...profileOperations, ...settingsOperations }, dispatch);
}

function mapStateToProps(state) {
  return {
    profile: state.profile,
    settings: state.settings
  };
}

ProfileContainer.propTypes = {
  getUser: PropTypes.func.isRequired,
  profile: PropTypes.object.isRequired,
  location: PropTypes.object.isRequired,
  listDonorBounties: PropTypes.func.isRequired,
  listStreamerBounties: PropTypes.func.isRequired,
  voteBounty: PropTypes.func.isRequired
};

export default connect(mapStateToProps, mapDispatchToProps)(ProfileContainer);
