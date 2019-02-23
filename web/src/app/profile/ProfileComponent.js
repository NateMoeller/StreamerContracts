import { Col, Grid, Image, Nav, NavItem, Row, PageHeader, Glyphicon } from 'react-bootstrap';
import React, { Component } from 'react';
import MyActiveBountyContainer from '../common/activeBounty/MyActiveBountyContainer';
import OverlayComponent from './OverlayComponent/OverlayComponent';
import ProfileLink from './ProfileLink/ProfileLink';
import DonationsComponent from './DonationsComponent/DonationsComponent';
import StreamerDashboard from './StreamerDashboard/StreamerDashboard';
import { OPEN, COMPLETED, FAILED } from '../BountyState';
import PropTypes from 'prop-types';
import StatBox from './StatBox/StatBox';
import styles from './ProfileStyles.scss'

const ACCOUNT_TAB = 'account';
const STREAMER_DASHBOARD = 'streamer_dashboard';
const VIEWER_DASHBOARD = 'viewer_dashboard';

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
    } else if (this.state.activeTab === STREAMER_DASHBOARD) {
      return this.getBountiesContent();
    } else if (this.state.activeTab === VIEWER_DASHBOARD) {
      return this.getDonationsContent();
    } else {
      return this.getProfileContent();
    }
  }

  getProfileContent() {
    return (
      <div>
        <PageHeader>My Profile</PageHeader>
        <StatBox number={`$${Number(this.props.user.moneyEarned).toFixed(2)}`} label={'Money earned'} />
        <StatBox number={this.props.user.openContracts} label={'Bounties open'} onClick={() => this.onTabClick(STREAMER_DASHBOARD, OPEN)}/>
        <StatBox number={this.props.user.completedContracts} label={'Bounties completed'} onClick={() => this.onTabClick(STREAMER_DASHBOARD, COMPLETED)}/>
        <StatBox number={this.props.user.failedContracts} label={'Bounties failed'} onClick={() => this.onTabClick(STREAMER_DASHBOARD, FAILED)}/>
        <div className={styles.section}>
          <div className={styles.secondaryHeader}>Overlay</div>
          <OverlayComponent
            alertChannelId={this.props.user.alertChannelId}
            testAlert={this.props.testAlert}
          />
        </div>
        <div className={styles.section}>
          <div className={styles.secondaryHeader}>Profile link</div>
          <ProfileLink
            username={this.props.user.displayName}
          />
        </div>
      </div>
    );
  }

  getDonationsContent() {
    // TODO: go over props to make as modular as possible
    return (
      <div>
        <PageHeader>
          Viewer Dashboard
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
          Streamer Dashboard
        </PageHeader>
        <StreamerDashboard
          twitchUserName={this.props.user.displayName}
          listStreamerBounties={this.props.listStreamerBounties}
          bounties={this.props.bounties}
          totalBounties={this.props.totalBounties}
          loading={this.props.showSpinner}
          activateBounty={this.props.activateBounty}
          declineBounty={this.props.declineBounty}
          voteBounty={this.props.voteBounty}
          bountyFilter={this.state.initialBountyFilter}
          getSettings={this.props.getSettings}
          settings={this.props.settings}
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
            <h2 className={styles.name} title={this.props.user.displayName}>{this.props.user.displayName}</h2>
          </Row>
          <Row>
            <Nav stacked>
              <NavItem eventKey={1} onClick={() => this.onTabClick(ACCOUNT_TAB)}>
                <i className="fa fa-gear" /> General
              </NavItem>
              <NavItem eventKey={2} onClick={() => this.onTabClick(STREAMER_DASHBOARD)}>
                <i className="fa fa-gamepad" /> Streamer Dashboard
              </NavItem>
              <NavItem eventKey={3} onClick={() => this.onTabClick(VIEWER_DASHBOARD)}>
                <Glyphicon glyph="user" /> Viewer Dashboard
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
  getSettings: PropTypes.func.isRequired,
  settings: PropTypes.object.isRequired
};

export default ProfileComponent;
