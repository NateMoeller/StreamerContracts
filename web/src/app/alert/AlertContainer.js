import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import AlertComponent from './AlertComponent';

const INITIAL_RECALL_DELAY = 500; // how in (milli)  to wait if the first attempt at showing an alert fails
const RECALL_DELAY_INCREMENT = 500; // time (in ms) added to the recallDelay after each failed attempt
const ALERT_LENGTH = 5000;
const TIME_BETWEEN_ALERTS = 1000;

const alertDivID = 'alertContainer';

class AlertContainer extends Component {
  constructor(props) {
    super(props);

    this.stompClient = null;
    this.showOrQueueAlert = this.createQueue();
  }

  componentDidMount() {
    document.body.style.background = 'transparent';
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
    this.showOrQueueAlert(alert);
  }

  renderAlert(alert) {
    const target = document.getElementById(alertDivID);
    ReactDOM.render(<AlertComponent username={alert.username} amount={alert.amount} bounty={alert.bounty} alertLength={ALERT_LENGTH} />, target);
  }

  hideAlert() {
    const target = document.getElementById(alertDivID);
    ReactDOM.unmountComponentAtNode(target);
  }

  show(alert) {
    if (!document.getElementById(alertDivID).hasChildNodes()) {
      // render alert if not alert is displayed
      const renderTimeout = ALERT_LENGTH;
      this.renderAlert(alert);
      setTimeout(this.hideAlert, renderTimeout + TIME_BETWEEN_ALERTS);

      return true;
    }

    return false;
  }

  createQueue() {
    this.alertQueue = [];

    this.isVisible = false; // is the showAlert funcion in progress?
    this.currentRecallDelay = INITIAL_RECALL_DELAY;

    this.showAlert = () => {
      if (this.alertQueue.length === 0) {
          this.isVisible = false;
          return;
      }

      this.isVisible = true;
      const current = this.alertQueue.pop();
      if (this.show(current)) {
        this.currentRecallDelay = INITIAL_RECALL_DELAY;
        setTimeout(() => this.showAlert(), ALERT_LENGTH);
      } else {
        // If this.show failed, re-add the current alert to the front of the queue
        this.alertQueue.unshift(current);
        setTimeout(() => this.showAlert(), this.currentRecallDelay);
        this.currentRecallDelay += RECALL_DELAY_INCREMENT; // backoff by increment
      }
    }

    return (alert) => {
      this.alertQueue.push(alert);
      if (!this.isVisible) {
          this.showAlert();
      }
    };
  }

  render() {
    return (
      <div id={alertDivID} />
    );
  }
}

export default AlertContainer;
