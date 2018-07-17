import { Col, Grid, Image, Nav, NavItem, Row } from 'react-bootstrap';
import React, { Component } from 'react';
import PropTypes from 'prop-types';

class ProfileComponent extends Component {
  getContent() {
    if (this.props.activeTab === 'account') {
      return this.getAccountContent();
    }

    return 'OTHER CONTENT';
  }

  getAccountContent() {
    return (
      <Row>
        <Row>
          <Col sm={8} md={6}>
            <h3 className="number">$0.00</h3>
            Total contract revenue
          </Col>
          <Col sm={8} md={6}>
            <h3 className="number">0</h3>
            Open contracts
          </Col>
        </Row>
        <Row>
          <Col sm={8} md={6}>
            <h3 className="number">0</h3>
            Completed contracts
          </Col>
          <Col sm={8} md={6}>
            <h3 className="number">1</h3>
            Contract in progress
          </Col>
        </Row>
      </Row>
    );
  }

  render() {
    const content = this.getContent();

    return (
      <Grid className="content">
        <Col xs={3} md={2} className="sidebar">
          <Row>
            <Image src={this.props.imageUrl} thumbnail />
            <h2 className="name">{this.props.twitchUserName}</h2>
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
                Completed Contracts
              </NavItem>
              <NavItem eventKey={4}>
                Alerts
              </NavItem>
            </Nav>
          </Row>
        </Col>
        <Col xs={13} md={9}>
          {content}
        </Col>
      </Grid>
    );
  }
}

ProfileComponent.propTypes = {
  twitchUserName: PropTypes.string.isRequired,
  imageUrl: PropTypes.string.isRequired,
  activeTab: PropTypes.string.isRequired
};

export default ProfileComponent;
