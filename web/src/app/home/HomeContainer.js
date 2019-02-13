import React, { Component } from 'react';
import HomeComponent from './HomeComponent';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { homeOperations } from './duck';

class HomeContainer extends Component {
  render() {
    return (
      <div>
        <HomeComponent
          getHomeBounties={this.props.getHomeBounties}
          bounties={this.props.home.bounties}
          totalBounties={this.props.home.totalBounties}
        />
      </div>
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
  home: PropTypes.object.isRequired
};

export default connect(mapStateToProps, mapDispatchToProps)(HomeContainer);
