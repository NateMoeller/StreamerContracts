import './LoginStyles.css';
import React, { Component } from 'react';
import { Button } from 'react-bootstrap';

class LoginComponent extends Component {
  render() {
    const link = process.env.REACT_APP_API_HOST + 'login';
    return (
      <div>
        <Button href={link} className="btn btn-twitch">
          <i className="fa fa-twitch" /> LOG IN WITH TWITCH
        </Button>
      </div>
    );
  }
}


export default LoginComponent;
