import { Component } from 'react';
import { withRouter } from 'react-router'

class RouteContainer extends Component {
  constructor(props) {
    super(props);

    this.props.history.listen((location, action) => {
      const path = (/#!(\/.*)$/.exec(location.hash) || [])[1];
      if (path) {
          this.props.history.replace(path);
       }
    });
  }

  render() {
    return this.props.children;
  }
}

export default withRouter(RouteContainer);
