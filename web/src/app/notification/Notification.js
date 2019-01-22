import { Glyphicon, Popover, OverlayTrigger } from 'react-bootstrap';
import React, { Component } from 'react';
import cx from 'classnames';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { notificationOperations } from './duck';
import PropTypes from 'prop-types';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import styles from './Notification.scss';

class Notification extends Component {
  constructor(props) {
    super(props);

    this.stompClient = null;
  }

  componentDidMount() {
    const alertChannelId = this.props.alertChannelId;
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
        this.stompClient.subscribe('/alert/' + alertChannelId, this.receivedNotification);
    });
  }

  receivedNotification = (message) => {
    const notification = JSON.parse(message.body);
    const { pushNotification } = this.props;
    pushNotification(notification);
  };

  getItem(title, description, index) {
    const itemStyle = index % 2 !== 0 ? cx(styles.item, styles.itemGray) : styles.item;

    return (
      <div className={itemStyle} key={index}>
        <div className={styles.content}>
          <div className={styles.title}>
            {title}
          </div>
          <div className={styles.description}>
            {description}
          </div>
        </div>
        <div className={styles.remove}>
          <Glyphicon glyph="remove" onClick={() => this.props.clearNotification(index)} />
        </div>
      </div>
    )
  }

  render() {
    const items = this.props.notification.notifications.map((notification, index) => {
      return this.getItem(notification.title, notification.description, index);
    });
    const content = items.length > 0 ? items : <div className={styles.noNotifications}>No notifications</div>;

    const popover = (
      <Popover id="popover-positioned-bottom" className={styles.navPopover}>
        {content}
      </Popover>
    );
    const MAX_NOTIFICATION = 99;
    const number = this.props.notification.notifications.length > MAX_NOTIFICATION ? '99+' : this.props.notification.notifications.length;

    return (
      <OverlayTrigger
        trigger="click"
        placement="bottom"
        overlay={popover}
      >
        <div className={styles.notification}>
          <Glyphicon glyph="bell" className={styles.bellIcon} />
          {this.props.notification.notifications.length > 0 &&
            <div className={styles.redCircle}>{number}</div>
          }
        </div>
      </OverlayTrigger>
    );
  }
}

Notification.propTypes = {
  alertChannelId: PropTypes.string.isRequired
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...notificationOperations }, dispatch);
}

function mapStateToProps(state) {
  return {
    notification: state.notification
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(Notification);
