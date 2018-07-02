import React, { Component } from 'react';

class LoginComponent extends Component {
  render() {
    const link = 'api/login';
    return (
      <div>
        <a href={link}>LOGIN WITH TWITCH</a>
      </div>
    );
  }
}


export default LoginComponent;
