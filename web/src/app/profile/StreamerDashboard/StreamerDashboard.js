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
import { OPEN, ACTIVE, DECLINED, EXPIRED, COMPLETED, FAILED } from '../../BountyState';
import LoadingComponent from '../../common/loading/LoadingComponent';
import styles from '../DonationsComponent/DonationsComponentStyles.scss';
import tableStyles from '../../tableStyles.scss';

const pageSize = 10;

class StreamerDashboard extends Component {
  constructor(props) {
    super(props);

    this.state = {
      curBounty: null,
      curPage: 1,
      bountyFilter: props.bountyFilter ? props.bountyFilter : OPEN
    };

    if (this.props.isExtension) {
      this.columns = [{
        dataField: 'description',
        text: 'Bounty',
        formatter: this.getBounty,
        headerStyle: { width: '60%' }
      }, {
        dataField: 'contractAmount',
        text: 'Amount',
        formatter: this.getMoney,
      }, {
        dataField: 'action',
        text: '',
        isDummyField : true,
        formatter: this.getAction,
      }];
    } else {
      this.columns = [{
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
    }
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
    } else if (row.state === ACTIVE || row.state === OPEN) {
      if (row.userVote === COMPLETED) {
        return this.getVotedCompletedIcon();
      } else if (row.state === ACTIVE) {
        return this.getAcceptedDropdownMenu(row);
      }
      return this.getOpenDropdownMenu(row);
    }

    return '';
  };

  getCompletedIcon() {
    const tooltip = (
      <Tooltip id="tooltip">
        You've completed this bounty. Good job!
      </Tooltip>
    );
    const direction = this.props.isExtension ? 'left' : 'top';

    return (
      <OverlayTrigger placement={direction} overlay={tooltip}>
        <Glyphicon glyph="ok-circle" className={styles.checkmark} />
      </OverlayTrigger>
    );
  }

  getVotedCompletedIcon() {
    const tooltip = (
      <Tooltip id="tooltip">
        You have marked this bounty complete. The icon will fill in when voting has finished.
      </Tooltip>
    );
    const direction = this.props.isExtension ? 'left' : 'top';

    return (
      <OverlayTrigger placement={direction} overlay={tooltip}>
        <Glyphicon glyph="ok-circle" className={cx(styles.checkmark, styles.voted)} />
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
    const direction = this.props.isExtension ? 'left' : 'top';

    return (
      <OverlayTrigger placement={direction} overlay={tooltip}>
        <Glyphicon glyph="remove-circle" className={styles.error} />
      </OverlayTrigger>
    );
  }

  getAcceptedDropdownMenu(row) {
    const voteCompletedPayload = {
      contract: row,
      flagCompleted: true
    };
    const voteFailedPayload = {
      contract: row,
      flagCompleted: false
    };

    const completedPopover = (
      <Popover id="popover" title="Bounty completed?">
        <Button className={tableStyles.completeButton} bsStyle="success" onClick={() => this.onVoteBounty(voteCompletedPayload)}>I completed this bounty</Button>
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
    const direction = this.props.isExtension ? 'top' : 'left';
    const markCompleted = (
      <OverlayTrigger trigger="focus" placement={direction} overlay={completedPopover}>
        <MenuItem eventKey="1"><span className={tableStyles.complete}>Mark completed</span></MenuItem>
      </OverlayTrigger>
    );
    const markFailed = (
      <OverlayTrigger trigger="focus" placement={direction} overlay={failedPopover}>
        <MenuItem eventKey="2"><span className={tableStyles.error}>Mark failed</span></MenuItem>
      </OverlayTrigger>
    );

    return (
      <ButtonToolbar className={cx({ [tableStyles.extension] : this.props.isExtension })}>
        <DropdownButton
          bsStyle="default"
          title={kebab}
          noCaret
          id="dropdown-no-caret"
        >
          {markCompleted}
          {markFailed}
        </DropdownButton>
      </ButtonToolbar>
    );
  }

  getOpenDropdownMenu(row) {
    const voteCompletedPayload = {
      contract: row,
      flagCompleted: true
    };
    const acceptPopover = (
      <Popover id="popover" title="Activate bounty?">
        <Button bsStyle="success" onClick={() => this.onActivateBounty(row.contractId)}>Activate bounty</Button>
      </Popover>
    );
    const removePopover = (
      <Popover id="popover" title="Decline bounty?">
        <Button bsStyle="danger" onClick={() => this.onDeclineBounty(row.contractId)}>Decline bounty</Button>
      </Popover>
    );
    const markComplete = (
      <Popover id="popover" title="Mark complete?">
        <Button className={tableStyles.completeButton} bsStyle="success" onClick={() => this.onVoteBounty(voteCompletedPayload)}>Mark complete</Button>
      </Popover>
    );

    const kebab = (
      <div>
        <figure></figure>
        <figure className={styles.middle}></figure>
        <figure></figure>
      </div>
    );

    const direction = 'left';

    return (
      <ButtonToolbar className={cx({ [tableStyles.extension] : this.props.isExtension })}>
        <DropdownButton
          bsStyle="default"
          title={kebab}
          noCaret
          id="dropdown-no-caret"
        >
          <OverlayTrigger trigger="focus" placement={direction} overlay={acceptPopover}>
            <MenuItem eventKey="1"><span className={tableStyles.activate}>Activate bounty</span></MenuItem>
          </OverlayTrigger>
          <OverlayTrigger trigger="focus" placement={direction} overlay={removePopover}>
            <MenuItem eventKey="4"><span className={tableStyles.error}>Decline bounty</span></MenuItem>
          </OverlayTrigger>
          <MenuItem divider />
          <OverlayTrigger trigger="focus" placement={direction} overlay={markComplete}>
            <MenuItem eventKey="2"><span className={tableStyles.complete}>Mark complete</span></MenuItem>
          </OverlayTrigger>
        </DropdownButton>
      </ButtonToolbar>
    );
  }

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
    // TODO: bad practice to have duplicate states...fine for now
    if (this.props.setBountyFilter) {
      this.props.setBountyFilter(e.target.value);
    }
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

  onActivateBounty = (contractId) => {
    this.props.activateBounty(contractId, this.refreshList);
  }

  onDeclineBounty = (contractId) => {
    this.props.declineBounty(contractId, this.refreshList);
  }

  render() {
    if (this.props.loading) {
      return <LoadingComponent />;
    }

    if (this.state.curBounty === null) {
      return (
        <div className={tableStyles.table}>
          <div className={cx(tableStyles.typeDropdown, { [tableStyles.typeDropdownExtension]: this.props.isExtension })}>
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
                  <option value={ACTIVE}>Active</option>
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
          onActivateBounty={this.onActivateBounty}
          onDeclineBounty={this.onDeclineBounty}
          onVoteBounty={this.onVoteBounty}
          isStreamer
          isExtension={this.props.isExtension}
        />
      );
    }
  }
}

StreamerDashboard.propTypes = {
  twitchUserName: PropTypes.string.isRequired,
  loading: PropTypes.bool,
  bounties: PropTypes.array.isRequired,
  totalBounties: PropTypes.number.isRequired,
  activateBounty: PropTypes.func.isRequired,
  declineBounty: PropTypes.func.isRequired,
  voteBounty: PropTypes.func.isRequired,
  bountyFilter: PropTypes.string,
  isExtension: PropTypes.bool,
  setBountyFilter: PropTypes.func
}

StreamerDashboard.defaultProps = {
  loading: false,
  bountyFilter: null,
  isExtension: false,
  setBountyFilter: null
}

export default StreamerDashboard;
