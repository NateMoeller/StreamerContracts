import React, { Component } from 'react';
import PropTypes from 'prop-types';

class UserComponent extends Component {
  render() {
    return (
      <div>
        {this.props.publicUser.displayName}
      </div>
    )
  }
}

UserComponent.propTypes = {
  publicUser: PropTypes.object.isRequired
};

export default UserComponent;
