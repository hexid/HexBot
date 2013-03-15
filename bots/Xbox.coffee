### Xbox Live Code Redeemer by Hexid
#Arguments# email , password
#Return Codes#
  0 = no errors encountered
  1 = argument error
  2 = error logging in (most likely an incorrect password)
  3 = internet connection error
###
hexBot = require('./libs/HexBot.coffee')
casper = require('casper').create(
  pageSettings:
    loadImages:false
    loadPlugins:false
    userAgent:hexBot.userAgent
)

argData=[{name:'email'},{name:'password'},{name:'code'}]
ARGS = hexBot.parseArgs(argData, casper)

casper.echo 'Logging in...' # give immediate output to the user
casper.start 'http://www.xbox.com/', ->
  if @exists 'a[name="RpsSignInLink"]'
    @click 'a[name="RpsSignInLink"]' # Go to the windows live login page
  else
    @echo 'Connection error occurred.'
    @exit 3 # exit if it isn't the right page

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

casper.waitForSelector('#RedeemResults', getStatus = ->
  @echo @fetchText '#RedeemResults p'
)

casper.run ->
  @echo "Finished redeeming code."
  @exit 0
