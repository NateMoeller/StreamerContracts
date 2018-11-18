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
import paginationFactory from 'react-bootstrap-table2-paginator';
import PropTypes from 'prop-types';
import LoadingComponent from '../../common/loading/LoadingComponent';
import styles from '../DonationsComponent/DonationsComponentStyles.scss';

class MyBountiesComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      curBounty: null,
      bountyFilter: 'all'
    };
  }

  componentDidMount() {
    this.pageSize = 10;

    this.props.listMyBounties(0, this.pageSize, this.props.twitchUserName, this.state.bountyFilter);
  }

  pageChange = (page, sizePerPage) => {
    this.props.listMyBounties(page, sizePerPage, this.props.twitchUserName, this.state.bountyFilter);
  }

  getBounty = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={styles.description} onClick={() => this.setCurBounty(row)}>{row.description}</div>
    );

    return cellWrapper;
  };

  getAction = (cell, row, rowIndex, formatExtraData) => {
    if (row.isCompleted) {
      return this.getCompletedIcon();
    } else if (row.isExpired) {
      return this.getExpiredIcon();
    } else if (row.isAccepted) {
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

  getExpiredIcon() {
    const tooltip = (
      <Tooltip id="tooltip">
        This bounty has expired.
      </Tooltip>
    );

    return (
      <OverlayTrigger placement="top" overlay={tooltip}>
        <Glyphicon glyph="remove" className={styles.error} />
      </OverlayTrigger>
    );
  }

  getAcceptedDropdownMenu(row) {
    const completedPopover = (
      <Popover id="popover" title="Bounty completed?">
        <Button bsStyle="success" onClick={() => console.log('bounty completed')}>I completed this bounty</Button>
      </Popover>
    );
    const failedPopover = (
      <Popover id="popover" title="Bounty Failed?">
        <Button bsStyle="danger" onClick={() => console.log('bounty failed')}>I did not complete this bounty</Button>
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
            <MenuItem eventKey="1">Submit as completed</MenuItem>
          </OverlayTrigger>
          <OverlayTrigger trigger="focus" placement="left" overlay={failedPopover}>
            <MenuItem eventKey="2">Submit as failed</MenuItem>
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
        <Button bsStyle="success" onClick={() => this.props.acceptBounty(row.contractId)}>Accept bounty</Button>
      </Popover>
    );
    const removePopover = (
      <Popover id="popover" title="Remove bounty?">
        <Button bsStyle="danger" onClick={() => this.props.removeBounty(row.contractId)}>Remove bounty</Button>
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
            <MenuItem eventKey="2">Remove bounty</MenuItem>
          </OverlayTrigger>
          <MenuItem divider />
          <OverlayTrigger placement="left" overlay={tooltip}>
            <MenuItem eventKey="3">Report bounty</MenuItem>
          </OverlayTrigger>
        </DropdownButton>
      </ButtonToolbar>
    );
  }

  pagination = paginationFactory({
    totalSize: this.props.totalBounties,
    onPageChange: this.pageChange,
    sizePerPage: this.pageSize,
    hideSizePerPage: true
  });

  columns = [{
    dataField: 'description',
    text: 'Bounty',
    formatter: this.getBounty,
    headerStyle: { width: '40%' }
  }, {
    dataField: 'bountyOwnerName',
    text: 'Submitted by',
  }, {
    dataField: 'game',
    text: 'Game'
  }, {
    dataField: 'contractAmount',
    text: 'Amount',
    formatter: (cell, row, rowIndex) => `$${row.contractAmount.toFixed(2)}`
  }, {
    dataField: 'expiresAt',
    text: 'Expires at'
  }, {
    dataField: 'action',
    text: '',
    isDummyField : true,
    formatter: this.getAction,
    headerStyle: { width: '10%' }
  }];

  getEmptyContent() {
    return (
      <div>Your viewers haven't opened any bounties. Encourage them by putting a banner under your stream. [PUT IN IMAGE OF BANNER]</div>
    );
  }

  setCurBounty = (bounty) => {
    this.setState({
      curBounty: bounty
    });
  };

  setBountyFilter = (e) => {
    this.setState({
      bountyFilter: e.target.value
    }, () => {
      this.props.listMyBounties(0, this.pageSize, this.props.twitchUserName, this.state.bountyFilter);
    });
  };

  getTable() {
    return (
      <div className={styles.table}>
        <div className={styles.typeDropdown}>
          <form>
            <FormGroup controlId="formControlsSelect">
              <FormControl
                componentClass="select"
                placeholder="Type"
                onChange={this.setBountyFilter}
                value={this.state.bountyFilter}
              >
                <option value="all">All</option>
                <option value="open">Open</option>
                <option value="accepted">Accepted</option>
                <option value="completed">Completed</option>
                <option value="expired">Expired</option>
              </FormControl>
            </FormGroup>
          </form>
        </div>
        <BootstrapTable
          keyField='contractId'
          data={this.props.bounties}
          columns={this.columns}
          pagination={this.pagination}
        />
      </div>
    );
  }

  render() {
    if (this.props.loading) {
      return <LoadingComponent />;
    }

    if (this.props.bounties.length > 0) {
      if (this.state.curBounty === null) {
        return this.getTable();
      } else {
        return (
          <BountyDetails
            curBounty={this.state.curBounty}
            setCurBounty={this.setCurBounty}
            acceptBounty={this.props.acceptBounty}
            removeBounty={this.props.removeBounty}
          />
        );
      }

    }

    return this.getEmptyContent();
  }
}

MyBountiesComponent.propTypes = {
  twitchUserName: PropTypes.string.isRequired,
  loading: PropTypes.bool,
  listBounties: PropTypes.func.isRequired,
  bounties: PropTypes.array.isRequired,
  totalBounties: PropTypes.number.isRequired,
  acceptBounty: PropTypes.func.isRequired,
  removeBounty: PropTypes.func.isRequired,
}

MyBountiesComponent.defaultProps = {
  loading: false
}

export default MyBountiesComponent;
