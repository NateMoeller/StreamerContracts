import { Component } from 'react';
import { withRouter } from 'react-router'

class RouteContainer extends Component {
  constructor(props) {
    super(props);

    this.props.history.listen((location, action) => {
      const pathHash = (/#!(\/.*)$/.exec(location.hash) || [])[1];
      const pathName = (/#!(\/.*)$/.exec(location.pathname) || [])[1];
      if (pathHash) {
          this.props.history.replace(pathHash);
       }
       if (pathName) {
         this.props.history.replace(pathName);
       }
    });
  }

  render() {
    return this.props.children;
  }
}

export default withRouter(RouteContainer);
