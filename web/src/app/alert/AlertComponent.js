import React, { Component } from 'react';
import cx from 'classnames';
import PropTypes from 'prop-types';
import styles from './AlertStyles.scss';

const ALERT_LENGTH = 5000;
const BUFFER = 100;

class AlertComponent extends Component {
  constructor(props) {
    super(props);

    this.stompClient = null;

    this.state = {
      visible: false
    };
  }

  componentDidMount() {
    setTimeout(() => {
      this.setState({
        visible: true,
      });
    }, BUFFER);

    setTimeout(() => {
      this.setState({
        visible: false
      });
    }, ALERT_LENGTH);
  }

  render() {
    const boxStyle = this.state.visible ? cx([styles.alertBox, styles.visible]) : styles.alertBox;

    return (
      <div className={boxStyle}>
        <div className={styles.moneyBox}>${this.props.amount}</div>
        <div className={styles.donationBox}>
          <div className={styles.newChallenge}>
            New Bounty from <div className={styles.username}>{this.props.username}</div>
          </div>
          <div className={styles.bounty}>{this.props.bounty}</div>
        </div>
      </div>
    );
  }
}

AlertComponent.propTypes = {
  bounty: PropTypes.string.isRequired,
  username: PropTypes.string.isRequired,
  amount: PropTypes.number.isRequired,
}

export default AlertComponent;
