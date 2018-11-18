import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import React, { Component } from 'react';
import { ButtonToolbar, DropdownButton, MenuItem } from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import paginationFactory from 'react-bootstrap-table2-paginator';
import PropTypes from 'prop-types';
import LoadingComponent from '../../common/loading/LoadingComponent';
import styles from './DonationsComponentStyles.scss';

class DonationsComponent extends Component {
  pageSize = 20;
  componentDidMount() {
    this.props.listMyDonations(0, this.pageSize);
  }

  pageChange = (page, sizePerPage) => {
    this.props.listMyDonations(page, sizePerPage);
  }

  pagination = paginationFactory({
    totalSize: this.props.totalDonations,
    onPageChange: this.pageChange,
    sizePerPage: this.pageSize,
    hideSizePerPage: true
  });

  onVoteClick = (donationId, markedCompleted) => {
    const payload = {
      donationId: donationId,
      markedCompleted: markedCompleted
    };
    this.props.voteBounty(payload);
  }

  getBounty = (cell, row, rowIndex, formatExtraData) => {
    const cellWrapper = (
      <div className={styles.description}>{row.description}</div>
    );

    return cellWrapper;
  };

  getDropdownMenu = (cell, row, rowIndex, formatExtraData) => {
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
          <MenuItem eventKey="1">{`${row.streamerName} completed bounty`}</MenuItem>
          <MenuItem eventKey="2">{`${row.streamerName} failed bounty`}</MenuItem>
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
    headerStyle: { width: '25%' }
  }, {
    dataField: 'donationAmount',
    text: 'Amount',
    headerSTyle: { width: '15%' }
  },{
    dataField: 'action',
    text: '',
    isDummyField : true,
    formatter: this.getDropdownMenu,
    headerStyle: { width: '10%' }
  }];

  getEmptyContent() {
    return (
      <div>You haven't opened any bounties to streamers. Use the directory to find a streamer using BountyStreamer.</div>
    );
  }

  render() {
    if (this.props.loading) {
      return <LoadingComponent />
    }

    if (this.props.donations.length > 0) {
      return (
        <div className={styles.table}>
          <BootstrapTable
            keyField='donationId'
            data={this.props.donations}
            columns={this.columns}
            pagination={this.pagination}
          />
        </div>
      );
    }

    return this.getEmptyContent();
  }
}

DonationsComponent.propTypes = {
  donations: PropTypes.arrayOf(
    PropTypes.shape({
      streamerName: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
      donationAmount: PropTypes.number.isRequired,
      donationId: PropTypes.string.isRequired
    }).isRequired
  ),
  totalDonations: PropTypes.number.isRequired,
  listDonations: PropTypes.func.isRequired,
  voteBounty: PropTypes.func.isRequired,
  loading: PropTypes.bool
};

DonationsComponent.defaultProps = {
  loading: false
};

export default DonationsComponent;
