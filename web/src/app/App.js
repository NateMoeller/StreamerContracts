import React, { Component } from 'react';
import { Route, BrowserRouter as Router } from 'react-router-dom';
import AboutContainer from '../app/about/AboutContainer';
import HomeContainer from '../app/home/HomeContainer';
import LoginContainer from '../app/login/LoginContainer';
import NavBar from '../app/common/navbar/NavbarComponent';
import ProfileContainer from '../app/profile/ProfileContainer';
import PublicRoute from '../app/common/publicRoute/PublicRoute';

class App extends Component {
  render() {
    return (
      <Router>
        <div className="App container">
          <NavBar />
          <Route exact path="/" component={HomeContainer} />
          <Route path="/home" component={HomeContainer} />
          <Route path="/about" component={AboutContainer} />
          <PublicRoute path="/login" component={LoginContainer} />
          <Route path="/profile" component={ProfileContainer} />
        </div>
      </Router>
    );
  }
}

export default App;
