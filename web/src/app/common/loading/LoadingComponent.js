import React, { Component } from 'react';
import ReactLoading from 'react-loading';
import styles from './LoadingStyles.scss';

class LoadingComponent extends Component {
  render() {
    return (
      <div className={styles.loadingContainer}>
        <ReactLoading type={'bubbles'} color={'#13293D'} height={200} width={200} />
        <div className={styles.loadingText}>
          Loading...
        </div>
      </div>
    );
  }
}

export default LoadingComponent;
