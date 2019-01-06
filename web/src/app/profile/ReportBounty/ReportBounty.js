import React, { Component } from 'react';
import {
  FormGroup,
  FormControl,
  ControlLabel,
  Button
} from 'react-bootstrap';
import PropTypes from 'prop-types';
import cx from 'classnames';
import { emailRegex } from '../../../commonRegex';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { profileOperations } from '../duck';
import styles from './ReportBounty.scss';

const REQUIRED_MESSAGE = 'This field is required';
const INVALID_EMAIL = 'Please enter a valid email';

class ReportBounty extends Component {
  constructor(props) {
    super(props);

    this.state = {
      email: '',
      reportText: '',
      emailError: null,
      reportError: null
    }

    this.reportBountyDiv = React.createRef();
  }

  validateEmail() {
    let error = { type: null };
    if (this.state.email === '') {
      error = { type: 'error', message: REQUIRED_MESSAGE };
    } else if (!emailRegex.test(this.state.email)) {
      error = { type: 'error', message: INVALID_EMAIL };
    }

    this.setState({
      emailError: error
    });
  }

  validateReportText() {
    let error = { type: null };
    if (this.state.reportText === '') {
        error = { type: 'error', message: REQUIRED_MESSAGE };
    }

    this.setState({
      reportError: error
    });
  }

  emailChange = (email) => {
    this.setState({
      email
    }, () => {
      this.validateEmail();
    });
  }

  reportChange = (reportText) => {
    this.setState({
      reportText
    }, () => {
      this.validateReportText();
    });
  }

  isSubmitEnabled() {
    if ((this.state.reportError && this.state.reportError.type === null) &&
        (this.state.emailError && this.state.emailError.type === null)) {
          return true;
    }

    return false;
  }

  submit = () => {
    this.props.reportBounty(this.state.email, this.state.reportText, this.props.contractId);
    this.props.close();
    this.setState({
      email: '',
      reportText: ''
    });
  }

  render() {
    if (this.props.contractId === null) {
      if (this.reportBountyDiv.current) {
        return <div className={cx(styles.reportBounty, styles.slitOut)}></div>;
      }
      return null;
    }

    const emailErrorMessage = this.state.emailError !== null ? this.state.emailError.message : '';
    const reportErrorMessage = this.state.reportError !== null ? this.state.reportError.message : '';

    return (
      <div className={cx(styles.reportBounty, styles.slitIn)} ref={this.reportBountyDiv}>
        <FormGroup validationState={this.state.emailError ? this.state.emailError.type : null}>
          <ControlLabel>Report Bounty</ControlLabel>
          <div className={styles.row}>
            <div className={styles.leftCell}>
              <FormControl className={styles.emailField} type="text" placeholder="Email" value={this.state.email} onChange={(e) => this.emailChange(e.target.value)} />
            </div>
            <div className={styles.rightCell}>
              {emailErrorMessage}
            </div>
          </div>
        </FormGroup>
        <FormGroup validationState={this.state.reportError ? this.state.reportError.type : null}>
          <div className={styles.row}>
            <div className={styles.leftCell}>
              <FormControl componentClass="textarea" placeholder="What's wrong?" value={this.state.reportText} onChange={(e) => this.reportChange(e.target.value)} />
            </div>
            <div className={styles.rightCell}>
              {reportErrorMessage}
            </div>
          </div>
          <Button className={styles.submitButton} bsStyle="primary" type="submit" onClick={this.submit} disabled={!this.isSubmitEnabled()}>Submit</Button>
        </FormGroup>
      </div>
    );
  }
}

ReportBounty.defaultProps = {
  contractId: null
}

ReportBounty.propTypes = {
  close: PropTypes.func.isRequired,
  contractId: PropTypes.string
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...profileOperations }, dispatch);
}

export default connect(null, mapDispatchToProps)(ReportBounty);
