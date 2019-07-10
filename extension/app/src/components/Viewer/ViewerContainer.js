import React, { Component } from 'react';
import PropTypes from 'prop-types';
import LoadingComponent from '../../../../../web/src/app/common/loading/LoadingComponent';
import ViewerComponent from './ViewerComponent';
import DonateContainer from './DonateContainer';

const PAGE_SIZE = 10;

class ViewerContainer extends Component {
  constructor(props) {
    super(props);

    this.state = {
      user: null,
      publicBounties: [],
      totalPublicBounties: 0,
      bountyFilter: 'OPEN', // TODO: duplicate state, should consolidate with ViewerComponent
      showOpenForm: false,
      viewerUserName: null // if this is null, they do not exist in our db
    }

    this.getPublicBounties = this.getPublicBounties.bind(this);
    this.toggleOpenForm = this.toggleOpenForm.bind(this);
    this.setBountyFilter = this.setBountyFilter.bind(this);
  }

  componentDidMount() {
    this.props.twitch.listen('broadcast', (target, contentType, body) => {
      const message = JSON.parse(body);
      const refreshState = message.contractState;
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

    const viewerUserId = this.props.Authentication.state.user_id;
    if (viewerUserId) {
      this.props.getViewer(this.props.Authentication.state.user_id, (data) => {
        this.setState({
          viewerUserName: data.displayName 
        });
      });
    } else {
      this.setState({
        viewerUserName: null
      });
    }
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

  toggleOpenForm(username) {
    if (this.state.viewerUserName) {
      this.setState({
        showOpenForm: !this.state.showOpenForm
      });
    } else {
      const link = `${process.env.REACT_APP_PUBLIC_URL}user/${username}`;
      window.open(link, '_blank');
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

    if (this.state.showOpenForm) {
      return (
        <DonateContainer
          Authentication={this.props.Authentication}
          toggleOpenForm={this.toggleOpenForm}
          streamerUsername={this.state.user.displayName}
          viewerUserName={this.state.viewerUserName}
        />
      );
    }

    return (
      <ViewerComponent
        publicUser={this.state.user}
        publicBounties={this.state.publicBounties}
        totalPublicBounties={this.state.totalPublicBounties}
        getPublicBounties={this.getPublicBounties}
        toggleOpenForm={this.toggleOpenForm}
        showOpenForm={this.state.showOpenForm}
        setBountyFilter={this.setBountyFilter}
      />
    );
  }
}

ViewerContainer.propTypes = {
  Authentication: PropTypes.object.isRequired,
  channelId: PropTypes.string.isRequired,
  getBroadcaster: PropTypes.func.isRequired,
  getViewer: PropTypes.func.isRequired,
  twitch: PropTypes.object.isRequired
};

export default ViewerContainer;
