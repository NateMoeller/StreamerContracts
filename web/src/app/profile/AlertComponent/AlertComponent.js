import React, { Component } from 'react';
import { Button, Form, FormGroup, FormControl } from 'react-bootstrap';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import PropTypes from 'prop-types';
import styles from './AlertComponentStyles.scss';

/* globals window */
class AlertComponent extends Component {
  render() {
    const alertChannelId = process.env.PUBLIC_URL + '/overlay/' + this.props.alertChannelId;

    return (
      <div>
        <div className={styles.message}>
          Copy the below url to use in your favorite streaming program like OBS or XSplit. Do not share this url with anyone.
        </div>
        <Form>
          <FormGroup controlId="formInlineName">
            <div className={styles.alertLabel}>
              Alert url:
            </div>
            <div className={styles.alertTextBox}>
              <FormControl
                type="text"
                readOnly
                value={alertChannelId}
              />
            </div>
            <div className={styles.copyButton}>
              <CopyToClipboard text={alertChannelId}>
                <Button>Copy Link</Button>
              </CopyToClipboard>
            </div>
            <div className={styles.openButton}>
              <Button onClick={()=> window.open(alertChannelId, "_blank", "location=no,width=640,height=480")}>Open Alert</Button>
            </div>
          </FormGroup>
          </Form>
          <div className={styles.message}>
            Use the button below to test the alert
          </div>
          <div>
            <Button onClick={() => {
              this.props.testAlert(this.props.alertChannelId);
            }}>Test Alert</Button>
          </div>
      </div>
    );
  }
}

AlertComponent.propTypes = {
  alertChannelId: PropTypes.string.isRequired,
  testAlert: PropTypes.func.isRequired
}

export default AlertComponent;
