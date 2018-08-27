import React, { Component } from 'react';
import ProfileComponent from './ProfileComponent';
import PropTypes from 'prop-types';
import { Redirect } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { profileOperations } from './duck';

/* global sessionStorage */
class ProfileContainer extends Component {
  constructor(props) {
    super(props);

    this.state = {
      activeTab: 'account'
    };
  }
  componentWillMount() {
    const { getUser } = this.props;
    // TODO: Temporarily always hit API to retrieve user information to test Oauth interacting with a load balancer. Remove this line when testing is complete
    // if (!sessionStorage.getItem('user')) {
    if (true) {
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
        twitchUserName={user.display_name}
        imageUrl={user.profile_image_url}
        activeTab={this.state.activeTab}
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
