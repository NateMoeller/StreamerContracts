import React, { Component } from 'react';
import { Button, Form, FormGroup, FormControl } from 'react-bootstrap';
import { CopyToClipboard } from 'react-copy-to-clipboard';
import PropTypes from 'prop-types';
import logoDark from '../../../resources/logo_dark.png';
import styles from './ProfileLink.scss';

class ProfileLink extends Component {
  render() {
    const profileLink = process.env.REACT_APP_PUBLIC_URL + 'user/' + this.props.username;

    return (
      <div>
        <div className={styles.message}>
          Share this link on your stream so your viewers can send you bounties. You may use the below image as a twitch panel header.
        </div>
        <Form>
          <FormGroup controlId="formInlineName">
            <div className={styles.profileLabel}>
              Image:
            </div>
            <div className={styles.profileTextBox}>
              <img src={logoDark} alt="Bounty Streamer" width="200" height="50" />
            </div>
          </FormGroup>
        </Form>
        <Form>
          <FormGroup controlId="formInlineName">
            <div className={styles.profileLabel}>
              Profile url:
            </div>
            <div className={styles.profileTextBox}>
              <FormControl
                type="text"
                readOnly
                value={profileLink}
              />
            </div>
            <div className={styles.copyButton}>
              <CopyToClipboard text={profileLink}>
                <Button>Copy Link</Button>
              </CopyToClipboard>
            </div>
          </FormGroup>
          </Form>
      </div>
    );
  }
}

ProfileLink.propTypes = {
  username: PropTypes.string.isRequired
};

export default ProfileLink;
