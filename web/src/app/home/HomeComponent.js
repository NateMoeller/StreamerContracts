import React, { Component } from 'react';
import PropTypes from 'prop-types';

class HomeComponent extends Component {
  render() {
    return (
      <div>
        <div onClick={this.props.fetchApi} role="button" tabIndex="0">Click me to ping the api endpoint using redux!</div>
        {this.props.showSpinner ? <div>Fetching Api...</div> : <div>Done.</div>}
      </div>
    );
  }
}

HomeComponent.propTypes = {
  fetchApi: PropTypes.func.isRequired,
  showSpinner: PropTypes.bool.isRequired
};


export default HomeComponent;
