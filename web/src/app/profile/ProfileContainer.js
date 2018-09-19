import React, { Component } from 'react';
import ProfileComponent from './ProfileComponent';
import PropTypes from 'prop-types';
import { Redirect } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { profileOperations } from './duck';

/* global sessionStorage */
class ProfileContainer extends Component {
  componentWillMount() {
    const { getUser } = this.props;
    if (!sessionStorage.getItem('user')) {
      getUser();
    }
  }

  render() {
    if (this.props.profile.redirect) {
      return (
        <Redirect to={{ pathname: '/login', state: { from: this.props.location } }} />
      );
    }
    const user = JSON.parse(sessionStorage.getItem('user'));

    if (!user) {
      // TODO: show a nice spinner here
      return 'Loading...';
    }

    return (
      <ProfileComponent
        twitchUserName={user.displayName}
        imageUrl={user.profileImageUrl}
        testAlert={this.props.testAlert}
        alertChannelId={user.alertChannelId}
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

ProfileContainer.propTypes = {
  getUser: PropTypes.func.isRequired,
  profile: PropTypes.object.isRequired,
  location: PropTypes.object.isRequired
};

export default connect(mapStateToProps, mapDispatchToProps)(ProfileContainer);
