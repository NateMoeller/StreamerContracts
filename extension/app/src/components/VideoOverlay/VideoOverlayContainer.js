import React, { Component } from 'react';
import Authentication from '../../util/Authentication/Authentication';
import Overlay from '../../../../../web/src/app/overlay/Overlay';

const MESSAGE_TYPE = {
  ACTIVATE: 'ACTIVATE',
  DEACTIVATE: 'DEACTIVATE'
};

class VideoOverlayContainer extends Component {
  constructor(props) {
    super(props);

    this.Authentication = new Authentication();
    this.twitch = window.Twitch ? window.Twitch.ext : null;

    this.state = {
      activeBounty: null
    };
  }

  componentDidMount() {
    this.twitch.listen('broadcast', (target, contentType, body) => {
      const message = JSON.parse(body);
      if (message.type === MESSAGE_TYPE.ACTIVATE) {
        this.activateBounty(message);
      } else if (message.type === MESSAGE_TYPE.DEACTIVATE) {
        this.deactivateBounty();
      }
    });
  }

  activateBounty(activeBounty) {
    this.setState({
      activeBounty
    });
  }

  deactivateBounty() {
    this.setState({
      activeBounty: null
    });
  }

  render() {
    return (
      <Overlay
        activeBounty={this.state.activeBounty}
      />
    );
  }
}

export default VideoOverlayContainer;
