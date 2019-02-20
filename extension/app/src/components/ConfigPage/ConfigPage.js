import React from 'react';
import Authentication from '../../util/Authentication/Authentication';
import BroadcasterSettingsContainer from '../BroadcasterSettings/BroadcasterSettingsContainer';
import LoadingComponent from '../../../../../web/src/app/common/loading/LoadingComponent';
import logoDark from '../../resources/logo_dark.png';
import logoLight from '../../resources/logo_light.png';

import styles from './Config.scss'

export default class ConfigPage extends React.Component{
    constructor(props) {
        super(props);
        this.Authentication = new Authentication();

        //if the extension is running on twitch or dev rig, set the shorthand here. otherwise, set to null.
        this.twitch = window.Twitch ? window.Twitch.ext : null;
        this.state = {
            finishedLoading:false,
            theme:'light'
        };
    }

    contextUpdate(context, delta) {
        if (delta.includes('theme')) {
            this.setState(()=>{
                return { theme:context.theme }
            })
        }
    }

    componentDidMount() {
        // do config page setup as needed here
        if(this.twitch) {
            this.twitch.onAuthorized((auth)=>{
                this.Authentication.setToken(auth.token, auth.userId)
                if(!this.state.finishedLoading){
                    // if the component hasn't finished loading (as in we've not set up after getting a token), let's set it up now.

                    // now we've done the setup for the component, let's set the state to true to force a rerender with the correct data.
                    this.setState(()=>{
                        return { finishedLoading:true }
                    })
                }
            })

            this.twitch.onContext((context,delta)=>{
                this.contextUpdate(context,delta)
            })
        }
    }

    render() {
      const logo = this.state.theme === 'light' ? logoDark : logoLight;
      if (this.state.finishedLoading && this.Authentication.isModerator()) {
        return(
          <div>
            <div className={styles.header}>
              <img src={logo} alt="Bounty Streamer" width={200} height={50} />
              <BroadcasterSettingsContainer
                Authentication={this.Authentication}
              />
            </div>
          </div>

        );
      } else {
        return(
            <div>
              <LoadingComponent />
            </div>
        );
      }
    }
}
