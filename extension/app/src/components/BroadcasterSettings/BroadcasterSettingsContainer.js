import React, { Component } from 'react';
import PropTypes from 'prop-types';
import LoadingComponent from '../../../../../web/src/app/common/loading/LoadingComponent';
import SettingsComponent from '../../../../../web/src/app/settings/SettingsComponent';

class BroadcasterSettingsContainer extends Component {
  constructor(props) {
    super(props);

    this.state = {
      user: null,
      paypalEmail: null,
      isBusinessEmail: null,
      loading: true
    };

    this.updatePayPalEmail = this.updatePayPalEmail.bind(this);
  }

  componentDidMount() {
    this.setState({ loading: true });
    this.props.getBroadcaster(this.props.channelId, (data) => {
      this.setState({
        user: data
      }, () => {
        this.getSettings();
      });
    });
  }

  getSettings() {
    const url = `${process.env.API_HOST}/userSettings`;
    this.props.Authentication.makeCall(url).then((response) => {
      return response.text();
    }).then(data =>{
      const settings = data ? JSON.parse(data) : null;
      this.setState({
        loading: false,
        paypalEmail: settings ? settings.paypalEmail : null,
        isBusinessEmail: settings ? settings.isBusinessEmail : null
      });
    });
  }

  updatePayPalEmail(payload) {
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
          user={this.state.user}
          payPalEmail={this.state.paypalEmail}
          updatePayPalEmail={this.updatePayPalEmail}
        />
      </div>
    );
  }
}

BroadcasterSettingsContainer.propTypes = {
  Authentication: PropTypes.object.isRequired,
  getBroadcaster: PropTypes.func.isRequired,
  channelId: PropTypes.string.isRequired
}

export default BroadcasterSettingsContainer;
