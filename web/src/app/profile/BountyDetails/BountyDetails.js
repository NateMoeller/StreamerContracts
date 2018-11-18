import React, { Component } from 'react';
import {
  Button,
  Glyphicon
} from 'react-bootstrap';
import PropTypes from 'prop-types';
import styles from './BountyDetails.scss';

class BountyDetails extends Component {
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

  getOpenButtons() {
    return (
      <div className={styles.rightButtons}>
        <Button bsStyle="link" className={styles.remove}>Remove bounty</Button>
        <Button className={styles.submit}>Accept bounty</Button>
      </div>
    );
  }

  getAcceptButtons() {
    return (
      <div className={styles.rightButtons}>
        <Button bsStyle="link" className={styles.remove}>Fail bounty</Button>
        <Button className={styles.submit}>Complete bounty</Button>
      </div>
    );
  }

  getAction() {
    const { curBounty } = this.props;
    if (curBounty.isCompleted) {
      return this.getCompletedIcon();
    } else if (curBounty.isExpired) {
      return this.getFailedIcon();
    } else if (curBounty.isAccepted) {
      return this.getAcceptButtons();
    }

    return this.getOpenButtons();
  }

  render() {
    return (
      <div className={styles.details}>
        <div className={styles.buttons}>
          <Button onClick={() => this.props.setCurBounty(null)}>
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
              <div className={styles.statCell}>{this.props.curBounty.expiresAt}</div>
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

BountyDetails.propTypes = {
  curBounty: PropTypes.object.isRequired,
  setCurBounty: PropTypes.func.isRequired,
  acceptBounty: PropTypes.func.isRequired,
  removeBounty: PropTypes.func.isRequired,
}

export default BountyDetails;
