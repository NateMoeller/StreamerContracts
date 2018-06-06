import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import React, { Component } from 'react';
import HomeComponent from './HomeComponent';
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

export default connect(mapStateToProps, mapDispatchToProps)(HomeContainer);
