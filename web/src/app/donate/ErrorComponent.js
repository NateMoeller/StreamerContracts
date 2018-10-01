import {
  Button,
  Grid,
  Row,
  PageHeader,
  Glyphicon
} from 'react-bootstrap';
import React, { Component } from 'react';
import styles from './DonateStyles.scss';

class ErrorComponent extends Component {
  render() {
    return (
      <div className={styles.donateContainer}>
        <Grid>
          <Row>
            <PageHeader>
              <Glyphicon glyph="remove" className={styles.error} />
              Something went wrong.
            </PageHeader>
          </Row>
          <Row>
            Your challenge was not processed. Please try opening the challenge again.
          </Row>
          <Row className={styles.reloadButton}>
            <Button bsStyle="success" onClick={() => window.location.reload()}>Try again</Button>
          </Row>
        </Grid>
      </div>
    );
  }
}

export default ErrorComponent;
