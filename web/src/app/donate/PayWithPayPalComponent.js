import paypal from 'paypal-checkout';
import PropTypes from 'prop-types';
import React, { Component } from 'react';

const paypalSandboxClientID = process.env.REACT_APP_PAYPAL_SANDBOX_CLIENT_ID;
const paypalProductionClientID = process.env.REACT_APP_PAYPAL_SANDBOX_CLIENT_ID;
const paypalEnv = process.env.REACT_APP_PAYPAL_ENV + '';
const paypalButtonContainer = 'paypal-button-container';

class PayWithPayPalComponent extends Component {
  componentDidMount() {

    paypal.Button.render({
        env: paypalEnv, // sandbox | production
        // PayPal Client IDs - replace with your own
        // Create a PayPal app: https://developer.paypal.com/developer/applications/create
        client: {
            sandbox:  paypalSandboxClientID,
            production: paypalProductionClientID
        },
        // Show the buyer a 'Pay Now' button in the checkout flow
        commit: true,
        // payment() is called when the button is clicked
        payment: this.onPayment,
        // onAuthorize() is called when the buyer approves the payment
        onAuthorize: this.onAuthorize
    }, `#${paypalButtonContainer}`);
  }

  onPayment = (data, actions) => {
    // Make a call to the REST api to create the payment
    return actions.payment.create({
      payment: {
        transactions: [
          {
            payee: {
              email: this.props.streamerPaypalEmail
            },
            amount: {
              total: this.props.amount,
              currency: 'USD'
            }
          }
        ],
        intent: "authorize"
      },
      experience: {
        input_fields: {
          no_shipping: 1
        }
      }
    });
  };

  onAuthorize = (data, actions) => {
    const paymentId = data.paymentID;

    const payload = {
      username: this.props.username,
      amount: this.props.amount,
      bounty: this.props.bounty,
      payPalPaymentId: paymentId,
      streamerUsername: this.props.streamerUsername,
      game: this.props.gameName
    };
    // Make a call to the REST api to execute the payment. Unfortunatly 'paypal-checkout' service has a bug
    // that prevents executing payments when payee isnt the client's paypal account.
    // So we must do that on our backend. See this bug: https://github.com/paypal/paypal-checkout/issues/464
    this.props.insertBounty(payload);
  };

  render() {
    return (
      <div>
        <div id={`${paypalButtonContainer}`}></div>
      </div>
    );
  }
}

PayWithPayPalComponent.defaultProps = {
  gameName: null
};

PayWithPayPalComponent.propTypes = {
  amount: PropTypes.string.isRequired,
  streamerPaypalEmail: PropTypes.string.isRequired,
  bounty: PropTypes.string.isRequired,
  username: PropTypes.string.isRequired,
  insertBounty: PropTypes.func.isRequired,
  streamerUsername : PropTypes.string.isRequired,
  gameName: PropTypes.string
};

export default PayWithPayPalComponent;
