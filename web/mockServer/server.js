const privateUser = require('./user/privateUser.json');
const publicUser = require('./user/publicUser.json');
const openDonations = require('./donations/openDonations.json');

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
app.get('/donations/listOpenDonations/:page/:pageSize', (request, response) => {
  response.send(openDonations);
});

https.createServer({
  key: fs.readFileSync('./mockServer/server.key'),
  cert: fs.readFileSync('./mockServer/server.cert')
}, app)
.listen(port, function () {
  console.log(`Example app listening on port ${port}! Go to https://localhost:${port}/`);
})
