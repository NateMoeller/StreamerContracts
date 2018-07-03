import './ProfileStyles.css';
import { Col, Grid, Image, Nav, NavItem, Row } from 'react-bootstrap';
import React, { Component } from 'react';
import PropTypes from 'prop-types';

class ProfileComponent extends Component {
  render() {
    return (
      <Grid>
        <Row>
          <Col xs={6} md={4}>
            <Image src={this.props.imageUrl} thumbnail />
            <Row>
              <h3>{this.props.twitchUserName}</h3>
            </Row>
            <Row>
              <Nav bsStyle="pills" stacked activeKey={1}>
                <NavItem eventKey={1}>
                  Account
                </NavItem>
                <NavItem eventKey={2} title="Item">
                  Open Contracts
                </NavItem>
                <NavItem eventKey={3}>
                  Complete Contracts
                </NavItem>
                <NavItem eventKey={4}>
                  Alerts
                </NavItem>
              </Nav>
            </Row>
          </Col>
          <Col xs={12} md={8}>
            MAIN CONTENT
          </Col>
        </Row>
      </Grid>
    );
  }
}

ProfileComponent.propTypes = {
  twitchUserName: PropTypes.string.isRequired,
  imageUrl: PropTypes.string.isRequired
};

export default ProfileComponent;
