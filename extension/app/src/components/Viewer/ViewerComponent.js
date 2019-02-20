import React, { Component } from 'react';
import { Button, FormGroup, FormControl, Tooltip, OverlayTrigger, Glyphicon } from 'react-bootstrap';
import cx from 'classnames';
import PropTypes from 'prop-types';
import BootstrapTable from 'react-bootstrap-table-next';
import paginationFactory from 'react-bootstrap-table2-paginator';
import logo from '../../resources/logo_dark.png';
import styles from './ViewerStyles.scss';
import BountyDetails from '../../../../../web/src/app/profile/BountyDetails/BountyDetails';
import tableStyles from '../../../../../web/src/app/tableStyles.scss';

const PAGE_SIZE = 10;

class ViewerComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      curPage: 1,
      bountyFilter: 'OPEN',
      curBounty: null
    };

    this.getBounty = this.getBounty.bind(this);
    this.setCurBounty = this.setCurBounty.bind(this);
    this.tableChange = this.tableChange.bind(this);
    this.setBountyFilter = this.setBountyFilter.bind(this);
    this.getTable = this.getTable.bind(this);
    this.getContent = this.getContent.bind(this);
    this.refreshList = this.refreshList.bind(this);
    this.getMoney = this.getMoney.bind(this);
    this.getEmptyContent = this.getEmptyContent.bind(this);
    this.getAction = this.getAction.bind(this);

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
  }

  componentDidMount() {
    this.refreshList();
  }

  setBountyFilter(e) {
    this.props.setBountyFilter(e.target.value);
    this.setState({
      bountyFilter: e.target.value,
      curPage: 1
    }, this.refreshList);
  }

  refreshList() {
    this.setState({
      curBounty: null
    });
    this.props.getPublicBounties(0, PAGE_SIZE, this.props.publicUser.displayName, this.state.bountyFilter);
  }

  tableChange(type, newState) {
    if (type === 'pagination') {
      this.setState({
        curPage: newState.page
      }, () => {
        this.props.getPublicBounties(newState.page - 1, newState.sizePerPage, this.props.publicUser.displayName, this.state.bountyFilter);
      });
    }
  }

  setCurBounty(bounty) {
    this.setState({
      curBounty: bounty
    });
  }

  getBounty(cell, row, rowIndex, formatExtraData) {
    const cellWrapper = (
      <div className={cx(tableStyles.noOverflow, tableStyles.description)} onClick={() => this.setState({ curBounty: row })}>{row.description}</div>
    );

    return cellWrapper;
  }

  getMoney(cell, row, rowIndex, formatExtraData) {
    const cellWrapper = (
      <div className={cx(tableStyles.noOverflow, tableStyles.money)}>${row.contractAmount.toFixed(2)}</div>
    )
    return cellWrapper;
  }

  getAction(cell, row, rowIndex, formatExtraData) {
    if (row.state === 'COMPLETED') {
      return this.getCompletedIcon();
    } else if (row.state === 'EXPIRED' || row.state === 'DECLINED' || row.state === 'FAILED') {
      return this.getRemoveIcon(row);
    } else if (row.state === 'ACTIVE' || row.state === 'OPEN') {
      if (row.userVote === 'COMPLETED') {
        return this.getVotedCompletedIcon();
      }
    }

    return '';
  };

  getCompletedIcon() {
    const tooltip = (
      <Tooltip id="tooltip">
        Bounty Completed
      </Tooltip>
    );

    return (
      <OverlayTrigger placement="top" overlay={tooltip}>
        <Glyphicon glyph="ok-circle" className={styles.checkmark} />
      </OverlayTrigger>
    );
  }

  getVotedCompletedIcon() {
    const tooltip = (
      <Tooltip id="tooltip">
        Broadcaster has marked this bounty complete.
      </Tooltip>
    );

    return (
      <OverlayTrigger placement="top" overlay={tooltip}>
        <Glyphicon glyph="ok-circle" className={cx(styles.checkmark, styles.voted)} />
      </OverlayTrigger>
    );
  }

  getRemoveIcon(bounty) {
    let tooltipText = 'Bounty Declined';
    if (bounty.state === 'EXPIRED') {
      tooltipText = 'This bounty has expired';
    } else if (bounty.state === 'FAILED') {
      tooltipText = 'Bounty Failed';
    }

    const tooltip = (
      <Tooltip id="tooltip">
        {tooltipText}
      </Tooltip>
    );

    return (
      <OverlayTrigger placement="top" overlay={tooltip}>
        <Glyphicon glyph="remove-circle" className={styles.error} />
      </OverlayTrigger>
    );
  }

  getTable() {
    if (this.props.totalPublicBounties > 0) {
      const pagOptions = {
        totalSize: this.props.totalPublicBounties,
        sizePerPage: PAGE_SIZE,
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

  getContent() {
    if (this.state.curBounty !== null) {
      return (
        <div className={styles.mainContent}>
          <BountyDetails
            curBounty={this.state.curBounty}
            setCurBounty={this.setCurBounty}
            isExtension
          />
        </div>
      );
    }

    return this.getTable();
  }

  getEmptyContent() {
    return (
      <div className={cx(styles.empty, styles.mainContent)}>
        {`No ${this.state.bountyFilter.toLowerCase()} bounties`}.
      </div>
    );
  }

  render() {
    return (
      <div>
        <div className={styles.action}>
          <Button variant="primary" className={styles.button} onClick={this.props.openBounty}>Open Bounty</Button>
          <form className={styles.selectDropdown}>
            <FormGroup controlId="formControlsSelect">
              <FormControl
                componentClass="select"
                placeholder="Type"
                onChange={this.setBountyFilter}
                value={this.state.bountyFilter}
              >
                <option value="">All</option>
                <option value={'OPEN'}>Open</option>
                <option value={'ACTIVE'}>Active</option>
                <option value={'COMPLETED'}>Completed</option>
                <option value={'DECLINED'}>Declined</option>
                <option value={'FAILED'}>Failed</option>
              </FormControl>
            </FormGroup>
          </form>
        </div>
        <div>
          {this.getContent()}
        </div>
      </div>
    );
  }
}

ViewerComponent.defaultProps = {
  publicUser: null
}

ViewerComponent.propTypes = {
  publicUser: PropTypes.object,
  publicBounties: PropTypes.array.isRequired,
  totalPublicBounties: PropTypes.number.isRequired,
  getPublicBounties: PropTypes.func.isRequired,
  openBounty: PropTypes.func.isRequired,
  setBountyFilter: PropTypes.func.isRequired
};

export default ViewerComponent;
