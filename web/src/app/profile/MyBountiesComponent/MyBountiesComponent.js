import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import React, { Component } from 'react';
import {
  ButtonToolbar,
  DropdownButton,
  MenuItem,
  Tooltip,
  OverlayTrigger,
  Popover,
  Button,
  FormGroup,
  FormControl,
  Glyphicon
} from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import BountyDetails from '../BountyDetails/BountyDetails';
import cx from 'classnames';
import paginationFactory from 'react-bootstrap-table2-paginator';
import PropTypes from 'prop-types';
import { OPEN, ACCEPTED, DECLINED, EXPIRED, COMPLETED, FAILED } from '../../BountyState';
import LoadingComponent from '../../common/loading/LoadingComponent';
import styles from '../DonationsComponent/DonationsComponentStyles.scss';
import tableStyles from '../../tableStyles.scss';

const pageSize = 10;

class MyBountiesComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      curBounty: null,
      curPage: 1,
      bountyFilter: OPEN
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
        this.props.listStreamerBounties(newState.page - 1, newState.sizePerPage, this.props.twitchUserName, this.state.bountyFilter);
      });
    }
  }

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

  getAction = (cell, row, rowIndex, formatExtraData) => {
    if (row.state === COMPLETED) {
      return this.getCompletedIcon();
    } else if (row.state === EXPIRED || row.state === DECLINED || row.state === FAILED) {
      return this.getRemoveIcon(row);
    } else if (row.state === ACCEPTED) {
      return this.getAcceptedDropdownMenu(row);
    }

    return this.getOpenDropdownMenu(row);
  };

  getCompletedIcon() {
    const tooltip = (
      <Tooltip id="tooltip">
        You've completed this bounty. Good job!
      </Tooltip>
    );

    return (
      <OverlayTrigger placement="top" overlay={tooltip}>
        <Glyphicon glyph="ok" className={styles.checkmark} />
      </OverlayTrigger>
    );
  }

  getRemoveIcon(bounty) {
    let tooltipText = 'Bounty Declined';
    if (bounty.state === EXPIRED) {
      tooltipText = 'This bounty has expired';
    } else if (bounty.state === FAILED) {
      tooltipText = 'Bounty Failed';
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

  getAcceptedDropdownMenu(row) {
    const voteCompletedPayload = {
      contractId: row.contractId,
      flagCompleted: true
    };
    const voteFailedPayload = {
      contractId: row.contractId,
      flagCompleted: false
    };

    const completedPopover = (
      <Popover id="popover" title="Bounty completed?">
        <Button bsStyle="success" onClick={() => this.onVoteBounty(voteCompletedPayload)}>I completed this bounty</Button>
      </Popover>
    );
    const failedPopover = (
      <Popover id="popover" title="Bounty Failed?">
        <Button bsStyle="danger" onClick={() => this.onVoteBounty(voteFailedPayload)}>I did not complete this bounty</Button>
      </Popover>
    );

    const kebab = (
      <div>
        <figure></figure>
        <figure></figure>
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
            <MenuItem eventKey="1">Mark completed</MenuItem>
          </OverlayTrigger>
          <OverlayTrigger trigger="focus" placement="left" overlay={failedPopover}>
            <MenuItem eventKey="2">Mark failed</MenuItem>
          </OverlayTrigger>
        </DropdownButton>
      </ButtonToolbar>
    );
  }

  getOpenDropdownMenu(row) {
    const tooltip = (
      <Tooltip id="tooltip">
        Offensive content? We'll review the bounty
      </Tooltip>
    );
    const acceptPopover = (
      <Popover id="popover" title="Accept bounty?">
        <Button bsStyle="success" onClick={() => this.onAcceptBounty(row.contractId)}>Accept bounty</Button>
      </Popover>
    );
    const removePopover = (
      <Popover id="popover" title="Decline bounty?">
        <Button bsStyle="danger" onClick={() => this.onDeclineBounty(row.contractId)}>Decline bounty</Button>
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
          <OverlayTrigger trigger="focus" placement="left" overlay={acceptPopover}>
            <MenuItem eventKey="1">Accept bounty</MenuItem>
          </OverlayTrigger>
          <OverlayTrigger trigger="focus" placement="left" overlay={removePopover}>
            <MenuItem eventKey="2">Decline bounty</MenuItem>
          </OverlayTrigger>
          <MenuItem divider />
          <OverlayTrigger placement="left" overlay={tooltip}>
            <MenuItem eventKey="3">Report bounty</MenuItem>
          </OverlayTrigger>
        </DropdownButton>
      </ButtonToolbar>
    );
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
    formatter: this.getAction,
    headerStyle: { width: '10%' }
  }];

  getEmptyContent() {
    return (
      <div className={styles.empty}>
        {`No ${this.state.bountyFilter.toLowerCase()} bounties`}.
      </div>
    );
  }

  getTable() {
    if (this.props.bounties.length > 0) {
      const pagOptions = {
        totalSize: this.props.totalBounties,
        sizePerPage: pageSize,
        hideSizePerPage: true,
        page: this.state.curPage
      };

      return (
        <BootstrapTable
          remote
          keyField='contractId'
          data={this.props.bounties}
          columns={this.columns}
          pagination={paginationFactory(pagOptions)}
          onTableChange={this.tableChange}
        />
      );
    }

    return this.getEmptyContent();
  }

  setCurBounty = (bounty) => {
    this.setState({
      curBounty: bounty
    });
  };

  setBountyFilter = (e) => {
    this.setState({
      bountyFilter: e.target.value,
      curPage: 1
    }, this.refreshList);
  };

  refreshList = () => {
    this.setState({
      curBounty: null
    });
    this.props.listStreamerBounties(0, pageSize, this.props.twitchUserName, this.state.bountyFilter);
  }

  onVoteBounty = (payload) => {
    this.props.voteBounty(payload, this.refreshList);
  }

  onAcceptBounty = (contractId) => {
    this.props.acceptBounty(contractId, this.refreshList);
  }

  onDeclineBounty = (contractId) => {
    this.props.removeBounty(contractId, this.refreshList);
  }

  render() {
    if (this.props.loading) {
      return <LoadingComponent />;
    }

    if (this.state.curBounty === null) {
      return (
        <div className={tableStyles.table}>
          <div className={tableStyles.typeDropdown}>
            <form>
              <FormGroup controlId="formControlsSelect">
                <FormControl
                  componentClass="select"
                  placeholder="Type"
                  onChange={this.setBountyFilter}
                  value={this.state.bountyFilter}
                >
                  <option value="">All</option>
                  <option value={OPEN}>Open</option>
                  <option value={ACCEPTED}>Active</option>
                  <option value={COMPLETED}>Completed</option>
                  <option value={DECLINED}>Declined</option>
                  <option value={EXPIRED}>Expired</option>
                  <option value={FAILED}>Failed</option>
                </FormControl>
              </FormGroup>
            </form>
          </div>
          {this.getTable()}
        </div>
      );
    } else {
      return (
        <BountyDetails
          curBounty={this.state.curBounty}
          setCurBounty={this.setCurBounty}
          onAcceptBounty={this.onAcceptBounty}
          onDeclineBounty={this.onDeclineBounty}
          onVoteBounty={this.onVoteBounty}
          isStreamer
        />
      );
    }
  }
}

MyBountiesComponent.propTypes = {
  twitchUserName: PropTypes.string.isRequired,
  loading: PropTypes.bool,
  bounties: PropTypes.array.isRequired,
  totalBounties: PropTypes.number.isRequired,
  acceptBounty: PropTypes.func.isRequired,
  removeBounty: PropTypes.func.isRequired,
  voteBounty: PropTypes.func.isRequired,
}

MyBountiesComponent.defaultProps = {
  loading: false
}

export default MyBountiesComponent;
