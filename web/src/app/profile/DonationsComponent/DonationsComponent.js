import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import React, { Component } from 'react';
import { Button, Col, Grid, Row } from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import paginationFactory from 'react-bootstrap-table2-paginator';
import PropTypes from 'prop-types';
import styles from './DonationsComponentStyles.scss';

class DonationsComponent extends Component {
  pageSize = 20;
  componentWillMount() {
    this.props.listOpenDonations(0, this.pageSize);
  }

  pageChange = (page, sizePerPage) => {
    this.props.listOpenDonations(page, sizePerPage);
  }

  pagination = paginationFactory({
    totalSize: this.props.totalOpenDonations,
    onPageChange: this.pageChange,
    sizePerPage: this.pageSize,
    hideSizePerPage: true
  })

  handleUpdateContractClick = (donationId, markedCompleted) => {
    const payload = {
      donationId: donationId,
      markedCompleted: markedCompleted
    };
    this.props.updateContract(payload);
  }

  contractActionsFormatter = (cell, row, rowIndex, formatExtraData) => {
    const donationId = row.donationId;
    const handleUpdateContractClick = formatExtraData;

    return (
      <Grid fluid={true}>
        <Row>
          <Col sm={12}>
            <Button
              bsStyle="success"
              onClick={() => {handleUpdateContractClick(donationId, true)}}
              block
            >Did It</Button>
          </Col>
        </Row>
        <Row className={styles.mt5}>
          <Col sm={12}>
            <Button
              bsStyle="danger"
              onClick={() => {handleUpdateContractClick(donationId, false)}}
              block
            >Failed</Button>
          </Col>
        </Row>
      </Grid>
    );
  }

  columns = [{
    dataField: 'streamerName',
    text: 'Streamer'
  }, {
    dataField: 'donationAmount',
    text: 'Amount'
  }, {
    dataField: 'description',
    text: 'Bounty'
  }, {
    dataField: 'action',
    text: 'Action',
    isDummyField : true,
    formatter: this.contractActionsFormatter,
    formatExtraData: this.handleUpdateContractClick
  }];

  render() {
    return (
      <BootstrapTable keyField='donationId' data={ this.props.openContracts } columns={ this.columns } pagination={ this.pagination } />
    );
  }
}

DonationsComponent.propTypes = {
  openContracts: PropTypes.arrayOf(
    PropTypes.shape({
      streamerName: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
      donationAmount: PropTypes.number.isRequired,
      donationId: PropTypes.string.isRequired
    }).isRequired
  ),
  totalOpenDonations: PropTypes.number.isRequired,
  listOpenDonations: PropTypes.func.isRequired,
  updateContract: PropTypes.func.isRequired
}

export default DonationsComponent;
