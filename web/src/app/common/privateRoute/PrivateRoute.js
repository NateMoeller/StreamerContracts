import { Redirect, Route } from 'react-router-dom';
import PropTypes from 'prop-types';
import React from 'react';

/* global sessionStorage */
const PrivateRoute = ({ component: Component, ...rest }) => ( // eslint-disable-line
  <Route
    {...rest}
    render={props => (
      sessionStorage.getItem('user')
        ? <Component {...props} />
        : <Redirect to={{ pathname: '/login', state: { from: props.location } }} />
    )}
  />
);

PrivateRoute.propTypes = {
  location: PropTypes.object.isRequired
};

export default PrivateRoute;
