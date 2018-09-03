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

class DonateComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      username: '',
      amount: '',
      bounty: '',
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

    if ((this.state.nameError && this.state.nameError.type === null) &&
        (this.state.amountError && this.state.amountError.type === null) &&
        (this.state.bountyError && this.state.bountyError.type === null)) {
          // everything is valid
          const payload = {
            username: this.state.username,
            amount: this.state.amount,
            bounty: this.state.bounty
          };
          this.props.insertBounty(payload);
    }
  }

  validateName() {
    let error = { type: null };
    if (this.state.username === '') {
      error = { type: 'error', 'message' : 'This field is required' };
    }

    this.setState({
      nameError: error
    });
  }

  validateAmount() {
    let error = { type: null };
    if (isNaN(this.state.amount)) {
      error = { type: 'error', message: 'Please enter a valid amount' };
    } else if (this.state.amount === '') {
      error = { type: 'error', message: 'This field is required' }
    }

    this.setState({
      amountError: error
    });
  }

  validateBounty() {
    let error = { type: null };
    if (this.state.bounty === '') {
      error = { type: 'error', message: 'This field is required' };
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
                  <PayWithPayPalComponent/>
                </Row>
                <Row>
                  <Button type="submit" bsStyle="success" onClick={this.submitForm}>Submit</Button>
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
  insertBounty: PropTypes.func.isRequired
};

export default DonateComponent;
