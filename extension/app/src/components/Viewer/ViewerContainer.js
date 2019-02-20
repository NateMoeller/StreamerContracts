import React, { Component } from 'react';
import PropTypes from 'prop-types';
import LoadingComponent from '../../../../../web/src/app/common/loading/LoadingComponent';
import ViewerComponent from './ViewerComponent';

const PAGE_SIZE = 10;

class ViewerContainer extends Component {
  constructor(props) {
    super(props);

    this.state = {
      user: null,
      publicBounties: [],
      totalPublicBounties: 0,
      bountyFilter: 'OPEN' // TODO: duplicate state, should consolidate with ViewerComponent
    }

    this.getPublicBounties = this.getPublicBounties.bind(this);
    this.openBounty = this.openBounty.bind(this);
    this.setBountyFilter = this.setBountyFilter.bind(this);
  }

  componentDidMount() {
    this.props.twitch.listen('broadcast', (target, contentType, body) => {
      const refreshState = JSON.parse(body);
      if (refreshState === 'ALL' || refreshState === this.state.bountyFilter) {
        this.getPublicBounties(0, PAGE_SIZE, this.state.user.displayName, this.state.bountyFilter);
      }
    });

    // get broadcaster data
    this.props.getBroadcaster(this.props.channelId, (data) => {
      this.setState({
        user: data
      });
    });
  }

  getPublicBounties(page, pageSize, twitchUsername, state = null) {
    let url = `bounties/streamerBounties/${page}/${pageSize}?username=${twitchUsername}`;
    if (state) {
      url += `&state=${state}`;
    }
    this.props.Authentication.makeCall(`${process.env.API_HOST}/${url}`).then(response => {
      return response.text();
    }).then(data => {
      const bountyData = JSON.parse(data);
      this.setState({
        publicBounties: bountyData.content,
        totalPublicBounties: bountyData.totalElements
      });
    });
  }

  openBounty() {
    // TODO: make link dynamic, make sure this url is whitelisted
    window.open('https://localhost:3010/user/vgame18', '_blank');
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
      <ViewerComponent
        publicUser={this.state.user}
        publicBounties={this.state.publicBounties}
        totalPublicBounties={this.state.totalPublicBounties}
        getPublicBounties={this.getPublicBounties}
        openBounty={this.openBounty}
        setBountyFilter={this.setBountyFilter}
      />
    );
  }
}

ViewerContainer.propTypes = {
  Authentication: PropTypes.object.isRequired,
  channelId: PropTypes.string.isRequired,
  getBroadcaster: PropTypes.func.isRequired,
  twitch: PropTypes.object.isRequired
};

export default ViewerContainer;
