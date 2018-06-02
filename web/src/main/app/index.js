import axios from 'axios';
import React from 'react';
import ReactDOM from 'react-dom'

class Greeting extends React.Component {

    handleClick() {
//    Hard coded uuid: 92950a04-6606-11e8-adc0-fa7ae01bbebc that is set during DB initialization
        axios.get('api/92950a04-6606-11e8-adc0-fa7ae01bbebc').then(response => {
            alert('/api response: ' + response.data);
        })
    }

    render() {
        return (
            <div>
                <p>Hello From React!</p>
                <a onClick={this.handleClick}>Click me to make a request to /api... if you get an alert, nginx routing + API are working</a>
            </div>
        );
    }
}
ReactDOM.render(
    <Greeting />,
    document.getElementById('root')
);
