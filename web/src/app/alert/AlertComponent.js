import React, { Component } from 'react';
import cx from 'classnames';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import styles from './AlertStyles.scss';

class AlertComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      username: null,
      amount: null,
      bounty: null,
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

  receivedAlert(alertResponse) {
    const ALERT_LENGTH = 5000;
    const alert = JSON.parse(alertResponse.body);
    this.setState({
      username: alert.username,
      amount: alert.amount,
      bounty: alert.bounty,
      visible: true
    }, () => {
      setTimeout(() => {
        this.setState({
          visible: false
        });
      }, ALERT_LENGTH);
    });
  }

  render() {
    const boxStyle = this.state.visible ? cx([styles.alertBox, styles.visible]) : styles.alertBox;

    return (
      <div className={boxStyle}>
        <div className={styles.moneyBox}>${this.state.amount}</div>
        <div className={styles.donationBox}>
          <div className={styles.newChallenge}>
            New Bounty from <div className={styles.username}>{this.state.username}</div>
          </div>
          <div className={styles.bounty}>{this.state.bounty}</div>
        </div>
      </div>
    );
  }
}

export default AlertComponent;
