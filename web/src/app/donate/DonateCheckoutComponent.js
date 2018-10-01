import {
  Button,
  Grid,
  Row,
  Col,
  PageHeader,
  Glyphicon
} from 'react-bootstrap';
import PropTypes from 'prop-types';
import React, { Component } from 'react';
import PayWithPayPalComponent from './PayWithPayPalComponent';
import styles from './DonateStyles.scss';

class DonateCheckoutComponent extends Component {
  render() {
    return (
      <div className={styles.donationContainer}>
        <Grid>
          <Row>
            <PageHeader>Review your challenge</PageHeader>
          </Row>
          <Row style={{ marginBottom: '20px' }}>
            <Col xs={3} md={2}>
              <Button onClick={this.props.goBack}>
                <Glyphicon glyph="arrow-left" className={styles.arrowLeft} />
                Back
              </Button>
            </Col>
          </Row>
          <Row style={{ marginBottom: '10px' }}>
            <Col xs={3} md={2}>
              Username:
            </Col>
            <Col xs={8} md={6}>
              {this.props.username}
            </Col>
          </Row>
          <Row style={{ height: '50px', borderBottom: '1px solid #eee' }}>
            <Col xs={3} md={2}>
              Bounty:
            </Col>
            <Col xs={8} md={6}>
              {this.props.bounty}
            </Col>
          </Row>
          <Row style={{ marginBottom: '10px', marginTop: '10px' }}>
            <Col xs={3} md={2}>
              Total amount:
            </Col>
            <Col xs={3} md={2} style={{ color: 'red' }}>
              ${this.props.amount}
            </Col>
          </Row>
          <Row>
            <Col xs={3} md={2}></Col>
            <Col xs={3} md={2}>
              <PayWithPayPalComponent
                  amount={this.props.amount}
                  streamerPaypalEmail={this.props.streamerPaypalEmail}
                  bounty={this.props.bounty}
                  username={this.props.username}
                  insertBounty={this.props.insertBounty}
                  streamerUsername={this.props.streamerUsername}
              />
            </Col>
          </Row>
        </Grid>
      </div>
    );
  }
}

DonateCheckoutComponent.propTypes = {
  goBack: PropTypes.func.isRequired,
  amount: PropTypes.string.isRequired,
  streamerPaypalEmail: PropTypes.string.isRequired,
  bounty: PropTypes.string.isRequired,
  username: PropTypes.string.isRequired,
  insertBounty: PropTypes.func.isRequired,
  streamerUsername: PropTypes.string.isRequired
};

export default DonateCheckoutComponent;
