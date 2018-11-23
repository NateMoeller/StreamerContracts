import React, { Component } from 'react';
import SettingsComponent from './SettingsComponent';

class SettingsContainer extends Component {
  render() {
    return <SettingsComponent payPalEmail={'n.moeller18@gmail.com'} />;
  }
}

export default SettingsContainer;
