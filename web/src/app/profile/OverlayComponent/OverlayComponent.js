import React, { Component } from 'react';
import { Button, Form, FormGroup, FormControl } from 'react-bootstrap';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import PropTypes from 'prop-types';
import styles from './OverlayComponentStyles.scss';

/* globals window */
class OverlayComponent extends Component {
  render() {
    const alertChannelId = process.env.REACT_APP_PUBLIC_URL + 'overlay/' + this.props.alertChannelId;

    return (
      <div>
        <div className={styles.message}>
          Copy the below url to use in a browser source for a streaming program like OBS. Do not share this url with anyone.
        </div>
        <Form>
          <FormGroup controlId="formInlineName">
            <div className={styles.alertLabel}>
              Overlay url:
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
          </FormGroup>
        </Form>
        <div className={styles.secondaryTitle}>
          Recommended settings:
        </div>
        <div className={styles.settingsRow}>
          Width: 1500px
        </div>
        <div className={styles.settingsRow}>
          Height: 120px
        </div>
      </div>
    );
  }
}

OverlayComponent.propTypes = {
  alertChannelId: PropTypes.string.isRequired,
  testAlert: PropTypes.func.isRequired
}

export default OverlayComponent;
