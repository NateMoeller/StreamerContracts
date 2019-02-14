import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import { Button, Glyphicon, PageHeader } from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import paginationFactory from 'react-bootstrap-table2-paginator';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';
import styles from './HomeStyles.scss';
import tableStyles from '../tableStyles.scss';

const pageSize = 10;

class HomeComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      curPage: 1
    };
  }

  componentDidMount() {
    this.props.getHomeBounties(0, 10);
  }

  tableChange = (type, newState) => {
    if (type === 'pagination') {
      this.setState({
        curPage: newState.page
      }, () => {
        this.props.getHomeBounties(newState.page - 1, newState.sizePerPage);
      });
    }
  }

  getBounty = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={cx(tableStyles.noOverflow, tableStyles.description)}>{row.description}</div>
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

  render() {
    const pagOptions = {
      totalSize: this.props.totalBounties,
      sizePerPage: pageSize,
      hideSizePerPage: true,
      page: this.state.curPage
    };

    return (
      <div className={styles.content}>
        <div className={cx(styles.section, styles.backgroundImage)}>
          <div className={styles.overlay} />
          <div className={cx(styles.section1, "container")}>
            <div className={styles.bigText}>
              Build Big Moments
            </div>
            <div className={styles.captionText}>
              Bounty streamer is a tool that helps broadcasters create interactive streams that allow viewers to challenge the streamer to in game tasks.
            </div>
            <div className={styles.joinButton}>
              <Button className={styles.button} variant="primary" href="/profile">Start Now</Button>
            </div>
          </div>
        </div>
        <div className={cx(styles.section, styles.section2)}>
          <div className="container">
            <PageHeader className={styles.header}>Create an interactive stream</PageHeader>
            <div className={styles.box}>
              <div className={styles.icon}>
                <Glyphicon glyph="user" />
              </div>
              <div className={styles.text}>
                Viewers open "bounties" with a monetary value for the streamer
              </div>
            </div>
            <div className={styles.box}>
              <div className={styles.icon}>
                <Glyphicon glyph="ok" />
              </div>
              <div className={styles.text}>
                Streamer attempts the bounty on stream
              </div>
            </div>
            <div className={styles.box}>
              <div className={cx(styles.icon, styles.money)}>
                $
              </div>
              <div className={styles.text}>
                Money is rewarded only when the bounty is completed
              </div>
            </div>
          </div>
        </div>
        <div className={cx(styles.section, styles.section3)}>
          <div className="container">
            <PageHeader className={styles.header}>Bounties</PageHeader>
            <div className={tableStyles.table}>
              <BootstrapTable
                remote
                keyField='contractId'
                data={this.props.bounties}
                columns={this.columns}
                pagination={paginationFactory(pagOptions)}
                onTableChange={this.tableChange}
              />
            </div>
          </div>
        </div>
      </div>

    );
  }
}

HomeComponent.propTypes = {
  getHomeBounties: PropTypes.func.isRequired,
  bounties: PropTypes.array.isRequired,
  totalBounties: PropTypes.number.isRequired
};


export default HomeComponent;
