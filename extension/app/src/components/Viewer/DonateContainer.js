import React, { Component } from 'react';
import PropTypes from 'prop-types';
import DonateComponent from './DonateComponent';
import { BLACK_LISTED_WORDS } from '../../../../../web/src/app/BlackListedWords'


const REQUIRED_MESSAGE = 'This field is required';
const TOO_LONG_MESSAGE = 'Too many characters';

const MIN_AMOUNT = 0;
const MAX_AMOUNT = 1000;
const MAX_BOUNTY_LENGTH = 300;

class DonateContainer extends Component {
    constructor(props) {
        super(props);

        this.state = {
            topGames: [],
            game: '',
            bounty: '',
            bountyError: {},
            bountyWarning: {},
            loading: false
        };

        this.setSelectedGame = this.setSelectedGame.bind(this);
        this.editSelectedGame = this.editSelectedGame.bind(this);
        this.bountyChange = this.bountyChange.bind(this);
        this.submitForm = this.submitForm.bind(this);
        this.isSubmitEnabled = this.isSubmitEnabled.bind(this);
    }


    componentDidMount() {
        this.getTopGames();
    }


    getTopGames() {
        const url = 'twitch/topGames';
        this.props.Authentication.makeCall(`${process.env.API_HOST}/${url}`).then(response => {
            return response.text();
        }).then(data => {
            const gameData = JSON.parse(data);
            const games = gameData ? gameData.data : [];
            this.setState({
                topGames: games
            });
        });
    }

    setSelectedGame(selected) {
        if (selected[0]) {
            this.setState({ game: selected[0].name });
        }
    }

    editSelectedGame(text) {
        this.setState({
            game: text
        });
    }

    bountyChange(e) {
        const newBounty = e.target.value;
        this.setState({
            bounty: newBounty,
            blackListedBountyWords: this.getBlackListedBountyWords(newBounty)
        }, () => {
            this.validateBounty();
        });
    }

    getBlackListedBountyWords(bounty) {
        return BLACK_LISTED_WORDS.filter(blackListedWord => bounty.toLowerCase().includes(blackListedWord.toLowerCase()));
    }

    validateBounty() {
        console.log('validateBounty');
        let error = { type: null };
        let warning = { type: null };
        if (this.state.bounty === '') {
            error = { type: 'error', message: REQUIRED_MESSAGE };
        } else if (this.state.bounty.length >= MAX_BOUNTY_LENGTH) {
            error = { type: 'error', message: TOO_LONG_MESSAGE };
        } else if (this.state.blackListedBountyWords.length > 0) {
            warning = { type: 'warning', message: 'Caution. This bounty contains potentially offensive language and will be reviewed for abuse. If abuse is detected, this account will be banned.' }
        }

        this.setState({
            bountyError: error,
            bountyWarning: warning
        });
    }

    submitForm(e) {
        e.preventDefault();
        this.setState({
            loading: true
        });
        const payload = {
            username: this.props.viewerUserName,
            amount: 0,
            bounty: this.state.bounty,
            payPalPaymentId: '',
            streamerUsername: this.props.streamerUsername,
            game: this.state.game
        };

        const url = 'donations';
        this.props.Authentication.makeCall(`${process.env.API_HOST}/${url}`, 'POST', payload).then(response => {
            return response.text();
        }).then(() => {
            this.props.toggleOpenForm();
        });
    }

    isSubmitEnabled() {
        if (this.state.bountyError && this.state.bountyError.type === null) {
            return true;
        }

        return false;
    }

    render() {
        return (
            <DonateComponent
                topGames={this.state.topGames}
                setSelectedGame={this.setSelectedGame}
                editSelectedGame={this.editSelectedGame}
                bounty={this.state.bounty}
                bountyChange={this.bountyChange}
                submitForm={this.submitForm}
                bountyError={this.state.bountyError}
                bountyWarning={this.state.bountyWarning}
                isSubmitEnabled={this.isSubmitEnabled}
                toggleOpenForm={this.props.toggleOpenForm}
            />
        );
    }
}

DonateContainer.propTypes = {
    Authentication: PropTypes.object.isRequired,
    toggleOpenForm: PropTypes.func.isRequired,
    streamerUsername: PropTypes.string.isRequired,
    viewerUserName: PropTypes.string.isRequired
};

export default DonateContainer;