# Bounty Streamer 
https://bountystreamer.gg is a free to use (open source) incentivied donations platform built for twitch.tv... Its designed to bring streamers closer to their viewers by building big moments.

Incentivized donations are extremely common on streaming platforms. You'll frequently hear "Do **X** and I'll donate another **Y** dollars". These types of donations are fun! They add stacks to the stream and keep the audience invested. For example, Drake famously told Ninja "I'll give you **5k** is you **get this win**". That was a big moment that had over half a million users watching!

## Can I use this code?
Absolutly! But please contact nckackerman@gmail.com first.

## Questions? Problems? Suggestions?
Please reach out to nckackerman@gmail.com for all feedback about bountystreamer.

## Dev Setup
### Front end

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


### Back end

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


## Architecture Diagram

![Architecture Diagram Image](https://streamer-contracts-public-assets.s3.us-east-2.amazonaws.com/bountyStreamer.png)
