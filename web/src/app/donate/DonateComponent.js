import {
  Button,
  FormControl,
  FormGroup,
  ControlLabel,
  InputGroup,
  Grid,
  Row,
  Col
} from 'react-bootstrap';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import PayWithPayPalComponent from './PayWithPayPalComponent';
import styles from './DonateStyles.scss';

const REQUIRED_MESSAGE = 'This field is required';
const TOO_LONG_MESSAGE = 'Too many characters';

const MIN_AMOUNT = 1;
const MAX_AMOUNT = 1000;
const MAX_NAME_LENGTH = 64;
const MAX_BOUNTY_LENGTH = 300;

class DonateComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      username: '',
      amount: '',
      bounty: '',
      showPaymentOptions: false,
      nameError: null,
      amountError: null,
      bountyError: null
    };

    this.submitForm = this.submitForm.bind(this);
    this.validateAmount = this.validateAmount.bind(this);
  }

  submitForm(e) {
    e.preventDefault();
    this.validateName();
    this.validateAmount();
    this.validateBounty();

    if (this.isSubmitEnabled()) {
        this.setState({
          showPaymentOptions: true
        });
    }
  }

  isSubmitEnabled() {
    if ((this.state.nameError && this.state.nameError.type === null) &&
        (this.state.amountError && this.state.amountError.type === null) &&
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

  validateName() {
    let error = { type: null };
    if (this.validateRequiredField(this.state.username)) {
      error = { type: 'error', 'message' : REQUIRED_MESSAGE };
    } else if (this.state.username.length >= MAX_NAME_LENGTH) {
      error = { type: 'error', 'message': TOO_LONG_MESSAGE };
    }

    this.setState({
      nameError: error
    });
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

  nameChange(newName) {
    this.setState({
      username: newName
    }, () => {
      this.validateName();
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

  render() {
    const nameErrorMessage = this.state.nameError !== null ? this.state.nameError.message : '';
    const amountErrorMessage = this.state.amountError !== null ? this.state.amountError.message : '';
    const bountyErrorMessage = this.state.bountyError !== null ? this.state.bountyError.message : '';

    return (
      <div className={styles.donateContainer}>
        <Grid>
          <Row>
            <h1 className={styles.title}>Open a bounty for {this.props.twitchUserName}</h1>
          </Row>
          <Row>
            <Col xs={8} md={6}>
              <form>
                <Row>
                  <Col xs={8} md={6} style={{ paddingLeft: '0px' }}>
                    <FormGroup
                      controlId={'username'}
                      validationState={this.state.nameError ? this.state.nameError.type : null}
                      >
                      <ControlLabel>Username</ControlLabel>
                      <FormControl
                        type='text'
                        placeholder='Enter username'
                        onChange={(e) => this.nameChange(e.target.value)}
                      />
                    </FormGroup>
                    <span className={styles.errorMessage}>{nameErrorMessage}</span>
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
                          placeholder='Enter amount'
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
                      placeholder="Ex: Win the next game with a no scope kill"
                      onChange={(e) => this.bountyChange(e.target.value)}
                      style={{ height: '150px' }}
                    />
                  </FormGroup>
                  <span className={styles.errorMessage}>{bountyErrorMessage}</span>
                </Row>
                <Row>
                  <Button type="submit" bsStyle="success" onClick={this.submitForm} disabled={!this.isSubmitEnabled()}>Complete Payment with PayPal</Button>
                </Row>
                <Row>
                  {this.state.showPaymentOptions ?
                    <PayWithPayPalComponent
                        amount={this.state.amount}
                        streamerPaypalEmail={this.props.streamerPaypalEmail} //TODO: need to get paypal email from API server based on the person we're donating too
                        bounty={this.state.amount}
                        username={this.state.username}
                    />  :
                    null}
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
  twitchUserName: PropTypes.string.isRequired,
  streamerPaypalEmail: PropTypes.string.isRequired
};

export default DonateComponent;
