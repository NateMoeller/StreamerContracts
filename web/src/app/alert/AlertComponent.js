import React, { Component } from 'react';
import cx from 'classnames';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import styles from './AlertStyles.scss';

class AlertComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      visible: false
    };

    this.stompClient = null;

    this.receivedAlert = this.receivedAlert.bind(this);
  }

  componentDidMount() {
    const alertChannelId = this.props.match.params.alertChannelId;
    if (alertChannelId) {
      this.connect(alertChannelId);
    } else {
      console.log('No id specified');
    }

  }

  connect(alertChannelId) {
    const socket = new SockJS(`${process.env.REACT_APP_API_HOST}alert-websocket`);
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, (frame) => {
        this.stompClient.subscribe('/alert/' + alertChannelId, this.receivedAlert);
    });
  }

  receivedAlert() {
    this.setState({
      visible: true
    }, () => {
      setTimeout(() => {
        this.setState({
          visible: false
        });
      }, 5000);
    });
  }

  render() {
    const boxStyle = this.state.visible ? cx([styles.alertBox, styles.visible]) : styles.alertBox;

    return (
      <div className={boxStyle}>
        <div className={styles.moneyBox}>$3.0</div>
        <div className={styles.donationBox}>
          <div className={styles.newChallenge}>
            New Bounty from <div className={styles.username}>Test User</div>
          </div>
          <div className={styles.bounty}>Score 15 kills in the next game.</div>
        </div>
      </div>
    );
  }
}

export default AlertComponent;
