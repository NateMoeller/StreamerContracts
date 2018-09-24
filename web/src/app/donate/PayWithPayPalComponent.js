import paypal from 'paypal-checkout';
import PropTypes from 'prop-types';
import React, { Component } from 'react';
import RestClient from '../RestClient';

class PayWithPayPalComponent extends Component {
  // TODO: clean up code
  componentDidMount() {

    const streamerPaypalEmail = this.props.streamerPaypalEmail;
    const donationAmount = this.props.amount;
    const bounty = this.props.bounty;
    const username = this.props.username;
    const insertBounty = this.props.insertBounty;

    paypal.Button.render({

      env: 'sandbox', // sandbox | production

        // PayPal Client IDs - replace with your own
        // Create a PayPal app: https://developer.paypal.com/developer/applications/create
        client: {
            sandbox:    'AV_VKTQQlMAB8Fq58souv8_CazzqdHZS5pDF_H9L4eZIIH65I6LjvoSQVBHmgCk9-uJwtpsKxfOX9bop',
            production: '<insert production client id>'
        },

        // Show the buyer a 'Pay Now' button in the checkout flow
        commit: true,

        // payment() is called when the button is clicked
        payment: function(data, actions) {
          // Make a call to the REST api to create the payment
          return actions.payment.create({
            payment: {
              transactions: [
                {
                  payee: {
                    email: streamerPaypalEmail
                  },
                  amount: {
                    total: donationAmount,
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
        },

        // onAuthorize() is called when the buyer approves the payment
        onAuthorize: function(data, actions) {
          const paymentId = data.paymentID;

          const payload = {
            username: username,
            amount: donationAmount,
            bounty: bounty,
            payPalPaymentId: paymentId
          };
          // Make a call to the REST api to execute the payment. Unfortunatly 'paypal-checkout' service has a bug
          // that prevents executing payments when payee isnt the client's paypal account.
          // So we must do that on our backend. See this bug: https://github.com/paypal/paypal-checkout/issues/464
          insertBounty(payload);
        }

    }, '#paypal-button-container');
  }

  render() {
    return (
      <div>
        <div id="paypal-button-container"></div>
      </div>
    );
  }
}

PayWithPayPalComponent.propTypes = {
  amount: PropTypes.string.isRequired,
  streamerPaypalEmail: PropTypes.string.isRequired,
  bounty: PropTypes.string.isRequired,
  username: PropTypes.string.isRequired,
  insertBounty: PropTypes.func.isRequired
};

export default PayWithPayPalComponent;
