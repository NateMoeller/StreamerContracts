import {
  PageHeader
} from 'react-bootstrap';
import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { LinkContainer } from 'react-router-bootstrap';
import styles from './InvalidUserStyles.scss';

class InvalidUserComponent extends Component {
  render() {
    return (
      <div className={styles.missingContainer}>
        <PageHeader>Oops! User not found</PageHeader>
        <div>
          You can try finding them in the <LinkContainer exact to="/about"><Link to='/about'>streamer directory</Link></LinkContainer>
        </div>
      </div>
    );
  }
}

export default InvalidUserComponent;
