README

This README describes how to setup alerts for local development.

1. First, install http-server node module globally (so it can be run anywhere). This can be done with the command:

`npm install http-server -g`

more info: https://www.npmjs.com/package/http-server

2. cd to /alert in the project directory

3. Create certificate files to run https. This is only necessary for development because for production it will run on the same s3 bucket.
Below is the command:

`openssl req -newkey rsa:2048 -new -nodes -x509 -days 3650 -keyout key.pem -out cert.pem`

more info: https://stackoverflow.com/questions/35127383/npm-http-server-with-ssl

4. Run the http-server using the command:

`http-server -S -C cert.pem -o`

the alert web server is now running.

5. Assuming the FE and BE are running, navigate to the alert section on /profile, to get the url string. Paste the string into your browser to
see the alert page. Click "Test Alert" to see if it is working.
