1) cd to web/mockServer/

2) Generate the certificate (first time only):

openssl req -nodes -new -x509 -keyout server.key -out server.cert

3) Make sure your real backend isn't running

4) Run the mock server

npm run mock-server
