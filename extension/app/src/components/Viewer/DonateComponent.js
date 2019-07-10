import React, { Component } from 'react';
import {
    FormGroup,
    ControlLabel,
    FormControl,
    Button,
} from 'react-bootstrap';
import { Typeahead } from 'react-bootstrap-typeahead';
import PropTypes from 'prop-types';
import styles from './ViewerStyles.scss'

class DonateComponent extends Component {

    getGameOptions() {
        return this.props.topGames.map((game, idx) => ({
            id: idx,
            img: game.box_art_url,
            name: game.name,
        }));
    }

    renderOption(option, props, index) {
        const width = 50;
        const height = 75;
        const url = option.img.replace('{width}', width).replace('{height}', height);

        return (
            <div>
                <img src={url} alt={option.name} width={width} height={height} />
                {option.name}
            </div>
        );
    }

    getErrorState() {
        if (this.props.bountyError && this.props.bountyError.type) {
            return this.props.bountyError.type;
        } else if (this.props.bountyWarning && this.props.bountyWarning.type) {
            return this.props.bountyWarning.type;
        }

        return null;
    }

    getErrorMessage() {
        if (this.props.bountyError && this.props.bountyError.message) {
            return this.props.bountyError.message;
        } else if (this.props.bountyWarning && this.props.bountyWarning.message) {
            return this.props.bountyWarning.message;
        }

        return null;
    }

    render() {
        return (
            <div>
                <form>
                    <FormGroup controlId="formControlsSelect">
                        <ControlLabel>Game</ControlLabel>
                        <Typeahead
                            labelKey="name"
                            onChange={this.props.setSelectedGame}
                            onInputChange={this.props.editSelectedGame}
                            options={this.getGameOptions()}
                            renderMenuItemChildren={this.renderOption}
                        />
                    </FormGroup>
                    <FormGroup
                        controlId="bountyMessage"
                        validationState={this.getErrorState()}
                    >
                        <ControlLabel>Bounty message</ControlLabel>
                        <FormControl
                            componentClass="textarea"
                            value={this.props.bounty}
                            placeholder="Ex: Win the next game with a no scope kill"
                            onChange={this.props.bountyChange}
                            style={{ height: '75px' }}
                            title={this.getErrorMessage()}
                        />
                    </FormGroup>
                    <div>
                        <div className={styles.backButton}>
                            <Button
                                bsStyle="default"
                                onClick={this.props.toggleOpenForm}
                            >
                                Back
                            </Button>
                        </div>
                        <div className={styles.submitButton}>
                            <Button
                                type="submit"
                                bsStyle="success"
                                onClick={this.props.submitForm}
                                disabled={!this.props.isSubmitEnabled()}
                            >
                                Open Bounty
                            </Button>
                        </div>
                    </div>
                </form>

            </div>
        );
    }
}

DonateComponent.propTypes = {
    topGames: PropTypes.array.isRequired,
    setSelectedGame: PropTypes.func.isRequired,
    editSelectedGame: PropTypes.func.isRequired,
    bounty: PropTypes.string.isRequired,
    bountyChange: PropTypes.func.isRequired,
    submitForm: PropTypes.func.isRequired,
    bountyError: PropTypes.object.isRequired,
    bountyWarning: PropTypes.object.isRequired,
    isSubmitEnabled: PropTypes.func.isRequired,
    toggleOpenForm: PropTypes.func.isRequired,
};

export default DonateComponent;