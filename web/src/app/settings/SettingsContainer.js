import React, { Component } from 'react';
import SettingsComponent from './SettingsComponent';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { settingsOperations } from './duck';

class SettingsContainer extends Component {
  componentDidMount() {
    this.props.getSettings();
  }

  render() {
    const user = JSON.parse(sessionStorage.getItem('user'));

    return (
      <SettingsComponent
        user={user}
        payPalEmail={this.props.settings.payPalEmail}
        updatePayPalEmail={this.props.updatePayPalEmail}
      />
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...settingsOperations }, dispatch);
}

function mapStateToProps(state) {
  return {
    settings: state.settings
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(SettingsContainer);
