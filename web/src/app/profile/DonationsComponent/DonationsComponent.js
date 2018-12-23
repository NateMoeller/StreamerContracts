import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import React, { Component } from 'react';
import { ButtonToolbar, DropdownButton, MenuItem, Popover, Button, OverlayTrigger, Tooltip, Glyphicon } from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import BountyDetails from '../BountyDetails/BountyDetails';
import paginationFactory from 'react-bootstrap-table2-paginator';
import cx from 'classnames';
import { OPEN, ACCEPTED, DECLINED, EXPIRED, COMPLETED, FAILED } from '../../BountyState';
import PropTypes from 'prop-types';
import LoadingComponent from '../../common/loading/LoadingComponent';
import styles from './DonationsComponentStyles.scss';

const pageSize = 10;

class DonationsComponent extends Component {
  constructor(props){
    super(props);

    this.state = {
      curBounty: null,
      curPage: 1
    };
  }

  componentDidMount() {
    this.refreshList();
  }

  refreshList = () => {
    this.setState({
      curBounty: null
    });
    this.props.listDonorBounties(0, pageSize);
  }

  tableChange = (type, newState) => {
    if (type === 'pagination') {
      this.setState({
        curPage: newState.page
      }, () => {
        this.props.listDonorBounties(newState.page - 1, newState.sizePerPage);
      });
    }
  }

  onVoteClick = (contractId, markedCompleted) => {
    const payload = {
      contractId: contractId,
      flagCompleted: markedCompleted
    };
    this.props.voteBounty(payload);
  }

  getName = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={styles.noOverflow}>{row.streamerName}</div>
    );

    return cellWrapper;
  }

  getGame = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={styles.noOverflow}>{row.game}</div>
    );
    return cellWrapper
  }

  getMoney = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={cx(styles.noOverflow, styles.money)}>${row.contractAmount.toFixed(2)}</div>
    )
    return cellWrapper;
  }

  getBounty = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={styles.description} onClick={() => this.setCurBounty(row)}>{row.description}</div>
    );

    return cellWrapper;
  };

  getAction = (cell, row, rowIndex, formatExtraData) => {
    if (row.state === COMPLETED) {
      return this.getCompletedIcon(row);
    } else if (row.state === EXPIRED || row.state === DECLINED || row.state === FAILED) {
      return this.getRemoveIcon(row);
    } else if (row.state === OPEN || row.state === ACCEPTED) {
      return this.getDropdownMenu(row);
    }

    return '';
  }

  getCompletedIcon(bounty) {
    const tooltip = (
      <Tooltip id="tooltip">
        {bounty.streamerName} completed this bounty.
      </Tooltip>
    );

    return (
      <OverlayTrigger placement="top" overlay={tooltip}>
        <Glyphicon glyph="ok" className={styles.checkmark} />
      </OverlayTrigger>
    );
  }

  getRemoveIcon(bounty) {
    let tooltipText = `${bounty.streamerName} declined this bounty`;
    if (bounty.state === EXPIRED) {
      tooltipText = `${bounty.streamerName} let this bounty expire`;
    } else if (bounty.state === FAILED) {
      tooltipText = `${bounty.streamerName} failed this bounty`;
    }

    const tooltip = (
      <Tooltip id="tooltip">
        {tooltipText}
      </Tooltip>
    );

    return (
      <OverlayTrigger placement="top" overlay={tooltip}>
        <Glyphicon glyph="remove" className={styles.error} />
      </OverlayTrigger>
    );
  }

  getDropdownMenu = (row) => {
    const completedPopover = (
      <Popover id="popover" title="Bounty completed?">
        <Button bsStyle="success" onClick={() => this.onVoteClick(row.contractId, true)}>{`${row.streamerName} completed bounty`}</Button>
      </Popover>
    );
    const failedPopover = (
      <Popover id="popover" title="Bounty Failed?">
        <Button bsStyle="danger" onClick={() => this.onVoteClick(row.contractId, false)}>{`${row.streamerName} failed bounty`}</Button>
      </Popover>
    );

    const kebab = (
      <div>
        <figure></figure>
        <figure className={styles.middle}></figure>
        <figure></figure>
      </div>
    );

    return (
      <ButtonToolbar>
        <DropdownButton
          bsStyle="default"
          title={kebab}
          noCaret
          id="dropdown-no-caret"
        >
          <OverlayTrigger trigger="focus" placement="left" overlay={completedPopover}>
            <MenuItem eventKey="1">{`${row.streamerName} completed bounty`}</MenuItem>
          </OverlayTrigger>
          <OverlayTrigger trigger="focus" placement="left" overlay={failedPopover}>
            <MenuItem eventKey="2">{`${row.streamerName} failed bounty`}</MenuItem>
          </OverlayTrigger>
        </DropdownButton>
      </ButtonToolbar>
    );
  }

  columns = [{
    dataField: 'description',
    text: 'Bounty',
    formatter: this.getBounty,
    headerStyle: { width: '50%' }
  }, {
    dataField: 'streamerName',
    text: 'Streamer Name',
    formatter: this.getName,
    headerStyle: { width: '15%' }
  }, {
    dataField: 'game',
    text: 'Game',
    formatter: this.getGame
  }, {
    dataField: 'contractAmount',
    text: 'Amount',
    headerSTyle: { width: '10%' },
    formatter: this.getMoney
  },{
    dataField: 'action',
    text: '',
    isDummyField : true,
    formatter: this.getAction,
    headerStyle: { width: '10%' }
  }];

  getEmptyContent() {
    return (
      <div className={styles.empty}>You haven't opened any bounties to streamers.</div>
    );
  }

  setCurBounty = (bounty) => {
    this.setState({
      curBounty: bounty
    });
  };

  render() {
    if (this.props.loading) {
      return <LoadingComponent />
    }

    if (this.state.curBounty === null) {
      if (this.props.totalDonations > 0) {
        const pagOptions = {
          totalSize: this.props.totalDonations,
          sizePerPage: pageSize,
          hideSizePerPage: true,
          page: this.state.curPage
        };
        return (
          <div className={styles.table}>
            <BootstrapTable
              remote
              keyField='contractId'
              data={this.props.donations}
              columns={this.columns}
              pagination={paginationFactory(pagOptions)}
              onTableChange={this.tableChange}
            />
          </div>
        );
      }

      return this.getEmptyContent();
    }

    return (
      <BountyDetails
        curBounty={this.state.curBounty}
        setCurBounty={this.setCurBounty}
        isStreamer={false}
      />
    );
  }
}

DonationsComponent.propTypes = {
  donations: PropTypes.array.isRequired,
  totalDonations: PropTypes.number.isRequired,
  listDonorBounties: PropTypes.func.isRequired,
  voteBounty: PropTypes.func.isRequired,
  loading: PropTypes.bool
};

DonationsComponent.defaultProps = {
  loading: false
};

export default DonationsComponent;
