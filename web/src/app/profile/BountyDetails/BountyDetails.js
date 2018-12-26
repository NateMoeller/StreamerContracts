import React, { Component } from 'react';
import {
  Button,
  Glyphicon
} from 'react-bootstrap';
import PropTypes from 'prop-types';
import { OPEN, ACCEPTED, DECLINED, EXPIRED, COMPLETED, FAILED } from '../../BountyState';
import styles from './BountyDetails.scss';

class BountyDetails extends Component {
  goBack = () => {
    this.props.setCurBounty(null)
  }

  getCompletedIcon() {
    return (
      <div className={styles.rightButtons}>
        <div className={styles.checkmark}><Glyphicon glyph="ok" /></div>
        <div className={styles.text}>Completed</div>
      </div>
    );
  }

  getFailedIcon() {
    return (
      <div className={styles.rightButtons}>
        <div className={styles.failed}><Glyphicon glyph="remove" /></div>
        <div className={styles.text}>Failed</div>
      </div>
    );
  }

  getDeclinedIcon() {
    return (
      <div className={styles.rightButtons}>
        <div className={styles.failed}><Glyphicon glyph="remove" /></div>
        <div className={styles.text}>Declined</div>
      </div>
    );
  }

  getOpenButtons() {
    return (
      <div className={styles.rightButtons}>
        <Button bsStyle="link" className={styles.remove} onClick={() => this.props.onDeclineBounty(this.props.curBounty.contractId)}>Decline bounty</Button>
        <Button className={styles.submit} onClick={() => this.props.onAcceptBounty(this.props.curBounty.contractId)}>Accept bounty</Button>
      </div>
    );
  }

  getAcceptButtons() {
    const fail = this.props.isStreamer ? 'Mark failed' : `${this.props.curBounty.streamerName} failed bounty`;
    const success = this.props.isStreamer ? 'Mark success' : `${this.props.curBounty.streamerName} completed bounty`;

    return (
      <div className={styles.rightButtons}>
        <Button
          bsStyle="link"
          className={styles.remove}
          onClick={() => {
            const voteFailedPayload = {
              contractId: this.props.curBounty.contractId,
              flagCompleted: false
            };
            this.props.onVoteBounty(voteFailedPayload);
          }}
        >
          {fail}
        </Button>
        <Button
          className={styles.submit}
          onClick={() => {
            const voteCompletedPayload = {
              contractId: this.props.curBounty.contractId,
              flagCompleted: true
            };
            this.props.onVoteBounty(voteCompletedPayload);
          }}
        >
          {success}
        </Button>
      </div>
    );
  }

  getAction() {
    const { curBounty } = this.props;
    if (curBounty.state === COMPLETED) {
      return this.getCompletedIcon();
    } else if (curBounty.state === DECLINED) {
      return this.getDeclinedIcon();
    } else if (curBounty.state === EXPIRED || curBounty.state === FAILED) {
      return this.getFailedIcon();
    } else if (curBounty.state === ACCEPTED && (this.props.isStreamer || this.props.isDonor)) {
      return this.getAcceptButtons();
    } else if (curBounty.state === OPEN && this.props.isStreamer) {
      return this.getOpenButtons();
    }

    return '';
  }

  render() {
    return (
      <div className={styles.details}>
        <div className={styles.buttons}>
          <Button onClick={this.goBack}>
            <Glyphicon glyph="arrow-left" className={styles.arrowLeft} />
            Back
          </Button>
          {this.getAction()}
        </div>
        <div className={styles.content}>
          <div className={styles.description}>
            {this.props.curBounty.description}
          </div>
          <div className={styles.stats}>
            <div className={styles.statRow}>
              <div className={styles.statHeader}>Submitted by:</div>
              <div className={styles.statCell}>{this.props.curBounty.bountyOwnerName}</div>
            </div>
            <div className={styles.statRow}>
              <div className={styles.statHeader}>Game:</div>
              <div className={styles.statCell}>{this.props.curBounty.game}</div>
            </div>
            <div className={styles.statRow}>
              <div className={styles.statHeader}>Expires at:</div>
              <div className={styles.statCell}>{new Date(this.props.curBounty.settlesAt).toLocaleString()}</div>
            </div>
            <div className={styles.statRow}>
              <div className={styles.statHeader}>Amount:</div>
              <div className={styles.statCell}>{`$${this.props.curBounty.contractAmount.toFixed(2)}`}</div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

BountyDetails.defaultProps = {
  onAcceptBounty: null,
  onDeclineBounty: null,
  onVoteBounty: null,
  isStreamer: false,
  isDonor: false
};

BountyDetails.propTypes = {
  curBounty: PropTypes.object.isRequired,
  setCurBounty: PropTypes.func.isRequired,
  onAcceptBounty: PropTypes.func,
  onDeclineBounty: PropTypes.func,
  onVoteBounty: PropTypes.func,
  isStreamer: PropTypes.bool,
  isDonor: PropTypes.bool
}

export default BountyDetails;
