Architecture Diagram

![Architecture Diagram Image](https://s3.amazonaws.com/bountystreamer.gg/bountyStreamer.png)

Setup Instructions

1) Clone repo

Front end:

1) Run `npm install`
2) Run `npm run start`

3) (Optional) Run mock server: `npm run mock-server`

4) (Optional) Atom packages:
	-atom-ternjs
	-autocomplete-modules
	-busy-signal
	-editorconfig
	-es6-javascript
	-file-icons
	-intentions
	-javascript-snippets
	-language-babel
	-linter
	-linter-eslint
	-linter-ui-default


Back end:

1) Install lombok plugin to IntellIJ (Settings -> Plugins). Enable annotation processing (Settings -> build -> compiler -> annotation processors) 
2) Set up postgres database (see README in dev_ops/sql)
3) Intall/Run Redis: https://stackoverflow.com/questions/6476945/how-do-i-run-redis-on-windows/10525215?fbclid=IwAR0X6NdMCyziBaOP15VSoZatOsOgO2sDYp_eq8RxuYeDOKbBUeHLlWAcTgU
   To run (Services -> Right click on "Redis Server" -> Start)
4) Setup aws credentials: (https://aws.amazon.com/cli/?fbclid=IwAR2XLH5NU50tey7IJ7oDH8mAE7rTS0PqNyw7ORmiGKeg3NOtnmn2WDM0omw)
	`aws configure`
	accessKey ->
	secretKey -> 
	region -> us-east-1
	
	Then add `-Dspring.profiles.active=dev` to VM options in IntellIJ. (Run -> Edit Configurations)
5) Setup keystore env variable. This file is available on aws s3. Environment variable should be:
	`STREAMER_CONTRACTS_KEY_STORE_URL="<path>"`

