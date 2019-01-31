import { Col, Grid, Image, Nav, NavItem, Row, PageHeader } from 'react-bootstrap';
import React, { Component } from 'react';
import MyActiveBountyContainer from '../common/activeBounty/MyActiveBountyContainer';
import AlertComponent from './AlertComponent/AlertComponent';
import DonationsComponent from './DonationsComponent/DonationsComponent';
import MyBountiesComponent from './MyBountiesComponent/MyBountiesComponent';
import { OPEN, COMPLETED, FAILED } from '../BountyState';
import PropTypes from 'prop-types';
import StatBox from './StatBox/StatBox';
import styles from './ProfileStyles.scss'

const ACCOUNT_TAB = 'account';
const MY_BOUNTIES_TAB = 'my_bounties';
const BOUNTIES_TO_STREAMERS_TAB = 'bounties_to_streamers';
const OVERLAY_TAB = 'overlay';

class ProfileComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      activeTab: ACCOUNT_TAB,
      initialBountyFilter: null
    };
  }

  onTabClick(newActiveTab, bountyFilter = null) {
    if (bountyFilter) {
      this.setState({
        activeTab: newActiveTab,
        initialBountyFilter: bountyFilter
      });
    } else {
      this.setState({
        activeTab: newActiveTab
      });
    }
  }

  getContent() {
    if (this.state.activeTab === ACCOUNT_TAB) {
      return this.getProfileContent();
    } else if (this.state.activeTab === MY_BOUNTIES_TAB) {
      return this.getBountiesContent();
    } else if (this.state.activeTab === BOUNTIES_TO_STREAMERS_TAB) {
      return this.getDonationsContent();
    } else if (this.state.activeTab === OVERLAY_TAB) {
      return this.getOverlayContent();
    } else {
      return this.getProfileContent();
    }
  }

  getProfileContent() {
    return (
      <div>
        <PageHeader>My Profile</PageHeader>
        <StatBox number={`$${Number(this.props.user.moneyEarned).toFixed(2)}`} label={'Money earned'} />
        <StatBox number={this.props.user.openContracts} label={'Bounties open'} onClick={() => this.onTabClick(MY_BOUNTIES_TAB, OPEN)}/>
        <StatBox number={this.props.user.completedContracts} label={'Bounties completed'} onClick={() => this.onTabClick(MY_BOUNTIES_TAB, COMPLETED)}/>
        <StatBox number={this.props.user.failedContracts} label={'Bounties failed'} onClick={() => this.onTabClick(MY_BOUNTIES_TAB, FAILED)}/>
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
          listDonorBounties={this.props.listDonorBounties}
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
          listStreamerBounties={this.props.listStreamerBounties}
          bounties={this.props.bounties}
          totalBounties={this.props.totalBounties}
          loading={this.props.showSpinner}
          activateBounty={this.props.activateBounty}
          declineBounty={this.props.declineBounty}
          voteBounty={this.props.voteBounty}
          bountyFilter={this.state.initialBountyFilter}
        />
      </div>

    );
  }

  getOverlayContent() {
    return (
      <div>
        <PageHeader>Overlay</PageHeader>
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
        <MyActiveBountyContainer
          twitchUserName={this.props.user.displayName}
        />
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
              <NavItem eventKey={4} onClick={() => this.onTabClick(OVERLAY_TAB)}>
                Overlay
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
  listDonorBounties: PropTypes.func.isRequired,
  listStreamerBounties: PropTypes.func.isRequired,
  voteBounty: PropTypes.func.isRequired,
  donations: PropTypes.array.isRequired,
  bounties: PropTypes.array.isRequired,
  totalBounties: PropTypes.number.isRequired,
  totalDonations: PropTypes.number.isRequired,
  showSpinner: PropTypes.bool.isRequired,
  activateBounty: PropTypes.func.isRequired,
  declineBounty: PropTypes.func.isRequired,
};

export default ProfileComponent;
