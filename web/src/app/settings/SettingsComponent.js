import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Button, Panel, PageHeader, Tabs, Tab, FormGroup, FormControl, Glyphicon, Tooltip, OverlayTrigger } from 'react-bootstrap';
import cx from 'classnames';
import OverlayComponent from '../profile/OverlayComponent/OverlayComponent';
import { emailRegex } from '../../commonRegex';
import profileStyles from '../profile/ProfileStyles.scss';
import styles from './SettingsStyles.scss';

const INVALID_EMAIL = 'Please enter a valid email';

class SettingsComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      editPayPal: false,
      newPayPalEmail: '',
      payPalEmailError: null,
      isBusinessEmailChecked: false
    };
  }

  validateEmail() {
    let error = { type: null };
    if (!emailRegex.test(this.state.newPayPalEmail)) {
      error = { type: 'error', message: INVALID_EMAIL };
    }

    this.setState({
      payPalEmailError: error
    });
  }

  payPalEmailChange(payPalEmail) {
    this.setState({
      newPayPalEmail: payPalEmail
    }, () => {
      this.validateEmail();
    });
  }

  isBusinessEmailChecked = (e) => {
    this.setState({
      isBusinessEmailChecked: e.target.checked
    });
  }

  isPayPalSubmitEnabled() {
    if (this.state.payPalEmailError && this.state.payPalEmailError.type === null && this.state.isBusinessEmailChecked) {
        return true;
    }

    return false;
  }

  submitPayPalEmail(e) {
    e.preventDefault();
    const payload = {
      paypalEmail: this.state.newPayPalEmail,
      isBusinessEmail: this.state.isBusinessEmailChecked
    };

    this.props.updatePayPalEmail(payload);
    this.setState({ editPayPal: false, isBusinessEmailChecked: false });
  }

  getPayPalContent() {
    if (this.state.editPayPal) {
      const emailErrorMessage = this.state.payPalEmailError !== null ? this.state.payPalEmailError.message : '';
      const tooltip = (
        <Tooltip id="tooltip">
          Important! We cannot authorize payments if your paypal account is not a business account.
        </Tooltip>
      );

      return (
        <Panel.Body className={styles.panelBody}>
          <form>
            <FormGroup
              controlId="payPalEmail"
              validationState={this.state.payPalEmailError ? this.state.payPalEmailError.type : null}
              className={styles.emailField}
            >
              <FormControl
                type="text"
                value={this.state.newPayPalEmail}
                placeholder="PayPal Email"
                onChange={(e) => this.payPalEmailChange(e.target.value)}
              />
            </FormGroup>
            <span className={styles.errorMessage}>{emailErrorMessage}</span>
            <Button className={cx(styles.button, styles.submitButton)} onClick={(e) => this.submitPayPalEmail(e)} disabled={!this.isPayPalSubmitEnabled()}>Submit</Button>
            <Button className={styles.button} onClick={() => this.setState({ editPayPal: false, isBusinessEmailChecked: false })}>Cancel</Button>
          </form>
          <div className={styles.checkboxDiv}>
            <OverlayTrigger placement="top" overlay={tooltip}>
              <input
                type="checkbox"
                className={styles.checkbox}
                checked={this.state.isBusinessEmailChecked}
                onChange={this.isBusinessEmailChecked}
              />
            </OverlayTrigger>

            <span>This is a business paypal email.</span>
          </div>
        </Panel.Body>
      );
    } else if (this.props.payPalEmail !== null) {
      return (
        <Panel.Body className={styles.panelBody}>
          <Glyphicon glyph="ok" className={styles.successIcon} />
          <div className={styles.text}>Email: {this.props.payPalEmail}</div>
          <Button className={styles.button} onClick={() => this.setState({ editPayPal: true })}>Edit</Button>
        </Panel.Body>
      );
    }

    return (
      <Panel.Body className={styles.panelBody}>
        <Glyphicon glyph="remove" className={styles.errorIcon} />
        <div className={styles.text}>PayPal email not linked</div>
        <Button className={styles.button} onClick={() => this.setState({ editPayPal: true })}>Edit</Button>
      </Panel.Body>
    );
  }

  render() {
    return (
      <div>
        <PageHeader>Settings</PageHeader>
        <div className={profileStyles.section}>
          <div className={profileStyles.secondaryHeader}>Overlay</div>
          <OverlayComponent
            alertChannelId={this.props.user.alertChannelId}
            testAlert={() => console.log('test alert')}
          />
        </div>
        {!this.props.isExtension &&
          <Tabs defaultActiveKey={1} id="settings-tabs" className={styles.tabs}>
            <Tab eventKey={1} title="Payments">
              <div className={styles.settingsContent}>
                <Panel>
                  <Panel.Heading className={styles.heading}>
                    <i className="fa fa-paypal" />
                    <div className={styles.headingText}>PayPal</div>
                  </Panel.Heading>
                  {this.getPayPalContent()}
                </Panel>
              </div>
            </Tab>
          </Tabs>
        }
      </div>
    );
  }
}

SettingsComponent.defaultProps = {
  payPalEmail: null,
  isExtension: false
};

SettingsComponent.propTypes = {
  user: PropTypes.object.isRequired,
  payPalEmail: PropTypes.string,
  updatePayPalEmail: PropTypes.func.isRequired,
  isExtension: PropTypes.bool,
};

export default SettingsComponent;
