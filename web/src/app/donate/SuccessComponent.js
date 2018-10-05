import {
  Grid,
  Row,
  PageHeader,
  Glyphicon
} from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { LinkContainer } from 'react-router-bootstrap';
import PropTypes from 'prop-types';
import React, { Component } from 'react';
import styles from './DonateStyles.scss';

class SuccessComponent extends Component {
  render() {
    const link = `/user/${this.props.streamerUserName}`;

    return (
      <div className={styles.donateContainer}>
        <Grid>
          <Row>
            <PageHeader>
              <Glyphicon glyph="ok" className={styles.checkmark} />
              Challenge submitted
            </PageHeader>
          </Row>
          <Row>
            Your challenge has been processed! Thank you for supporting {this.props.streamerUserName}.
            Head over to their <LinkContainer exact to={link}><Link to={link}>profile page</Link></LinkContainer> to check the status of the challenge.
          </Row>
        </Grid>
      </div>
    )
  }
}

SuccessComponent.propTypes = {
  streamerUserName: PropTypes.string.isRequired
};

export default SuccessComponent;
