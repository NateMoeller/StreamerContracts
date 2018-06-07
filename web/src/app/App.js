import React, { Component } from 'react';
import { Route, BrowserRouter as Router } from 'react-router-dom';
import AboutContainer from '../app/about/AboutContainer';
import HomeContainer from '../app/home/HomeContainer';

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <Route exact path="/" component={HomeContainer} />
          <Route path="/home" component={HomeContainer} />
          <Route path="/about" component={AboutContainer} />
        </div>
      </Router>
    );
  }
}

export default App;
