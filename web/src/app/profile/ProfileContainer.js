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
      logout: false
    };

    this.logout = this.logout.bind(this);
  }
  componentWillMount() {
    const { getUser } = this.props;
    if (!sessionStorage.getItem('user')) {
      getUser();
    }
  }

  logout() {
    // TODO: call backend to logout user
    sessionStorage.removeItem('user');
    this.setState({
      logout: true
    });
  }

  render() {
    if (this.props.profile.redirect || this.state.logout) {
      return (
        <Redirect to={{ pathname: '/login', state: { from: this.props.location } }} />
      );
    }

    return (
      <ProfileComponent
        logout={this.logout}
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
