import React, { Component } from 'react';
import DonateComponent from './DonateComponent';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { donateOperations } from './duck';

class DonateContainer extends Component {

  // TODO: make sure this user exists
  render() {
    const twitchUserName = this.props.match.params.twitchUserName;

    return (
      <DonateComponent
        twitchUserName={twitchUserName}
        streamerPaypalEmail='nckackerman+streamer-business@gmail.com' //TODO: need to query API endpoint for this information
      />
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...donateOperations }, dispatch);
}

export default connect(null, mapDispatchToProps)(DonateContainer);
