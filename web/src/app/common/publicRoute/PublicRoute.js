import { Redirect, Route } from 'react-router-dom';
import PropTypes from 'prop-types';
import React from 'react';

/* global sessionStorage */
const PublicRoute = ({ component: Component, ...rest }) => ( // eslint-disable-line
  <Route
    {...rest}
    render={props => (
      sessionStorage.getItem('user')
        ? <Redirect to={{ pathname: '/profile', state: { from: props.location } }} />
        : <Component {...props} />
    )}
  />
);

PublicRoute.propTypes = {
  location: PropTypes.object.isRequired
};

export default PublicRoute;
