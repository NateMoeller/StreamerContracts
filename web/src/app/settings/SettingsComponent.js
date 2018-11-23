import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Button, Panel, PageHeader, Tabs, Tab, FormGroup, FormControl } from 'react-bootstrap';
import cx from 'classnames';
import styles from './SettingsStyles.scss';

class SettingsComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      editPayPal: false,
      newPayPalEmail: props.payPalEmail
    };
  }

  getPayPalContent() {
    if (this.state.editPayPal) {
      return (
        <Panel.Body>
          <form>
            <FormGroup
              controlId="payPalEmail"
              // validationState={this.getValidationState()}
              className={styles.emailField}
            >
              <FormControl
                type="text"
                value={this.state.newPayPalEmail}
                placeholder="PayPal Email"
                onChange={(e) => this.setState({
                  newPayPalEmail: e.target.value
                })}
              />
            </FormGroup>
            <Button className={cx(styles.button, styles.submitButton)} onClick={() => this.setState({ editPayPal: false })}>Submit</Button>
          </form>
        </Panel.Body>
      );
    }

    return (
      <Panel.Body>
        Email: {this.props.payPalEmail}
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
  payPalEmail: PropTypes.string
};

export default SettingsComponent;
