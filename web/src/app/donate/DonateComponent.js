import {
  Button,
  FormControl,
  FormGroup,
  ControlLabel,
  InputGroup,
  Grid,
  Row,
  Col,
  PageHeader,
} from 'react-bootstrap';
import { BLACK_LISTED_WORDS } from '../BlackListedWords'
import React, { Component } from 'react';
import { Typeahead } from 'react-bootstrap-typeahead';
import DonationCheckoutComponent from './DonateCheckoutComponent';
import PropTypes from 'prop-types';
import styles from './DonateStyles.scss';

const REQUIRED_MESSAGE = 'This field is required';
const TOO_LONG_MESSAGE = 'Too many characters';

const MIN_AMOUNT = 0;
const MAX_AMOUNT = 1000;
const MAX_BOUNTY_LENGTH = 300;

class DonateComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      bountyType: 'free', // 'free' or 'cash'
      amount: '',
      bounty: '',
      blackListedBountyWords: '',
      game: null,
      showPaymentOptions: false,
      amountError: null,
      bountyError: null,
      bountyWarning: null
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
    if (((this.state.bountyType === 'free') || (this.state.amountError && this.state.amountError.type === null)) &&
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

  getBlackListedBountyWords(bounty) {
    return BLACK_LISTED_WORDS.filter(blackListedWord => bounty.toLowerCase().includes(blackListedWord.toLowerCase()));
  }

  validateAmount() {
    const regex = /^[0-9]\d*(?:\.\d{0,2})?$/;
    let error = { type: null };
    if (this.state.bountyType === 'cash' && this.validateRequiredField(this.state.amount)) {
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
    let warning = { type: null };
    if (this.validateRequiredField(this.state.bounty)) {
      error = { type: 'error', message: REQUIRED_MESSAGE };
    } else if (this.state.bounty.length >= MAX_BOUNTY_LENGTH) {
      error = { type: 'error', message: TOO_LONG_MESSAGE };
    } else if (this.state.blackListedBountyWords.length > 0) {
      warning = { type: 'warning', message: 'Caution. This bounty contains potentially offensive language and will be reviewed for abuse. If abuse is detected, this account will be banned.'}
    }

    this.setState({
      bountyError: error,
      bountyWarning: warning
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
      bounty: newBounty,
      blackListedBountyWords: this.getBlackListedBountyWords(newBounty)
    }, () => {
      this.validateBounty();
    });
  }

  showBountyForm() {
    this.setState({
      showPaymentOptions: false
    });
  }

  getGameOptions() {
    return this.props.topGames.map((game, idx) => ({
      id: idx,
      img: game.box_art_url,
      name: game.name,
    }));
  }

  renderOption(option, props, index) {
    const width = 50;
    const height = 75;
    const url = option.img.replace('{width}', width).replace('{height}', height);

    return (
      <div>
        <img className={styles.gameThumbnail} src={url} alt={option.name} width={width} height={height} />
        {option.name}
      </div>
    );
  }

  setSelectedGame = (selected) => {
    if (selected[0]) {
      this.setState({ game: selected[0].name });
    }
  }

  render() {
    const amountErrorMessage = this.state.bountyType === 'cash' && this.state.amountError !== null ? this.state.amountError.message : '';
    const bountyErrorMessage = this.state.bountyError !== null ? this.state.bountyError.message : '';
    const bountyWarningMessage = this.state.bountyWarning !== null ? this.state.bountyWarning.message : '';
    const offensiveWords = this.state.blackListedBountyWords.length > 0 ? ('Words flagged: ' + this.state.blackListedBountyWords.join(', ')) : '';

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
          game={this.state.game}
          bountyType={this.state.bountyType}
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
                </Row>
                <Row>
                  <FormGroup
                    controlId={'amount'}
                    validationState={this.state.bountyType === 'cash' && this.state.amountError ? this.state.amountError.type : null}
                    >
                    <ControlLabel>Amount</ControlLabel>
                    <InputGroup>
                      <div className={styles.amountSelect}>
                        <FormControl
                          componentClass="select"
                          placeholder="select"
                          value={this.state.bountyType}
                          onChange={e => this.setState({ bountyType: e.target.value })}
                        >
                          <option value="free">Free Bounty</option>
                          {this.props.streamerPaypalEmail !== null && this.props.streamerPaypalEmail !== '' &&
                            <option value="cash">Cash Bounty</option>
                          }
                        </FormControl>
                      </div>
                      {this.state.bountyType === 'cash' &&
                        <InputGroup.Addon className={styles.amountLabel}>$</InputGroup.Addon>
                      }
                      {this.state.bountyType === 'cash' &&
                      <FormControl
                        className={styles.amountBox}
                        type="text"
                        value={this.state.amount}
                        placeholder='5.00'
                        onChange={(e) => this.amountChange(e.target.value)}
                      />
                      }
                    </InputGroup>
                  </FormGroup>
                    <span className={styles.errorMessage}>{amountErrorMessage}</span>
                </Row>
                <Row className={styles.bountyMessageRow}>
                  <FormGroup controlId="formControlsSelect">
                    <ControlLabel>Game</ControlLabel>
                    <Typeahead
                      labelKey="name"
                      onChange={this.setSelectedGame}
                      onInputChange={(text) => this.setState({ game: text })}
                      options={this.getGameOptions()}
                      renderMenuItemChildren={this.renderOption}
                    />
                  </FormGroup>
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
                  <span className={styles.warningMessage}>{bountyWarningMessage}</span>
                  <span>{offensiveWords}</span>
                </Row>
                <Row>
                  <Button type="submit" bsStyle="success" onClick={this.submitForm} disabled={!this.isSubmitEnabled()}>
                    Open Bounty
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

DonateComponent.defaultProps = {
  streamerPaypalEmail: null
};

DonateComponent.propTypes = {
  user: PropTypes.object.isRequired,
  streamerUsername: PropTypes.string.isRequired,
  streamerPaypalEmail: PropTypes.string,
  insertBounty: PropTypes.func.isRequired,
  topGames: PropTypes.array.isRequired
};

export default DonateComponent;
