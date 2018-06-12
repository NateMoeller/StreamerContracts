import React, { Component } from 'react';
import HomeComponent from './HomeComponent';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { homeOperations } from './duck';

class HomeContainer extends Component {
  render() {
    const { fetchApi } = this.props; // get the fetchApi function from operations

    return (
      <HomeComponent
        fetchApi={fetchApi}
        showSpinner={this.props.home.showSpinner}
      />
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...homeOperations }, dispatch);
}

function mapStateToProps(state) {
  return {
    home: state.home
  };
}

HomeContainer.propTypes = {
  fetchApi: PropTypes.func.isRequired,
  home: PropTypes.object.isRequired
};

export default connect(mapStateToProps, mapDispatchToProps)(HomeContainer);
