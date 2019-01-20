import React, { Component } from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';
import styles from './StatBoxStyles.scss';

class StatBox extends Component {
  render() {
    const boxStyle = cx(styles.box, { [styles.link]: this.props.onClick !== null});

    return (
      <div className={boxStyle} onClick={this.props.onClick}>
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

StatBox.defaultProps = {
  onClick: null
}

StatBox.propTypes = {
  number: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
  label: PropTypes.string.isRequired,
  onClick: PropTypes.func
}

export default StatBox;
