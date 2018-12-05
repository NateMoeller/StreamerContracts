import { Button, Col, Grid, Image, Row, Tabs, Tab } from 'react-bootstrap';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import profileStyles from '../profile/ProfileStyles.scss';
import styles from './UserComponentStyles.scss';

class UserComponent extends Component {
  render() {
    const donateUrl = `/donate/${this.props.publicUser.displayName}`;

    return (
      <Grid className="content">
        <Col xs={3} md={2} className={profileStyles.sidebar}>
          <Row>
            <Image src={this.props.publicUser.profileImageUrl} thumbnail />
            <h2 className="name">{this.props.publicUser.displayName}</h2>
          </Row>
          <Row>
            <Button bsStyle="primary" className={styles.openBountyButton} href={donateUrl}>Open Bounty</Button>
          </Row>
        </Col>
        <Col xs={13} md={9}>
          <div className={styles.mainContent}>
            <Tabs defaultActiveKey={1} id="contract-tabs">
              <Tab eventKey={1} title="Open Bounties">
                tab 1 content
              </Tab>
              <Tab eventKey={2} title="Completed Bounties">
                tab 2 content
              </Tab>
              <Tab eventKey={3} title="Failed Bounties">
                tab 2 content
              </Tab>
            </Tabs>
          </div>
        </Col>
      </Grid>
    )
  }
}

UserComponent.propTypes = {
  publicUser: PropTypes.object.isRequired
};

export default UserComponent;
