import {
  PageHeader
} from 'react-bootstrap';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { LinkContainer } from 'react-router-bootstrap';
import styles from './InvalidUserStyles.scss';

class InvalidUserComponent extends Component {
  render() {
    return (
      <div className={styles.missingContainer}>
        <PageHeader>{this.props.title}</PageHeader>
        <div>
          {this.props.message}
        </div>
      </div>
    );
  }
}

InvalidUserComponent.defaultProps = {
  title: 'User not found',
  message: 'We couldn\'t find this user. They may not have signed up for Bounty streamer yet.'
}

InvalidUserComponent.propTypes = {
  title: PropTypes.string,
  message: PropTypes.string,
}


export default InvalidUserComponent;
