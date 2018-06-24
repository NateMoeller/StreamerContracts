import React, { Component } from 'react';
import axios from 'axios';
import paypal from 'paypal-checkout';

class PayWithPayPalComponent extends Component {
  componentDidMount() {
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
                    //TODO: need to populate this with the paypal Email of the streamer
                    email: 'nckackerman+streamer-business@gmail.com'
                  },
                  amount: {
                    total: '0.01',
                    currency: 'USD'
                  }
                }
              ],
              intent: "authorize"
            }
          });
        },

        // onAuthorize() is called when the buyer approves the payment
        onAuthorize: function(data, actions) {
          const paymentId = data.paymentID;

          // Make a call to the REST api to execute the payment. Unfortunatly 'paypal-checkout' service has a bug
          // that prevents executing payments when payee isnt the client's paypal account.
          // So we must do that on our backend. See this bug: https://github.com/paypal/paypal-checkout/issues/464
          return axios.get('api/donations/execute/' + paymentId).then((response) => {
            console.log(response)
            window.alert('Payment Complete!');
          });
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

export default PayWithPayPalComponent;