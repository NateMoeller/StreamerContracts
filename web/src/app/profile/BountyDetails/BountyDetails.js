import React, { Component } from 'react';
import {
  Button,
  Glyphicon,
  Tooltip,
  OverlayTrigger
} from 'react-bootstrap';
import PropTypes from 'prop-types';
import cx from 'classnames';
import { OPEN, ACTIVE, DECLINED, EXPIRED, COMPLETED, FAILED } from '../../BountyState';
import LoadingComponent from '../../common/loading/LoadingComponent';
import RestClient from '../../RestClient';
import styles from './BountyDetails.scss';

class BountyDetails extends Component {
  constructor(props) {
    super(props);

    this.state = {
      loading: false,
      image: null
    };
  }

  componentDidMount() {
    if (!this.props.isExtension) {
      const gameName = this.props.curBounty.game;
      this.setState({ loading: true });
      RestClient.GET(`twitch/game/${gameName}`, (response) => {
        this.setState({
          loading: false,
          image: response.data.boxArtUrl
        });
      });
    }
  }

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

  getFailedIcon(text) {
    return (
      <div className={styles.rightButtons}>
        <div className={styles.failed}><Glyphicon glyph="remove" /></div>
        <div className={styles.text}>{text}</div>
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
        <Button bsStyle="link" className={cx(styles.remove, { [styles.extensionText]: this.props.isExtension})} onClick={() => this.props.onDeclineBounty(this.props.curBounty.contractId)}>Decline bounty</Button>
        <Button className={cx(styles.submit, { [styles.extensionText]: this.props.isExtension})} onClick={() => this.props.onActivateBounty(this.props.curBounty.contractId)}>Activate bounty</Button>
      </div>
    );
  }

  getAcceptButtons() {
    const fail = this.props.isStreamer ? 'Mark failed' : `${this.props.curBounty.streamerName} failed bounty`;
    const success = this.props.isStreamer ? 'Mark completed' : `${this.props.curBounty.streamerName} completed bounty`;

    return (
      <div className={styles.rightButtons}>
        <Button
          bsStyle="link"
          className={styles.remove}
          onClick={() => {
            const voteFailedPayload = {
              contract: this.props.curBounty,
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
              contract: this.props.curBounty,
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
      const text = curBounty.state === EXPIRED ? 'Expired' : 'Failed';
      return this.getFailedIcon(text);
    } else if (curBounty.state === ACTIVE && (this.props.isStreamer || this.props.isDonor)) {
      return this.getAcceptButtons();
    } else if (curBounty.state === OPEN) {
      if (this.props.isStreamer) {
        return curBounty.userVote === COMPLETED ? this.getMarkedCompleted() : this.getOpenButtons();
      } else if (this.props.isDonor) {
        return curBounty.userVote === FAILED ? this.getMarkedFailed() : this.getAcceptButtons();
      }
    }

    return '';
  }

  getMarkedCompleted() {
    const tooltip = (
      <Tooltip id="tooltip">
        You have marked this bounty complete. This bounty will be completed when voting has finished
      </Tooltip>
    );

    return (
      <OverlayTrigger placement="bottom" overlay={tooltip}>
        <div className={cx(styles.rightButtons, styles.voted)}>

            <div className={styles.checkmark}><Glyphicon glyph="ok" /></div>
            <div className={styles.text}>Marked Completed</div>
        </div>
      </OverlayTrigger>
    );
  }

  getMarkedFailed() {
    const tooltip = (
      <Tooltip id="tooltip">
        You have marked this bounty failed. The transaction will be canceled if the streamer agrees the bounty was failed.
      </Tooltip>
    );

    return (
      <OverlayTrigger placement="bottom" overlay={tooltip}>
        <div className={cx(styles.rightButtons, styles.voted)}>
          <div className={styles.failed}><Glyphicon glyph="remove" /></div>
          <div className={styles.text}>Marked failed</div>
        </div>
      </OverlayTrigger>
    );
  }

  render() {
    if (this.state.loading) {
      return <LoadingComponent />;
    }

    const width = 100;
    const height = 150;
    const url = this.state.image ? this.state.image.replace('{width}', width).replace('{height}', height) : null;

    return (
      <div className={styles.details}>
        <div className={styles.buttons}>
          <Button onClick={this.goBack}>
            <Glyphicon glyph="arrow-left" className={styles.arrowLeft} />
            {!this.props.isExtension ? 'Back' : null}
          </Button>
          {this.getAction()}
        </div>
        <div className={styles.content}>
          {this.state.image &&
            <div className={styles.image}>
              <img src={url} alt={this.state.image.name} width={width} height={height} />
            </div>
          }
          <div className={styles.description}>
            <div className={styles.title}>Bounty Description</div>
            {this.props.curBounty.description}
          </div>
          <div className={styles.stats}>
            <div className={styles.title}>Statistics</div>
            <div className={styles.statRow}>
              <div className={styles.statHeader}>Amount:</div>
              <div className={cx(styles.statCell, styles.money)}>{`$${this.props.curBounty.contractAmount.toFixed(2)}`}</div>
            </div>
            <div className={styles.statRow}>
              <div className={styles.statHeader}>Submitted by:</div>
              <div className={styles.statCell}>{this.props.curBounty.proposerName}</div>
            </div>
            {this.props.curBounty.game &&
              <div className={styles.statRow}>
                <div className={styles.statHeader}>Game:</div>
                <div className={styles.statCell}>{this.props.curBounty.game}</div>
              </div>
            }
            <div className={styles.statRow}>
              <div className={styles.statHeader}>Expires at:</div>
              <div className={styles.statCell}>{new Date(this.props.curBounty.settlesAt).toLocaleString()}</div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

BountyDetails.defaultProps = {
  onActivateBounty: null,
  onDeclineBounty: null,
  onVoteBounty: null,
  isStreamer: false,
  isDonor: false,
  isExtension: false
};

BountyDetails.propTypes = {
  curBounty: PropTypes.object.isRequired,
  setCurBounty: PropTypes.func.isRequired,
  onActivateBounty: PropTypes.func,
  onDeclineBounty: PropTypes.func,
  onVoteBounty: PropTypes.func,
  isStreamer: PropTypes.bool,
  isDonor: PropTypes.bool,
  isExtension: PropTypes.bool
}

export default BountyDetails;
