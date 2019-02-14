import React, { Component } from 'react';
import { PageHeader, Collapse } from 'react-bootstrap';
import styles from './AboutStyles.scss';

class AboutComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      open: null
    }

    this.questions = [
      {
        question: 'What is BountyStreamer?',
        answer: 'BountyStreamer is an platform where viewers can open challenges (dubbed bounties) to streamers to complete live on stream.' },
      {
        question: 'How do update my paypal email?',
        answer: 'Click on your name in the upper right of the page. Select "Settings" from the dropdown list. Click "Edit" and type your paypal email in the textbox' }
    ];
  }

  toggleQuestion(index) {
    if (this.state.open === index) {
      this.setState({
        open: null
      });
    } else {
      this.setState({
        open: index
      });
    }
  }

  getQuestions() {
    return this.questions.map((question, index) =>
      <div className={styles.questionBox} key={index}>
        <div className={styles.question} onClick={() => this.toggleQuestion(index)}>
          {question.question}
        </div>
        <Collapse in={this.state.open === index}>
          <div id="example-collapse-text">
            <div className={styles.answer}>
              {question.answer}
            </div>
          </div>
        </Collapse>
      </div>
    );
  }

  render() {
    return (
      <div>
        <PageHeader>About</PageHeader>
        <div className={styles.aboutContent}>
          {this.getQuestions()}
        </div>
      </div>
    );
  }
}

export default AboutComponent;
