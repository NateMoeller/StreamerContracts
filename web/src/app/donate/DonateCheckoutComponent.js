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
  insertFreeBounty = () => {
    const payload = {
      username: this.props.username,
      amount: 0,
      bounty: this.props.bounty,
      payPalPaymentId: '',
      streamerUsername: this.props.streamerUsername,
      game: this.props.game
    };

    this.props.insertBounty(payload);
  }


  render() {
    return (
      <div className={styles.donateContainer}>
        <Grid>
          <Row>
            <PageHeader>Review your challenge</PageHeader>
          </Row>
          <Row className={styles.backButton}>
            <Col xs={3} md={2}>
              <Button onClick={this.props.goBack}>
                <Glyphicon glyph="arrow-left" className={styles.arrowLeft} />
                Back
              </Button>
            </Col>
          </Row>
          <Row className={styles.usernameCheckout}>
            <Col xs={3} md={2}>
              Username:
            </Col>
            <Col xs={8} md={6}>
              {this.props.username}
            </Col>
          </Row>
          {this.props.game &&
            <Row className={styles.usernameCheckout}>
              <Col xs={3} md={2}>
                Game:
              </Col>
              <Col xs={8} md={6}>
                {this.props.game}
              </Col>
            </Row>
          }
          <Row className={styles.bountyCheckout}>
            <Col xs={3} md={2}>
              Bounty:
            </Col>
            <Col xs={8} md={6}>
              {this.props.bounty}
            </Col>
          </Row>
          {this.props.bountyType === 'cash' &&
            <Row className={styles.totalAmountCheckout}>
              <Col xs={3} md={2}>
                Total amount:
              </Col>
              <Col xs={3} md={2} className={styles.amount}>
                ${parseFloat(this.props.amount).toFixed(2)}
              </Col>
            </Row>
          }
          <Row>
            <Col xs={3} md={2}></Col>
            <Col xs={3} md={2}>
              {this.props.bountyType === 'cash' &&
                <PayWithPayPalComponent
                  amount={this.props.amount}
                  streamerPaypalEmail={this.props.streamerPaypalEmail}
                  bounty={this.props.bounty}
                  username={this.props.username}
                  insertBounty={this.props.insertBounty}
                  streamerUsername={this.props.streamerUsername}
                  gameName={this.props.game}
                />
              }
            </Col>
          </Row>
          {this.props.bountyType === 'free' &&
            <Button className={styles.openBountyBtn} type="submit" bsStyle="success" onClick={this.insertFreeBounty}>
              Open Bounty
            </Button>
          }
        </Grid>
      </div>
    );
  }
}

DonateCheckoutComponent.defaultProps = {
  game: null
};

DonateCheckoutComponent.propTypes = {
  goBack: PropTypes.func.isRequired,
  amount: PropTypes.string.isRequired,
  streamerPaypalEmail: PropTypes.string.isRequired,
  bounty: PropTypes.string.isRequired,
  username: PropTypes.string.isRequired,
  insertBounty: PropTypes.func.isRequired,
  streamerUsername: PropTypes.string.isRequired,
  game: PropTypes.string,
  bountyType: PropTypes.string,
};

export default DonateCheckoutComponent;
