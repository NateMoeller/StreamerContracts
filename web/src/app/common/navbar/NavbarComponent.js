import { MenuItem, Nav, NavDropdown, NavItem, Navbar } from 'react-bootstrap';
import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { LinkContainer } from 'react-router-bootstrap';
import { connect } from 'react-redux';
import cx from 'classnames';
import Notification from '../../notification/Notification';
import logo from '../../../resources/logo_light.png';
import styles from './NavBarStyles.scss';

class NavbarComponent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      activeItem: ''
    };
  }

  setActiveItem(activeItem) {
    this.setState({
      activeItem
    });
  }

  getUserTitle(user) {
    if (user) {
      return (
        <div className="pull-left">
            <img className="thumbnail-image"
                src={user.profileImageUrl}
                alt="profileImage"
            />
            {user.displayName}
        </div>
      );
    }

    return null;
  }

  getRightMenu() {
    const user = JSON.parse(sessionStorage.getItem('user'));
    const userTitle = this.getUserTitle(user);
    const logoutLink = process.env.REACT_APP_API_HOST + 'logout';

    if (user === null) {
      return (
        <LinkContainer to="/login" className={styles.tab}>
          <NavItem href="/login">Login</NavItem>
        </LinkContainer>
      );
    }

    return (
      <NavDropdown eventKey={3} title={userTitle} id="basic-nav-dropdown" className={styles.dropdownMenu}>
        <LinkContainer exact to="/profile" onClick={() => this.setActiveItem('profile')}>
          <MenuItem eventKey={3.1}>My Profile</MenuItem>
        </LinkContainer>
        <LinkContainer exact to="/settings">
          <MenuItem eventKey={3.2}>Settings</MenuItem>
        </LinkContainer>
        <MenuItem divider />
        <MenuItem eventKey={3.4} href={logoutLink} onClick={() => {
          sessionStorage.removeItem('user');
          //TODO: switch to POST once CSRF is working
          //RestClient.POST('logout');
        }}>Logout</MenuItem>
      </NavDropdown>
    );
  }

  render() {
    const aboutClassname = this.state.activeItem === 'about' ? cx([styles.tab, styles.activeTab]) : styles.tab;
    const profileClassname = this.state.activeItem === 'profile' ? cx([styles.tab, styles.activeTab]) : styles.tab;
    const user = JSON.parse(sessionStorage.getItem('user'));

    return (
      <Navbar fluid collapseOnSelect className={styles.navBar}>
        <div className="container">
          <Navbar.Header>
            <Navbar.Brand className={styles.navbarBrand}>
              <Link to="/" href="/" onClick={() => this.setActiveItem('home')}>
                <img src={logo} alt="Bounty Streamer" width="200" height="50" />
              </Link>
            </Navbar.Brand>
            <Navbar.Toggle />
          </Navbar.Header>
          <Navbar.Collapse>
            <Nav>
              <LinkContainer exact to="/about" className={aboutClassname} onClick={() => this.setActiveItem('about')}>
                <NavItem>How it works</NavItem>
              </LinkContainer>
              <LinkContainer exact to="/profile" className={profileClassname} onClick={() => this.setActiveItem('profile')}>
                <NavItem>Profile</NavItem>
              </LinkContainer>
            </Nav>
            <Nav pullRight>
              {user !== null &&
                <Notification
                  alertChannelId={user.alertChannelId}
                />
              }
              {this.getRightMenu()}
            </Nav>
          </Navbar.Collapse>
        </div>
      </Navbar>
    );
  }
}

function mapStateToProps(state) {
  return {
    profile: state.profile
  };
}

export default connect(mapStateToProps, null)(NavbarComponent);
