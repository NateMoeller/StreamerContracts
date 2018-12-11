import React, { Component } from 'react';
import PropTypes from 'prop-types';
import styles from './StatBoxStyles.scss';

class StatBox extends Component {
  render() {
    return (
      <div className={styles.box}>
        <div className={styles.mainNumber}>
          {this.props.number}
        </div>
        <div className={styles.label}>
          {this.props.label}
        </div>
      </div>
    );
  }
}

StatBox.propTypes = {
  number: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  label: PropTypes.string.isRequired,
}

export default StatBox;
