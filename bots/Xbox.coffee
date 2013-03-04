### Xbox Live Code Redeemer by Hexid
#Arguments# email , password
#Return Codes#
  0 = no errors encountered
  1 = argument error
  2 = error logging in (most likely an incorrect password)
  3 = internet connection error
###
hexBot = require('./HexBot.coffee')
casper = require('casper').create(
  pageSettings:
    loadImages:false, loadPlugins:false # don't load images or plugins
    userAgent:hexBot.userAgent
  verbose:false; logLevel:'debug' # output logs at specified level
)

argData=[{c:0,n:'email',d:''},{c:1,n:'password',d:''},
         {c:2,n:'code' ,d:''}]#{count, name, default}#
ARGS = hexBot.parseArgs(argData, casper.cli)
if '' in ARGS[0..2] # if the username or password was not set
  casper.echo 'argErr: username, password, and code cannot be empty'; casper.exit 1

casper.echo 'Logging in...' # give immediate output to the user
casper.start 'http://www.xbox.com/', ->
  if @exists 'a[name="RpsSignInLink"]' then @click 'a[name="RpsSignInLink"]' # Go to the windows live login page
  else @echo 'Connection error occurred.'; @exit 3 # exit if it isn't the right page

casper.thenEvaluate (submitLoginData = (user,pass) ->
  f = document.querySelector 'form[name="f1"]' # login form
  f.querySelector('input[name="login"]').value  = user # username input
  f.querySelector('input[name="passwd"]').value = pass # password input
  f.querySelector('input[name="KMSI"]').checked = true # keep me signed in
  f.querySelector('input[name="SI"]').click() # login button
), ARGS[0], ARGS[1]

casper.thenOpen 'https://live.xbox.com/en-US/RedeemCode'
casper.echo "Entering code: #{ARGS[2]}"
casper.then redeemCode = ->
  @sendKeys 'input.TokenTextBox', ARGS[2]
  @click '#RedeemCode'

casper.then getStatus = ->
  @echo @fetchText '#RedeemResults p'

casper.run ->
  @echo "Finished redeeming code."
  @exit 0
