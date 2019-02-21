import React, { Component } from 'react';
import PropTypes from 'prop-types';
import LoadingComponent from '../../../../../web/src/app/common/loading/LoadingComponent';
import BroadCasterComponent from './BroadcasterComponent';

const PAGE_SIZE = 10;

class BroadcasterContainer extends Component {
  constructor(props) {
    super(props);

    this.state = {
      user: null,
      bounties: [],
      totalBounties: 0,
      loading: false,
      bountyFilter: 'OPEN' // TODO: duplicate state, should consolidate with StreamerDashboard
    };

    this.listStreamerBounties = this.listStreamerBounties.bind(this);
    this.activateBounty = this.activateBounty.bind(this);
    this.declineBounty = this.declineBounty.bind(this);
    this.voteBounty = this.voteBounty.bind(this);
    this.setBountyFilter = this.setBountyFilter.bind(this);
  }

  componentDidMount() {
    this.props.twitch.listen('broadcast', (target, contentType, body) => {
      const message = JSON.parse(body);
      const refreshState = message.contractState;
      // this.twitch.rig.log(`New PubSub message!\n${target}\n${contentType}\n${body}`);
      if (refreshState === 'ALL' || refreshState === this.state.bountyFilter) {
        this.listStreamerBounties(0, PAGE_SIZE, this.state.user.displayName, this.state.bountyFilter);
      }
    });

    // get broadcaster data
    this.props.getBroadcaster(this.props.channelId, (data) => {
      this.setState({
        user: data
      });
    });
  }

  listStreamerBounties(page, pageSize, username, state = null) {
    this.setState({ loading: true });
    let url = `bounties/streamerBounties/${page}/${pageSize}?username=${username}`;
    if (state) {
      url += `&state=${state}`;
    }
    this.props.Authentication.makeCall(`${process.env.API_HOST}/${url}`).then(response => {
      return response.text();
    }).then(data => {
      const bountyData = JSON.parse(data);
      this.setState({
        loading: false,
        bounties: bountyData.content,
        totalBounties: bountyData.totalElements
      });
    })
  }

  activateBounty(contractId, callback = null) {
    const payload = { contractId };
    this.props.Authentication.makeCall(`${process.env.API_HOST}/bounties/activate`, 'PUT', payload).then(response => {
      return response;
    }).then(data => {
      if (callback) {
        callback();
      }
    });
  }

  declineBounty(contractId, callback = null) {
    const payload = { contractId };
    this.props.Authentication.makeCall(`${process.env.API_HOST}/bounties/decline`, 'PUT', payload).then(response => {
      return response;
    }).then(data => {
      if (callback) {
        callback();
      }
    })
  }

  voteBounty(payload, callback = null) {
    const votePayload = { contractId: payload.contract.contractId, flagCompleted: payload.flagCompleted };

    if (payload.contract.state === 'ACTIVE') {
      this.props.Authentication.makeCall(`${process.env.API_HOST}/bounties/deactivate`, 'PUT', { contractId: payload.contract.contractId }).then(deactivateResponse => {
        this.props.Authentication.makeCall(`${process.env.API_HOST}/bounties/vote`, 'POST', votePayload).then(voteResponse => {
          console.log('voteResponse', voteResponse);
          return voteResponse;
        }).then(data => {
          if (callback) {
            callback();
          }
        });
      });
    } else {
      this.props.Authentication.makeCall(`${process.env.API_HOST}/bounties/vote`, 'POST', votePayload).then(voteResponse => {
        return voteResponse;
      }).then(data => {
        if (callback) {
          callback();
        }
      });
    }
  }

  setBountyFilter(newFilter) {
    this.setState({
      bountyFilter: newFilter
    });
  }

  render() {
    if (!this.state.user) {
      return <LoadingComponent />;
    }

    return (
      <BroadCasterComponent
        user={this.state.user}
        listStreamerBounties={this.listStreamerBounties}
        loading={this.state.loading}
        bounties={this.state.bounties}
        totalBounties={this.state.totalBounties}
        activateBounty={this.activateBounty}
        declineBounty={this.declineBounty}
        voteBounty={this.voteBounty}
        setBountyFilter={this.setBountyFilter}
      />
    );
  }
}

BroadcasterContainer.propTypes = {
  Authentication: PropTypes.object.isRequired,
  channelId: PropTypes.string.isRequired,
  getBroadcaster: PropTypes.func.isRequired,
  twitch: PropTypes.object.isRequired
}

export default BroadcasterContainer;
