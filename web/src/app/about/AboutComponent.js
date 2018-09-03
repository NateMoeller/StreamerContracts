import React, { Component } from 'react';
import styles from './AboutStyles.scss';

class AboutComponent extends Component {
  render() {
    return (
      <div>
        <h1 className={styles.leftHeader}>eSports style donations</h1>
        <div className={styles.description}>
          Give your stream a competitive edge by enabling competitive donations. As a streamer, StreamerContracts allows viewers to open donation
          bounties for you to complete while you play. If you complete the bounty, then the money is yours. If you fail, the money returns to the viewer.
          StreamerContracts gives an every day stream the competitive tension of a high stakes tournament.
        </div>
        <h1 className={styles.rightHeader}>More Creativity, Interactivity</h1>
        <div className={styles.description}>
          Viewers can open different types of bounties. Competitive bounties can challenge the streamer to complete a difficult in game task,
          and creative bounties can encourage the streamer to do something fun. A competitive bounty would be something like
          "Complete 15 kills in the next match", while a creative bounty would be something like "Win a game with the sound off". Anything goes!
        </div>
      </div>
    );
  }
}

export default AboutComponent;
