import * as donateTypes from './duck/types';
import React, { Component } from 'react';
import DonateComponent from './DonateComponent';
import ErrorComponent from './ErrorComponent';
import InvalidUserComponent from '../common/InvalidUser/InvalidUserComponent';
import SuccessComponent from './SuccessComponent';
import LoadingComponent from '../common/loading/LoadingComponent';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { donateOperations } from './duck';
import { publicUserOperations } from '../user/duck';

class DonateContainer extends Component {

  componentWillMount() {
    const streamerUsername = this.props.match.params.twitchUserName;
    this.props.getPublicUser(streamerUsername);
  }

  render() {
    const user = JSON.parse(sessionStorage.getItem('user'));
    const streamerUsername = this.props.match.params.twitchUserName;

    if (this.props.publicUser.getPublicUserLoading || this.props.donate.curDonationState === donateTypes.DONATION_PROCESSING) {
      return <LoadingComponent />
    } else if (this.props.publicUser.publicUser === null) {
      return <InvalidUserComponent />;
    } else if (this.props.publicUser.publicUser && !this.props.publicUser.publicUser.hasPayPalEmail) {
      return (
        <InvalidUserComponent
          title={`Paypal not linked`}
          message={`Once ${streamerUsername} links their paypal, you may open a bounty.`}
        />
      );
    } else if (this.props.donate.curDonationState === donateTypes.DONATION_PROCESSED) {
      return (
        <SuccessComponent
          streamerUserName={streamerUsername}
        />
      );
    } else if (this.props.donate.curDonationState === donateTypes.DONATION_ERROR) {
      return (
        <ErrorComponent />
      );
    }

    return (
      <DonateComponent
        user={user}
        streamerUsername={streamerUsername}
        streamerPaypalEmail='nckackerman+streamer-business@gmail.com' //TODO: need to query API endpoint for this information
        insertBounty={this.props.insertBounty}
      />
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...donateOperations, ...publicUserOperations }, dispatch);
}

function mapStateToProps(state) {
  return {
    publicUser: state.publicUser,
    donate: state.donate
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(DonateContainer);
