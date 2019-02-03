import { Button, Tabs, Tab } from 'react-bootstrap';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';
import geoff from '../../resources/splash/geoff_right_blackWhite_noWindow2.jpg';
import styles from './HomeStyles.scss';

class HomeComponent extends Component {
  render() {
    return (
      <div className={styles.content}>
        <div className={cx(styles.section, styles.backgroundImage)}>
          <div className={styles.overlay} />
          <div className={cx(styles.section1, "container")}>
            <div className={styles.left}>
              <p>Build</p>
              <p>Big</p>
              <p>Moments.</p>
            </div>
            <div className={styles.right}>
              right content
            </div>
          </div>
        </div>
        <div className={cx(styles.section, styles.section2)}>
          <div className="container">
            ABOUT SECTION
          </div>
        </div>
        <div className={styles.section}>
          <div className="container">
            NEW SECTION
          </div>
        </div>
      </div>

    );
  }
}

HomeComponent.propTypes = {
  fetchApi: PropTypes.func.isRequired,
  showSpinner: PropTypes.bool.isRequired
};


export default HomeComponent;
