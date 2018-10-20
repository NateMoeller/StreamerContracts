import {
  Button,
  FormControl,
  FormGroup,
  Glyphicon
} from 'react-bootstrap';
import React, { Component } from 'react';
import LoadingComponent from '../common/loading/LoadingComponent'
import styles from './SplashStyles.scss';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { emailOperations } from './duck';
import logo from '../../resources/logo_light.png';

const REQUIRED_MESSAGE = 'This field is required';
const INVALID_EMAIL = 'Please enter a valid email';

class SplashComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      name: '',
      email: '',
      nameError: null,
      emailError: null,
    };
  }

  submitEmail = (e) => {
    e.preventDefault();
    const payload = {
      name: this.state.name,
      email: this.state.email
    };

    this.props.addEmail(payload);
  }

  nameChange(name) {
    this.setState({
      name
    }, () => {
      this.validateName();
    });
  }

  emailChange(email) {
    this.setState({
      email
    }, () => {
      this.validateEmail();
    });
  }

  validateRequiredField(value) {
    if (value === '') {
      return true;
    }

    return false;
  }

  validateName() {
    let error = { type: null };
    if (this.validateRequiredField(this.state.name)) {
        error = { type: 'error', message: REQUIRED_MESSAGE };
    }

    this.setState({
      nameError: error
    });
  }

  validateEmail() {
    const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    let error = { type: null };
    if (this.validateRequiredField(this.state.email)) {
      error = { type: 'error', message: REQUIRED_MESSAGE };
    } else if (!emailRegex.test(this.state.email)) {
      error = { type: 'error', message: INVALID_EMAIL };
    }

    this.setState({
      emailError: error
    });
  }

  isSubmitEnabled() {
    if ((this.state.nameError && this.state.nameError.type === null) &&
        (this.state.emailError && this.state.emailError.type === null)) {
          return true;
    }

    return false;
  }

  getForm() {
    const nameErrorMessage = this.state.nameError !== null ? this.state.nameError.message : '';
    const emailErrorMessage = this.state.emailError !== null ? this.state.emailError.message : '';

    if (this.props.email.showSpinner) {
      return <LoadingComponent />;
    } else if (this.props.email.emailSubmitted) {
      return (
        <div className={styles.successContainer}>
          <Glyphicon glyph="ok" className={styles.checkmark} />
          Submitted
        </div>
      );
    } else {
      return (
        <div>
          <h3 className={styles.title}>Join the email list</h3>
          <div className={styles.fields}>
            <form>
              <div className={styles.field}>
                <FormGroup validationState={this.state.nameError ? this.state.nameError.type : null}>
                  <FormControl
                    type="text"
                    placeholder="Name"
                    value={this.state.name}
                    onChange={(e) => this.nameChange(e.target.value)}
                  />
                </FormGroup>
                <span className={styles.errorMessage}>{nameErrorMessage}</span>
              </div>
              <div className={styles.field}>
                <FormGroup validationState={this.state.emailError ? this.state.emailError.type : null}>
                  <FormControl type="text" placeholder="Email" value={this.state.email} onChange={(e) => this.emailChange(e.target.value)} />
                </FormGroup>
                <span className={styles.errorMessage}>{emailErrorMessage}</span>
              </div>
              <Button className={styles.button} type="submit" onClick={this.submitEmail} disabled={!this.isSubmitEnabled()}>Notify me</Button>
            </form>
          </div>
        </div>
      );
    }
  }

  render() {
    const form = this.getForm();

    return (
      <div className={styles.background}>
        <div className={styles.overlay} />
        <div className={styles.header}>
          <img src={logo} alt="Bounty Streamer" className={styles.image} />
        </div>
        <div className={styles.content}>
          <div className={styles.mainContent}>
            <div className={styles.mainHeader}>
              Coming Soon!
            </div>
            <div className={styles.mainSubText}>
              Donate to your favorite streamers by challenging them to complete in game bounties.
            </div>
          </div>
          <div className={styles.formContent}>
            <div className={styles.form}>
              {form}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...emailOperations }, dispatch);
}

function mapStateToProps(state) {
  return {
    email: state.email
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(SplashComponent);
