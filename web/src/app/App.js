import React, { Component } from 'react';
import { Route, BrowserRouter as Router } from 'react-router-dom';
import AboutContainer from '../app/about/AboutContainer';
import AlertContainer from '../app/alert/AlertContainer';
import DonateContainer from '../app/donate/DonateContainer';
import HomeContainer from '../app/home/HomeContainer';
import LoginContainer from '../app/login/LoginContainer';
import NavBar from '../app/common/navbar/NavbarComponent';
import ProfileContainer from '../app/profile/ProfileContainer';
import ProtectedRoute from '../app/common/protectedRoute/ProtectedRoute';
import UserContainer from '../app/user/UserContainer';
import SplashComponent from '../app/splash/SplashComponent';
import SettingsContainer from '../app/settings/SettingsContainer';
import cx from 'classnames';
import styles from '../index.scss';

class App extends Component {
  render() {
    return (
      <Router>
        <div>
          <Route path="/(|home|about|login|profile|donate|user|settings)" component={NavBar} />
          <Route path="/alert/:alertChannelId" component={AlertContainer} />
          <Route path="/splash" component={SplashComponent} />
          <div className={cx([styles.App, 'container'])}>
            <Route exact path="/" component={HomeContainer} />
            <Route path="/home" component={HomeContainer} />
            <Route path="/about" component={AboutContainer} />
            <ProtectedRoute path="/login" component={LoginContainer} />
            <Route path="/profile" component={ProfileContainer} />
            <ProtectedRoute private path="/donate/:twitchUserName" component={DonateContainer} />
            <ProtectedRoute private path="/user/:twitchUserName" component={UserContainer} />
            <ProtectedRoute private path="/settings" component={SettingsContainer} />
          </div>
        </div>
      </Router>
    );
  }
}

export default App;
