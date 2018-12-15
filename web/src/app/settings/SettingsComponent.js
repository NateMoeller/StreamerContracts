import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Button, Panel, PageHeader, Tabs, Tab, FormGroup, FormControl, Glyphicon } from 'react-bootstrap';
import cx from 'classnames';
import { emailRegex } from '../../commonRegex';
import styles from './SettingsStyles.scss';

const INVALID_EMAIL = 'Please enter a valid email';

class SettingsComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      editPayPal: false,
      newPayPalEmail: '',
      payPalEmailError: null,
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

  isPayPalSubmitEnabled() {
    if (this.state.payPalEmailError && this.state.payPalEmailError.type === null) {
        return true;
    }

    return false;
  }

  submitPayPalEmail(e) {
    e.preventDefault();
    const payload = {
      paypalEmail: this.state.newPayPalEmail
    };

    this.props.updatePayPalEmail(payload);
    this.setState({ editPayPal: false })
  }

  getPayPalContent() {
    if (this.state.editPayPal) {
      const emailErrorMessage = this.state.payPalEmailError !== null ? this.state.payPalEmailError.message : '';

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
            <Button className={styles.button} onClick={() => this.setState({ editPayPal: false })}>Cancel</Button>
          </form>
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
        <Tabs defaultActiveKey={1} id="settings-tabs">
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
      </div>
    );
  }
}

SettingsComponent.defaultProps = {
  payPalEmail: null
};

SettingsComponent.propTypes = {
  payPalEmail: PropTypes.string,
  updatePayPalEmail: PropTypes.func.isRequired,
};

export default SettingsComponent;
