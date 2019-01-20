import React, { Component } from 'react';
import { Button, Glyphicon } from 'react-bootstrap';
import Countdown from 'react-countdown-now';
import cx from 'classnames';
import PropTypes from 'prop-types';
import { COMPLETED } from '../../BountyState';
import styles from './ActiveBounty.scss';

class ActiveBounty extends Component {
  constructor(props) {
    super(props);

    this.activeBountyDiv = React.createRef();
  }

  voteActiveBounty(contract, completed) {
    const votePayload = {
      contract,
      flagCompleted: completed
    }

    this.props.voteBounty(votePayload);
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

    const twitchLink = `twitch.tv/${activeBounty.streamerName}`;

    return (
      <div className={cx(styles.activeBounty, styles.slitIn)} ref={this.activeBountyDiv}>
        <div className={styles.left}>
          <div className={styles.moneyBox}>
            ${activeBounty.contractAmount.toFixed(2)}
          </div>
          <div className={styles.description}>
            {activeBounty.description}
          </div>
          <div className={styles.link}>
            <i className="fa fa-twitch" /> <a href={`https://www.${twitchLink}`} target="_blank" rel='noreferrer noopener'>{twitchLink}</a>
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
                onClick={() => {
                  this.voteActiveBounty(activeBounty, false);
                }}
              >
                Mark failed
              </Button>
              <Button
                className={styles.submit}
                onClick={() => {
                  this.voteActiveBounty(activeBounty, true);
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
