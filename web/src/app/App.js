import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route
} from 'react-router-dom';
import HomeContainer  from '../app/home/HomeContainer';
import AboutContainer from '../app/about/AboutContainer';
import './App.css';

class App extends Component {

  render() {
    return (
      <Router>
        <div>
          <Route exact path='/' component={HomeContainer}/>
          <Route path='/home'   component={HomeContainer}/>
          <Route path='/about' component={AboutContainer}/>
        </div>
      </Router>
    );
  }
}

export default App;
