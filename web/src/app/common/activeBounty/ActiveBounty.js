import React, { Component } from 'react';
import { Button } from 'react-bootstrap';
import Countdown from 'react-countdown-now';
import cx from 'classnames';
import PropTypes from 'prop-types';
import styles from './ActiveBounty.scss';

class ActiveBounty extends Component {
  constructor(props) {
    super(props);

    this.activeBountyDiv = React.createRef();
  }

  voteActiveBounty(contractId, completed) {
    console.log('vote active bounty');

    const votePayload = {
      contractId,
      flagCompleted: completed
    }

    // this.props.voteBounty(votePayload);
  }

  render() {
    const { activeBounty } = this.props;

    if (!activeBounty) {
      if (this.activeBountyDiv.current) {
        return <div className={cx(styles.activeBounty, styles.slitOut)}></div>;
      }

      return null;
    }

    const dateObj = new Date(activeBounty.settlesAt);
    const renderer = ({ hours, minutes, seconds, completed }) => {
      if (completed) {
        return 'Expired';
      } else {
        return <span>{hours} hrs, {minutes} min, {seconds} s</span>;
      }
    };

    return (
      <div className={cx(styles.activeBounty, styles.slitIn)} ref={this.activeBountyDiv}>
        <div className={styles.left}>
          <div className={styles.moneyBox}>
            ${activeBounty.contractAmount}
          </div>
          <div className={styles.description}>
            {activeBounty.description}
          </div>
        </div>
        <div className={styles.right}>
          <div className={styles.timer}>
            <Countdown date={dateObj} renderer={renderer} />
          </div>
          {this.props.voteBounty &&
            <div className={styles.buttons}>
              <Button
                bsStyle="link"
                // disabled={!this.state.voteButtonsEnabled}
                onClick={() => {
                  this.voteActiveBounty(activeBounty.contractId, false);
                }}
              >
                Mark failed
              </Button>
              <Button
                className={styles.submit}
                // disabled={!this.state.voteButtonsEnabled}
                onClick={() => {
                  this.voteActiveBounty(activeBounty.contractId, true);
                }}
              >
                  Mark completed
              </Button>
            </div>
          }
        </div>
      </div>
    );
  }
}

ActiveBounty.defaultProps = {
  activeBounty: null
};

ActiveBounty.propTypes = {
  activeBounty: PropTypes.object
};

export default ActiveBounty;
