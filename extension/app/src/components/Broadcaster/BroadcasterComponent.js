import './BroadcasterStyles.css';
import React, { Component } from 'react';
import { Tabs, Tab } from 'react-bootstrap';
import logo from '../../resources/logo_dark.png';

class BroadCasterComponent extends Component {
  render() {
    return (
      <div>
        <div className="header">
          <img src={logo} alt="Bounty Streamer" width={200} height={50} />
        </div>
        <Tabs defaultActiveKey={1} id="tabs">
          <Tab eventKey={1} title="Open">
            Tab 1 content
          </Tab>
          <Tab eventKey={2} title="Active">
            Tab 2 content
          </Tab>
          <Tab eventKey={3} title="Completed">
            Tab 3 content
          </Tab>
        </Tabs>
      </div>
    );
  }
}

export default BroadCasterComponent;
