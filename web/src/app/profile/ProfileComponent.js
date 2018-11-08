import { Alert, Button, Col, Grid, Image, Nav, NavItem, Row, PageHeader } from 'react-bootstrap';
import React, { Component } from 'react';
import AlertComponent from './AlertComponent/AlertComponent';
import DonationsComponent from './DonationsComponent/DonationsComponent';
import PropTypes from 'prop-types';
import styles from './ProfileStyles.scss'

const ACCOUNT_TAB = 'account';
const MY_BOUNTIES_TAB = 'my_bounties';
const BOUNTIES_TO_STREAMERS_TAB = 'bounties_to_streamers';
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
    } else if (this.state.activeTab === MY_BOUNTIES_TAB) {
      return this.getBountiesContent();
    } else if (this.state.activeTab === BOUNTIES_TO_STREAMERS_TAB) {
      return this.getBountiesToStreamersContent();
    } else if (this.state.activeTab === ALERT_TAB) {
      return this.getAlertContent();
    } else {
      return this.getAccountContent();
    }
  }

  getAccountContent() {
    return (
      <PageHeader>My Profile</PageHeader>
    );
  }

  getBountiesContent() {
    // TODO: go over props to make as modular as possible
    return (
      <div>
        <PageHeader>My Bounties</PageHeader>
        <DonationsComponent
          listOpenDonations={this.props.listOpenDonations}
          updateContract={this.props.updateContract}
          openBounties={this.props.openBounties}
          totalOpenDonations={this.props.totalOpenDonations}
        />
      </div>
    );
  }

  getBountiesToStreamersContent() {
    return (
      <PageHeader>Bounties to streamers</PageHeader>
    );
  }

  getAlertContent() {
    return (
      <div>
        <PageHeader>Alerts</PageHeader>
        <AlertComponent
          alertChannelId={this.props.alertChannelId}
          testAlert={this.props.testAlert}
        />
      </div>
    );
  }

  getActiveBounty() {
    return (
      <Alert bsStyle="info" onDismiss={() => console.log('dismiss')}>
          <h4>Active bounty:</h4>
          <p>
            Win this next game.
          </p>
          <p>
            <Button bsStyle="danger">Take this action</Button>
            <span> or </span>
            <Button onClick={this.handleDismiss}>Hide Alert</Button>
          </p>
        </Alert>
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
                Profile
              </NavItem>
              <NavItem eventKey={2} onClick={() => this.onTabClick(MY_BOUNTIES_TAB)}>
                My Bounties
              </NavItem>
              <NavItem eventKey={3} onClick={() => this.onTabClick(BOUNTIES_TO_STREAMERS_TAB)}>
                Bounties to streamers
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
  alertChannelId: PropTypes.string.isRequired,
  testAlert: PropTypes.func.isRequired,
  listOpenDonations: PropTypes.func.isRequired,
  updateContract: PropTypes.func.isRequired,
  openBounties: PropTypes.arrayOf(
    PropTypes.shape({
      streamerName: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
      donationAmount: PropTypes.number.isRequired,
      donationId: PropTypes.string.isRequired
    }).isRequired
  ),
  totalOpenDonations: PropTypes.number.isRequired,
};

export default ProfileComponent;
