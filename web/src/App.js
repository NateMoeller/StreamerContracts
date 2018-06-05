import axios from 'axios';
import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

class App extends Component {
  handleClick() {
      // Hard coded uuid: 92950a04-6606-11e8-adc0-fa7ae01bbebc that is set during DB initialization
        axios.get('api/92950a04-6606-11e8-adc0-fa7ae01bbebc').then(response => {
            alert('/api response: ' + response.data);
        })
  }

  render() {
    return (
      <div>
          <p>TEST</p>
          <a onClick={this.handleClick}>Click me to make a request to /api</a>
      </div>
    );
  }
}

export default App;
