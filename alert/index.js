var stompClient = null;
var id = null;

$(document).ready(function(){
  var urlParams = new URLSearchParams(window.location.search);
  id = urlParams.get('id');

  if (id) {
    connect();
  } else {
    console.error('No alert id specified.');
  }
});

function connect() {
  var socket = new SockJS(REACT_APP_API_HOST + 'alert-websocket');
  stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/alert/' + id, receivedAlert);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
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

  $('.alertBox').addClass('visible');

  setTimeout(function() {
    $('.alertBox').removeClass('visible');
  }, alertLength);
}
