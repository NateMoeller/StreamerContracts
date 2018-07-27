import React, { Component } from 'react';
import { Route, BrowserRouter as Router } from 'react-router-dom';
import AboutContainer from '../app/about/AboutContainer';
import AlertComponent from '../app/alert/AlertComponent';
import HomeContainer from '../app/home/HomeContainer';
import LoginContainer from '../app/login/LoginContainer';
import NavBar from '../app/common/navbar/NavbarComponent';
import ProfileContainer from '../app/profile/ProfileContainer';
import ProtectedRoute from '../app/common/protectedRoute/ProtectedRoute';
import RouteContainer from './RouteContainer';
import cx from 'classnames';
import styles from '../index.scss';

class App extends Component {
  render() {
    return (
      <Router>
        <RouteContainer>
          <Route path="/(|home|about|login|profile)" component={NavBar} />
          <div className={cx([styles.App, 'container'])}>
            <Route exact path="/" component={HomeContainer} />
            <Route path="/home" component={HomeContainer} />
            <Route path="/about" component={AboutContainer} />
            <ProtectedRoute path="/login" component={LoginContainer} />
            <Route path="/profile" component={ProfileContainer} />
          </div>
        </RouteContainer>
      </Router>
    );
  }
}

export default App;
