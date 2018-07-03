import { Redirect, Route } from 'react-router-dom';
import React from 'react';

/* global sessionStorage */
function getComponent(privateRoute, Component, props) {
  if (privateRoute) {
    return (
      sessionStorage.getItem('user')
        ? <Component {...props} />
        : <Redirect to={{ pathname: '/login', state: { from: props.location } }} /> // eslint-disable-line
    );
  }

  return (
    sessionStorage.getItem('user')
      ? <Redirect to={{ pathname: '/profile', state: { from: props.location } }} />
      : <Component {...props} />
  );
}

const ProtectedRoute = ({ component: Component, ...rest }) => { // eslint-disable-line
  return (
    <Route
      {...rest}
      render={props => (
        getComponent(rest.private, Component, props)
      )}
    />
  );
};

export default ProtectedRoute;
