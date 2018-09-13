var stompClient = null;
var id = null;

$(document).ready(function(){
    connect();

    var urlParams = new URLSearchParams(window.location.search);
    id = urlParams.get('id');
});

function connect() {
  var socket = new SockJS('https://localhost:8070/alert-websocket');
  stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/alert/' + id, receivedAlert);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

// testing code
function sendAlert() {
  console.log('sending alert...');
  var alert = {
    username: 'Nathan Moeller',
    amount: '500',
    bounty: 'YOU WONT'
  };

  stompClient.send("/app/alertQueue", {}, JSON.stringify(alert));
}

function receivedAlert(alertResponse) {
  const alert = JSON.parse(alertResponse.body);
  showAlert(alert.username, alert.amount, alert.bounty);
}

function showAlert(username, amount, bounty) {
  var alertLength = 5000;
  $('.username').text(username);
  $('.moneyBox').text('$' + amount);
  $('.bounty').text(bounty);

  // show the alert
  $('.alertBox').addClass('opened');

  // close the alert
  setTimeout(function() {
    $('.alertBox').removeClass('opened');
  }, alertLength);
}
