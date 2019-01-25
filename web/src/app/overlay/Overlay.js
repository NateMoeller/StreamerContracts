import React, { Component } from 'react';
import cx from 'classnames';
import PropTypes from 'prop-types';
import logo from '../../resources/logo_light.png';
import styles from './OverlayStyles.scss';

class Overlay extends Component {
  constructor(props) {
    super(props);

    this.activeBountyDiv = React.createRef();
  }

  render() {
    const { activeBounty } = this.props;
    if (!activeBounty) {
      if (this.activeBountyDiv.current) {
        return <div className={cx(styles.activeBounty, styles.slitOut)}></div>;
      }

      return null;
    }

    // TODO: timer countdown?
    return (
      <div className={cx(styles.activeBounty, styles.slitIn)} ref={this.activeBountyDiv}>
        <div className={styles.left}>
          <div className={styles.moneyBox}>
            ${activeBounty.bountyAmount.toFixed(2)}
          </div>
          <div className={styles.description}>
            {activeBounty.bountyDescription}
          </div>
          <div className={styles.link}>
            <img src={logo} alt="Bounty Streamer" width="200" height="50" />
          </div>
        </div>
      </div>
    );
  }
}

Overlay.defaultProps = {
  activeBounty: null
};

Overlay.propTypes = {
  activeBounty: PropTypes.object
};

export default Overlay;
