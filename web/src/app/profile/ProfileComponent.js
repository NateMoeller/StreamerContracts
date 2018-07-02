import React, { Component } from 'react';
import PropTypes from 'prop-types';

class ProfileComponent extends Component {
  render() {
    return (
      <div>
        Profile Component <br />
        <button type="button" onClick={this.props.logout}>Logout</button>
      </div>
    );
  }
}

ProfileComponent.propTypes = {
  logout: PropTypes.func.isRequired
};

export default ProfileComponent;
