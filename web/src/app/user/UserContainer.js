import React, { Component } from 'react';
import LoadingComponent from '../common/loading/LoadingComponent';
import InvalidUserComponent from '../common/InvalidUser/InvalidUserComponent';
import UserComponent from './UserComponent';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { publicUserOperations } from './duck';

class UserContainer extends Component {
  componentWillMount() {
    const twitchUserName = this.props.match.params.twitchUserName;
    this.props.getPublicUser(twitchUserName);
  }

  render() {
    if (this.props.publicUser.getPublicUserLoading) {
      return <LoadingComponent />
    } else if (this.props.publicUser.publicUser === null) {
      return <InvalidUserComponent />
    }

    return (
      <UserComponent
        publicUser={this.props.publicUser.publicUser}
      />
    );
  }
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({ ...publicUserOperations }, dispatch);
}

function mapStateToProps(state) {
  return {
    publicUser: state.publicUser
  };
}


export default connect(mapStateToProps, mapDispatchToProps)(UserContainer);
