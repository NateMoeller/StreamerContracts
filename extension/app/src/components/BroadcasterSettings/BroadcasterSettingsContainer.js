import React, { Component } from 'react';
import PropTypes from 'prop-types';
import LoadingComponent from '../../../../../web/src/app/common/loading/LoadingComponent';
import SettingsComponent from '../../../../../web/src/app/settings/SettingsComponent';

class BroadcasterSettingsContainer extends Component {
  constructor(props) {
    super(props);

    this.state = {
      paypalEmail: null,
      isBusinessEmail: null,
      loading: true
    };

    this.updatePayPalEmail = this.updatePayPalEmail.bind(this);
  }

  componentDidMount() {
    this.getSettings();
  }

  getSettings() {
    this.setState({ loading: true });
    const url = `${process.env.API_HOST}/userSettings`;
    this.props.Authentication.makeCall(url).then((response) => {
      return response.text();
    }).then(data =>{
      const settings = JSON.parse(data);
      console.log('settings', settings);
      this.setState({
        loading: false,
        paypalEmail: settings.paypalEmail,
        isBusinessEmail: settings.isBusinessEmail
      });
    });
  }

  updatePayPalEmail(payload) {
    console.log(payload);
    const newEmail = payload.paypalEmail;
    // TODO: this really should be a PUT request
    const url = `${process.env.API_HOST}/userSettings`;
    this.props.Authentication.makeCall(url, 'POST', payload, (response) => {
      return response;
    }).then(data => {
      this.setState({
        paypalEmail: newEmail
      });
    })
  }

  render() {
    if (this.state.loading) {
      return <LoadingComponent />;
    }

    return (
      <div>
        <SettingsComponent
          payPalEmail={this.state.paypalEmail}
          updatePayPalEmail={this.updatePayPalEmail}
        />
      </div>
    );
  }
}

BroadcasterSettingsContainer.propTypes = {
  Authentication: PropTypes.object.isRequired,
}

export default BroadcasterSettingsContainer;
