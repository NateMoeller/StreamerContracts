import React, { Component } from 'react';
import Overlay from './Overlay';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

// defined alert types
const ALERT_TYPE = {
  ACTIVATE: 'ACTIVATE',
  DEACTIVATE: 'DEACTIVATE'
};

class OverlayContainer extends Component {
  constructor(props) {
    super(props);

    this.stompClient = null;

    this.state = {
      activeBounty: null
    };
  }

  componentDidMount() {
    document.body.style.background = 'transparent'; // TODO: does this work?
    const alertChannelId = this.props.match.params.alertChannelId;
    if (alertChannelId) {
      this.connect(alertChannelId);
    } else {
      console.error('No id specified');
    }
  }

  connect(alertChannelId) {
    const socket = new SockJS(`${process.env.REACT_APP_API_HOST}alert-websocket`);
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, (frame) => {
        this.stompClient.subscribe('/alert/' + alertChannelId, this.receivedAlert);
    });
  }

  receivedAlert = (alertResponse) => {
    const alert = JSON.parse(alertResponse.body);

    if (alert.type === ALERT_TYPE.ACTIVATE) {
      this.activateBounty(alert);
    } else if (alert.type === ALERT_TYPE.DEACTIVATE) {
      this.deactivateBounty();
    }
  }

  activateBounty = (activeBounty) => {
    this.setState({
      activeBounty
    });
  }

  deactivateBounty = () => {
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

export default OverlayContainer;
