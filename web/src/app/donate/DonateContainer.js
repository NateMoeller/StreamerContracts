import React, { Component } from 'react';
import DonateComponent from './DonateComponent';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { donateOperations } from './duck';

class DonateContainer extends Component {
  constructor(props) {
    super(props);
  }

  // TODO: make sure this user exists
  render() {
    const twitchUserName = this.props.match.params.twitchUserName;

    return (
      <DonateComponent
        twitchUserName={twitchUserName}
        insertBounty={this.props.insertBounty}
      />
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...donateOperations }, dispatch);
}

export default connect(null, mapDispatchToProps)(DonateContainer);
