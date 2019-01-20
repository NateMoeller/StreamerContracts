import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import { Button, Col, Grid, Image, Row, Tabs, Tab } from 'react-bootstrap';
import cx from 'classnames';
import React, { Component } from 'react';
import BootstrapTable from 'react-bootstrap-table-next';
import BountyDetails from '../profile/BountyDetails/BountyDetails';
import paginationFactory from 'react-bootstrap-table2-paginator';
import PropTypes from 'prop-types';
import PublicActiveBountyContainer from '../common/activeBounty/PublicActiveBountyContainer';
import { OPEN, COMPLETED, FAILED } from '../BountyState';
import profileStyles from '../profile/ProfileStyles.scss';
import styles from './UserComponentStyles.scss';
import tableStyles from '../tableStyles.scss';

const pageSize = 10;
const tabArray = [OPEN, COMPLETED, FAILED]; // should be in the order of the tabs

class UserComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      tabKey: 1,
      curPage: 1,
      bountyFilter: OPEN,
      curBounty: null
    };
  }

  componentDidMount() {
    this.refreshList();
  }

  tableChange = (type, newState) => {
    if (type === 'pagination') {
      this.setState({
        curPage: newState.page
      }, () => {
        this.props.getPublicBounties(newState.page - 1, newState.sizePerPage, this.props.publicUser.displayName, this.state.bountyFilter);
      });
    }
  }

  refreshList() {
    this.props.getPublicBounties(0, pageSize, this.props.publicUser.displayName, this.state.bountyFilter);
  }

  handleBountyFilterChange = (key) => {
    this.setState({
      curBounty: null,
      tabKey: key,
      bountyFilter: tabArray[key - 1]
    }, () => {
      this.refreshList();
    });
  }

  setCurBounty = (bounty) => {
    this.setState({
      curBounty: bounty
    });
  };

  getBounty = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={cx(tableStyles.noOverflow, tableStyles.description)} onClick={() => this.setCurBounty(row)}>{row.description}</div>
    );

    return cellWrapper;
  };

  getName = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={tableStyles.noOverflow}>{row.proposerName}</div>
    );

    return cellWrapper;
  }

  getGame = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={tableStyles.noOverflow}>{row.game}</div>
    );
    return cellWrapper
  }

  getMoney = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={cx(tableStyles.noOverflow, tableStyles.money)}>${row.contractAmount.toFixed(2)}</div>
    )
    return cellWrapper;
  }

  columns = [{
    dataField: 'description',
    text: 'Bounty',
    formatter: this.getBounty,
    headerStyle: { width: '40%' }
  }, {
    dataField: 'proposerName',
    text: 'Submitted by',
    formatter: this.getName
  }, {
    dataField: 'game',
    formatter: this.getGame,
    text: 'Game'
  }, {
    dataField: 'contractAmount',
    text: 'Amount',
    formatter: this.getMoney
  }, {
    dataField: 'action',
    text: '',
    isDummyField : true,
    headerStyle: { width: '10%' }
  }];

  getTable() {
    if (this.props.totalPublicBounties > 0) {
      const pagOptions = {
        totalSize: this.props.totalPublicBounties,
        sizePerPage: pageSize,
        hideSizePerPage: true,
        page: this.state.curPage
      };

      return (
        <div className={cx(tableStyles.table, styles.mainContent)}>
          <BootstrapTable
            remote
            keyField='contractId'
            data={this.props.publicBounties}
            columns={this.columns}
            pagination={paginationFactory(pagOptions)}
            onTableChange={this.tableChange}
          />
        </div>
      );
    } else {
      return this.getEmptyContent();
    }
  }

  getEmptyContent() {
    return (
      <div className={cx(styles.empty, styles.mainContent)}>
        {`No ${this.state.bountyFilter.toLowerCase()} bounties`}.
      </div>
    );
  }

  getContent() {
    if (this.state.curBounty !== null) {
      return (
        <div className={styles.mainContent}>
          <BountyDetails
            curBounty={this.state.curBounty}
            setCurBounty={this.setCurBounty}
          />
        </div>
      );
    }

    return this.getTable();
  }

  render() {
    const donateUrl = `/donate/${this.props.publicUser.displayName}`;

    return (
      <Grid className="content">
        <PublicActiveBountyContainer
          twitchUserName={this.props.publicUser.displayName}
        />
        <Col xs={3} md={2} className={profileStyles.sidebar}>
          <Row>
            <Image src={this.props.publicUser.profileImageUrl} thumbnail />
            <h2 className="name">{this.props.publicUser.displayName}</h2>
          </Row>
          <Row>
            <Button bsStyle="primary" className={styles.openBountyButton} href={donateUrl}>Open Bounty</Button>
          </Row>
        </Col>
        <Col xs={12} md={9}>
          <div className={styles.mainContent}>
            <Tabs
              activeKey={this.state.tabKey}
              onSelect={this.handleBountyFilterChange}
              id="contract-tabs"
            >
              <Tab eventKey={1} title="Open Bounties">
                {this.getContent()}
              </Tab>
              <Tab eventKey={2} title="Completed Bounties">
                {this.getContent()}
              </Tab>
              <Tab eventKey={3} title="Failed Bounties">
                {this.getContent()}
              </Tab>
            </Tabs>
          </div>
        </Col>
      </Grid>
    )
  }
}

UserComponent.propTypes = {
  publicUser: PropTypes.object.isRequired,
  publicBounties: PropTypes.array.isRequired,
  totalPublicBounties: PropTypes.number.isRequired,
  getPublicBounties: PropTypes.func.isRequired
};

export default UserComponent;
