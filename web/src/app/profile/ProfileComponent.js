import { Alert, Button, Col, Grid, Image, Nav, NavItem, Row, PageHeader } from 'react-bootstrap';
import React, { Component } from 'react';
import AlertComponent from './AlertComponent/AlertComponent';
import DonationsComponent from './DonationsComponent/DonationsComponent';
import MyBountiesComponent from './MyBountiesComponent/MyBountiesComponent';
import PropTypes from 'prop-types';
import StatBox from './StatBox/StatBox';
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
      return this.getProfileContent();
    } else if (this.state.activeTab === MY_BOUNTIES_TAB) {
      return this.getBountiesContent();
    } else if (this.state.activeTab === BOUNTIES_TO_STREAMERS_TAB) {
      return this.getDonationsContent();
    } else if (this.state.activeTab === ALERT_TAB) {
      return this.getAlertContent();
    } else {
      return this.getProfileContent();
    }
  }

  getProfileContent() {
    return (
      <div>
        <PageHeader>My Profile</PageHeader>
        <StatBox number={this.props.user.openBounties} label={'Bounties open'}/>
        <StatBox number={this.props.user.completedBounties} label={'Bounties completed'}/>
        <StatBox number={this.props.user.failedBounties} label={'Bounties failed'}/>
        <StatBox number={`$${this.props.user.moneyEarned.toFixed(2)}`} label={'Money earned'}/>
      </div>
    );
  }

  getDonationsContent() {
    // TODO: go over props to make as modular as possible
    return (
      <div>
        <PageHeader>
          Bounties to Streamers
        </PageHeader>
        <DonationsComponent
          listMyDonations={this.props.listMyDonations}
          voteBounty={this.props.voteBounty}
          donations={this.props.donations}
          totalDonations={this.props.totalDonations}
          loading={this.props.showSpinner}
        />
      </div>
    );
  }

  getBountiesContent() {
    return (
      <div>
        <PageHeader>
          My Bounties
        </PageHeader>
        <MyBountiesComponent
          twitchUserName={this.props.user.displayName}
          listMyBounties={this.props.listMyBounties}
          bounties={this.props.bounties}
          totalBounties={this.props.totalBounties}
          loading={this.props.showSpinner}
          acceptBounty={this.props.acceptBounty}
          removeBounty={this.props.removeBounty}
        />
      </div>

    );
  }

  getAlertContent() {
    return (
      <div>
        <PageHeader>Alerts</PageHeader>
        <AlertComponent
          alertChannelId={this.props.user.alertChannelId}
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
            <Image src={this.props.user.profileImageUrl} thumbnail />
            <h2 className="name">{this.props.user.displayName}</h2>
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
                Bounties to Streamers
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
  user: PropTypes.object.isRequired,
  testAlert: PropTypes.func.isRequired,
  listMyDonations: PropTypes.func.isRequired,
  listMyBounties: PropTypes.func.isRequired,
  voteBounty: PropTypes.func.isRequired,
  donations: PropTypes.arrayOf(
    PropTypes.shape({
      streamerName: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
      donationAmount: PropTypes.number.isRequired,
      donationId: PropTypes.string.isRequired
    }).isRequired
  ),
  bounties: PropTypes.array.isRequired, // TODO: validate shape of bounties
  totalBounties: PropTypes.number.isRequired,
  totalDonations: PropTypes.number.isRequired,
  showSpinner: PropTypes.bool.isRequired,
  acceptBounty: PropTypes.func.isRequired,
  removeBounty: PropTypes.func.isRequired,
};

export default ProfileComponent;
