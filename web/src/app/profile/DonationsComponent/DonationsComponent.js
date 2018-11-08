import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import React, { Component } from 'react';
import { Button, ButtonToolbar, Col, Grid, Row, DropdownButton, MenuItem } from 'react-bootstrap';
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
          <MenuItem eventKey="1">Accept bounty</MenuItem>
          <MenuItem eventKey="2">Remove bounty</MenuItem>
          <MenuItem divider />
          <MenuItem eventKey="3">Report bounty</MenuItem>
        </DropdownButton>
      </ButtonToolbar>
    );
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
    dataField: 'description',
    text: 'Bounty',
    headerStyle: { width: '50%' }
  }, {
    dataField: 'streamerName',
    text: 'Submitted by',
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
    formatExtraData: this.handleUpdateContractClick,
    headerStyle: { width: '10%' }
  }];

  render() {
    return (
      <div className={styles.table}>
        <BootstrapTable
          keyField='donationId'
          data={this.props.openBounties}
          columns={this.columns}
          pagination={this.pagination}
        />
      </div>
    );
  }
}

DonationsComponent.propTypes = {
  openBounties: PropTypes.arrayOf(
    PropTypes.shape({
      streamerName: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
      donationAmount: PropTypes.number.isRequired,
      donationId: PropTypes.string.isRequired
    }).isRequired
  ),
  totalOpenDonations: PropTypes.number.isRequired,
  listOpenDonations: PropTypes.func.isRequired,
  updateContract: PropTypes.func.isRequired,
}

export default DonationsComponent;
