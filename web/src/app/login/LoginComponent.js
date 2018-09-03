import styles from './LoginStyles.scss';
import React, { Component } from 'react';
import { Button } from 'react-bootstrap';

class LoginComponent extends Component {
  render() {
    const link = process.env.REACT_APP_API_HOST + 'login';
    return (
      <div className={styles.loginContent}>
        <div className={styles.contentContainer}>
          <div className={styles.message}>
            Welcome, please login with twitch
          </div>
          <div className={styles.button}>
            <Button href={link} className={styles.btnTwitch}>
              <i className="fa fa-twitch" /> LOG IN WITH TWITCH
            </Button>
          </div>
        </div>
      </div>
    );
  }
}


export default LoginComponent;
