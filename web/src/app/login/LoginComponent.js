import styles from './LoginStyles.scss';
import React, { Component } from 'react';
import { Button } from 'react-bootstrap';

class LoginComponent extends Component {
  render() {
    const link = 'api/login';
    return (
      <div className={styles.loginContent}>
        <Button href={link} className={styles.btnTwitch}>
          <i className="fa fa-twitch" /> LOG IN WITH TWITCH
        </Button>
      </div>
    );
  }
}


export default LoginComponent;
