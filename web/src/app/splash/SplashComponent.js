import {
  Button,
  FormControl,
  FormGroup,
} from 'react-bootstrap';
import React, { Component } from 'react';
import styles from './SplashStyles.scss';

class SplashComponent extends Component {
  render() {
    return (
      <div className={styles.background}>
        <div className={styles.overlay} />
        <div className={styles.header}>
          StreamerContracts.gg
        </div>
        <div className={styles.content}>
          <div className={styles.mainContent}>
            <div className={styles.mainHeader}>
              Donations done differently.
            </div>
            <div className={styles.mainSubText}>
              Donate to your favorite streamers by daring them to complete in game challenges.
            </div>
          </div>
          <div className={styles.formContent}>
            <div className={styles.form}>
              <h3 className={styles.title}>Find out when we're done</h3>
              <div className={styles.fields}>
                <form>
                  <div className={styles.field}>
                    <FormGroup>
                      <FormControl type="text" placeholder="Name" />
                    </FormGroup>
                  </div>
                  <div className={styles.field}>
                    <FormGroup>
                      <FormControl type="text" placeholder="Email" />
                    </FormGroup>
                  </div>
                  <Button className={styles.button} type="submit">Notify me</Button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default SplashComponent;
