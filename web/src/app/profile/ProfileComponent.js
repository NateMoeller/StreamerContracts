import { Col, Grid, Image, Nav, NavItem, Row, PageHeader } from 'react-bootstrap';
import React, { Component } from 'react';
import AlertComponent from './AlertComponent/AlertComponent';
import PropTypes from 'prop-types';
import styles from './ProfileStyles.scss'

const ACCOUNT_TAB = 'account';
const OPEN_CONTRACTS_TAB = 'open_contracts';
const COMPLETED_CONTRACTS_TAB = 'completed_contracts';
const ALERT_TAB = 'alerts';

class ProfileComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      activeTab: ACCOUNT_TAB
    };
  }

  onTabClick(newActiveTab) {
    this.setState({
      activeTab: newActiveTab
    });
  }

  getContent() {
    if (this.state.activeTab === ACCOUNT_TAB) {
      return this.getAccountContent();
    } else if (this.state.activeTab === OPEN_CONTRACTS_TAB) {
      return this.getOpenContractsContent();
    } else if (this.state.activeTab === COMPLETED_CONTRACTS_TAB) {
      return this.getCompletedContractsContent();
    } else {
      return this.getAlertContent();
    }
  }

  getAccountContent() {
    return (
      <PageHeader>My Profile</PageHeader>
    );
  }

  getOpenContractsContent() {
    return (
      <PageHeader>Open Contracts</PageHeader>
    );
  }

  getCompletedContractsContent() {
    return (
      <PageHeader>Completed Contracts</PageHeader>
    );
  }

  getAlertContent() {
    return (
      <div>
        <PageHeader>Alerts</PageHeader>
        <AlertComponent
          alertKey={this.props.alertKey}
          testAlert={this.props.testAlert}
        />
      </div>
    );
  }

  render() {
    const content = this.getContent();

    return (
      <Grid className="content">
        <Col xs={3} md={2} className={styles.sidebar}>
          <Row>
            <Image src={this.props.imageUrl} thumbnail />
            <h2 className="name">{this.props.twitchUserName}</h2>
          </Row>
          <Row>
            <Nav stacked>
              <NavItem eventKey={1} onClick={() => this.onTabClick(ACCOUNT_TAB)}>
                Account
              </NavItem>
              <NavItem eventKey={2} onClick={() => this.onTabClick(OPEN_CONTRACTS_TAB)}>
                Open Contracts
              </NavItem>
              <NavItem eventKey={3} onClick={() => this.onTabClick(COMPLETED_CONTRACTS_TAB)}>
                Completed Contracts
              </NavItem>
              <NavItem eventKey={4} onClick={() => this.onTabClick(ALERT_TAB)}>
                Alerts
              </NavItem>
            </Nav>
          </Row>
        </Col>
        <Col xs={13} md={9}>
          {content}
        </Col>
      </Grid>
    );
  }
}

ProfileComponent.propTypes = {
  twitchUserName: PropTypes.string.isRequired,
  imageUrl: PropTypes.string.isRequired,
  alertKey: PropTypes.string.isRequired,
  testAlert: PropTypes.func.isRequired
};

export default ProfileComponent;
