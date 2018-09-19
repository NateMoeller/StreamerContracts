import { Button, Tabs, Tab } from 'react-bootstrap';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import styles from './HomeStyles.scss';

class HomeComponent extends Component {
  render() {
    return (
      <div>
        <div className={styles.content}>
          <h1 className={styles.header}>Donations Done <span className={styles.highlight}>Differently</span>.</h1>
          <p className={styles.caption}>
            Give your stream a competitive edge by enabling competitive donations. As a streamer, StreamerContracts allows viewers to open donation
          bounties for you to complete while you play. If you complete the bounty, then the money is yours. If you fail, the money returns to the viewer.
          StreamerContracts gives an every day stream the competitive tension of a high stakes tournament.</p>
          <Button bsStyle="success" href="/login" bsSize="large">Start Now</Button>
        </div>
        <div className={styles.tabs}>
          <Tabs defaultActiveKey={1} id="contract-tabs">
            <Tab eventKey={1} title="Featured Contracts">
              <div className={styles.tabContent}>
                Tab 1 content
              </div>
            </Tab>
            <Tab eventKey={2} title="All Contracts">
              <div className={styles.tabContent}>
                Tab 2 Content
              </div>
            </Tab>
          </Tabs>
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
