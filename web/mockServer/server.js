const privateUser = require('./user/privateUser.json');
const publicUser = require('./user/publicUser.json');
const donations = require('./donations/donations.json');
const openBounties = require('./contracts/openContracts.json');
const completedBounties = require('./contracts/compltedContracts.json');
const allBounties = require('./contracts/allContracts.json');
const expiredBounties = require('./contracts/expiredContracts.json');
const acceptedBounties = require('./contracts/acceptedContracts.json');

const express = require('express');
const port = 8070;
const bodyParser = require('body-parser');
const fs = require('fs')
const https = require('https');
const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true,
}));

app.use((req, res, next) => {
  res.header("Access-Control-Allow-Origin", "https://localhost:3000");
  res.header("Access-Control-Allow-Credentials", 'true');
  res.header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

// user endpoints
app.get('/user', (request, response) => {
    response.send(privateUser);
});
app.get('/user/:twitchUsername', (request, response) => {
    response.send(publicUser);
});

// donation endpoints
app.post('/donations', (request, response) => {
  // this endpoint returns a response, but it is current not used on the FE. omitting for now
  response.send();
});
app.get('/donations/listDonations/:page/:pageSize', (request, response) => {
  response.send(donations);
});

// bounty endpoints
app.get('/bounties/listBounties/:page/:pageSize', (request, response) => {
  const query = request.query;
  if (query.state) {
    if (query.state === 'open') {
      response.send(openBounties);
    } else if (query.state === 'completed') {
      response.send(completedBounties);
    } else if (query.state === 'expired') {
      response.send(expiredBounties);
    } else if (query.state === 'accepted') {
      response.send(acceptedBounties);
    }
  }
  response.send(allBounties);
});
app.post('/bounties/accept', (request, response) => {
  response.send();
});
app.delete('/bounties/remove/:contractId', (request, response) => {
  response.send();
});

https.createServer({
  key: fs.readFileSync('./mockServer/server.key'),
  cert: fs.readFileSync('./mockServer/server.cert')
}, app)
.listen(port, function () {
  console.log(`Mock server listening on port ${port}! Go to https://localhost:${port}/`);
})
