import React from 'react';
import Authentication from '../../util/Authentication/Authentication';
import BroadcasterContainer from '../Broadcaster/BroadcasterContainer';
import ViewerContainer from '../Viewer/ViewerContainer';
import logoDark from '../../resources/logo_dark.png';
import logoLight from '../../resources/logo_light.png';

import styles from './App.scss';

export default class App extends React.Component{
    constructor(props){
        super(props);
        this.Authentication = new Authentication();

        //if the extension is running on twitch or dev rig, set the shorthand here. otherwise, set to null.
        this.twitch = window.Twitch ? window.Twitch.ext : null;

        this.state= {
          finishedLoading: false,
          theme: 'light',
          isVisible: true,
          channelId: null
        };

        this.getBroadcaster = this.getBroadcaster.bind(this);
    }

    contextUpdate(context, delta){
        if (delta.includes('theme')) {
            this.setState(()=>{
                return { theme:context.theme }
            });
        }
    }

    visibilityChanged(isVisible){
      this.setState(()=> {
        return {
          isVisible
        }
      });
    }

    componentDidMount() {
        if(this.twitch) {
          this.twitch.onAuthorized((auth)=> {
            this.Authentication.setToken(auth.token, auth.userId)
            if(!this.state.finishedLoading){
              // if the component hasn't finished loading (as in we've not set up after getting a token), let's set it up now.
              // now we've done the setup for the component, let's set the state to true to force a rerender with the correct data.
              this.setState({
                finishedLoading:true,
                channelId: auth.channelId
              });
            }
          });

          this.twitch.onVisibilityChanged((isVisible,_c) => {
            this.visibilityChanged(isVisible);
          });

          this.twitch.onContext((context,delta) => {
            this.contextUpdate(context,delta);
          });
        }
    }

    componentWillUnmount() {
      if (this.twitch){
        this.twitch.unlisten('broadcast', () => console.log('successfully unlistened'));
      }
    }

    getBroadcaster(channelId, callback) {
      const url = `${process.env.API_HOST}/user/twitchId/${channelId}`;
      this.Authentication.makeCall(url).then((response) => {
        return response.text();
      }).then(data =>{
        callback(JSON.parse(data));
      });
    }

    render() {
      const logo = this.state.theme === 'light' ? logoDark : logoLight;
      if (this.state.finishedLoading && this.state.isVisible) {
        if (this.Authentication.state.role === 'broadcaster') {
          return (
            <div className={styles.test}>
              <div className={styles.header}>
                <img src={logo} alt="Bounty Streamer" width={200} height={50} />
              </div>
              <BroadcasterContainer
                Authentication={this.Authentication}
                channelId={this.state.channelId}
                getBroadcaster={this.getBroadcaster}
                twitch={this.twitch}
              />
            </div>
          );
        }

        return (
          <div>
            <div className={styles.header}>
              <img src={logo} alt="Bounty Streamer" width={200} height={50} />
            </div>
            <ViewerContainer
              Authentication={this.Authentication}
              channelId={this.state.channelId}
              getBroadcaster={this.getBroadcaster}
              twitch={this.twitch}
            />
          </div>
        );
        } else {
            return (
                <div className="App" />
            );
        }
    }
}
