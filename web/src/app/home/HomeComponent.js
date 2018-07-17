import { Carousel } from 'react-bootstrap';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import styles from './HomeStyles.scss';

class HomeComponent extends Component {
  render() {
    return (
      <div className={styles.carousel}>
        <Carousel controls={true}>
          <Carousel.Item>
            <div className={styles.carouselItem}>
              <div className={styles.content1}>
                <h1 className={styles.header}>Donations Done <span className={styles.highlight}>Differently</span>.</h1>
                <p className={styles.caption}>Donate to your favorite streamers by challenging them to in game achievements</p>
              </div>
            </div>
          </Carousel.Item>
          <Carousel.Item>
            <div className={styles.carouselItem}>
              CONTENT 2
            </div>
          </Carousel.Item>
        </Carousel>
      </div>
    );
  }
}

HomeComponent.propTypes = {
  fetchApi: PropTypes.func.isRequired,
  showSpinner: PropTypes.bool.isRequired
};


export default HomeComponent;
