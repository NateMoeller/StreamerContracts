import {
  Button,
  FormControl,
  FormGroup,
  ControlLabel,
  InputGroup,
  Grid,
  Row,
  Col,
  PageHeader
} from 'react-bootstrap';
import React, { Component } from 'react';
import DonationCheckoutComponent from './DonateCheckoutComponent';
import PropTypes from 'prop-types';
import styles from './DonateStyles.scss';

const REQUIRED_MESSAGE = 'This field is required';
const TOO_LONG_MESSAGE = 'Too many characters';

const MIN_AMOUNT = 1;
const MAX_AMOUNT = 1000;
const MAX_BOUNTY_LENGTH = 300;

class DonateComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      amount: '',
      bounty: '',
      showPaymentOptions: false,
      amountError: null,
      bountyError: null
    };

    this.submitForm = this.submitForm.bind(this);
    this.validateAmount = this.validateAmount.bind(this);
    this.showBountyForm = this.showBountyForm.bind(this);
  }

  submitForm(e) {
    e.preventDefault();
    this.validateAmount();
    this.validateBounty();

    if (this.isSubmitEnabled()) {
        this.setState({
          showPaymentOptions: true
        });
    }
  }

  isSubmitEnabled() {
    if ((this.state.amountError && this.state.amountError.type === null) &&
        (this.state.bountyError && this.state.bountyError.type === null)) {
          return true;
    }

    return false;
  }

  validateRequiredField(value) {
    if (value === '') {
      return true;
    }

    return false;
  }

  validateAmount() {
    const regex = /^[1-9]\d*(?:\.\d{0,2})?$/;
    let error = { type: null };
    if (this.validateRequiredField(this.state.amount)) {
      error = { type: 'error', message: REQUIRED_MESSAGE }
    } else if (!regex.test(this.state.amount)) {
      error = { type: 'error', message: 'Please enter a valid amount' };
    } else if (this.state.amount < MIN_AMOUNT) {
      error = { type: 'error', message: `The minimum amount is $${MIN_AMOUNT}` };
    } else if (this.state.amount > MAX_AMOUNT) {
      error = { type: 'error', message: `The maximum amount is $${MAX_AMOUNT}` };
    }

    this.setState({
      amountError: error
    });
  }

  validateBounty() {
    let error = { type: null };
    if (this.validateRequiredField(this.state.bounty)) {
      error = { type: 'error', message: REQUIRED_MESSAGE };
    } else if (this.state.bounty.length >= MAX_BOUNTY_LENGTH) {
      error = { type: 'error', message: TOO_LONG_MESSAGE };
    }

    this.setState({
      bountyError: error
    });
  }

  amountChange(newValue) {
    this.setState({
      amount: newValue
    }, () => {
      this.validateAmount();
    });
  }

  bountyChange(newBounty) {
    this.setState({
      bounty: newBounty
    }, () => {
      this.validateBounty();
    });
  }

  showBountyForm() {
    this.setState({
      showPaymentOptions: false
    });
  }

  render() {
    const amountErrorMessage = this.state.amountError !== null ? this.state.amountError.message : '';
    const bountyErrorMessage = this.state.bountyError !== null ? this.state.bountyError.message : '';

    if (this.state.showPaymentOptions) {
      return (
        <DonationCheckoutComponent
          goBack={this.showBountyForm}
          amount={this.state.amount}
          streamerPaypalEmail={this.props.streamerPaypalEmail}
          bounty={this.state.bounty}
          username={this.props.user.displayName}
          insertBounty={this.props.insertBounty}
          streamerUsername={this.props.streamerUsername}
        />
      );
    }

    return (
      <div className={styles.donateContainer}>
        <Grid>
          <Row>
            <PageHeader className={styles.title}>Open a bounty for {this.props.streamerUsername}</PageHeader>
          </Row>
          <Row>
            <Col xs={8} md={6}>
              <form>
                <Row>
                  <Col xs={8} md={6} style={{ paddingLeft: '0px' }}>
                    <FormGroup
                      controlId={'username'}
                      >
                      <ControlLabel>Username</ControlLabel>
                      <FormControl
                        type='text'
                        value={this.props.user.displayName}
                        readOnly
                      />
                    </FormGroup>
                  </Col>
                  <Col xs={8} md={6}>
                    <FormGroup
                      controlId={'amount'}
                      validationState={this.state.amountError ? this.state.amountError.type : null}
                      >
                      <ControlLabel>Amount</ControlLabel>
                      <InputGroup>
                        <InputGroup.Addon>$</InputGroup.Addon>
                        <FormControl
                          type="text"
                          value={this.state.amount}
                          placeholder='5.00'
                          onChange={(e) => this.amountChange(e.target.value)}
                        />
                      </InputGroup>
                    </FormGroup>
                      <span className={styles.errorMessage}>{amountErrorMessage}</span>
                  </Col>
                </Row>
                <Row>
                  <FormGroup
                    controlId="bountyMessage"
                    validationState={this.state.bountyError ? this.state.bountyError.type : null}
                    >
                    <ControlLabel>Bounty message</ControlLabel>
                    <FormControl
                      componentClass="textarea"
                      value={this.state.bounty}
                      placeholder="Ex: Win the next game with a no scope kill"
                      onChange={(e) => this.bountyChange(e.target.value)}
                      style={{ height: '150px' }}
                    />
                  </FormGroup>
                  <span className={styles.errorMessage}>{bountyErrorMessage}</span>
                </Row>
                <Row>
                  <Button type="submit" bsStyle="success" onClick={this.submitForm} disabled={!this.isSubmitEnabled()}>
                    Open Challenge
                  </Button>
                </Row>
              </form>
            </Col>
          </Row>
        </Grid>
      </div>
    );
  }
}

DonateComponent.propTypes = {
  user: PropTypes.object.isRequired,
  streamerUsername: PropTypes.string.isRequired,
  streamerPaypalEmail: PropTypes.string.isRequired,
  insertBounty: PropTypes.func.isRequired
};

export default DonateComponent;
