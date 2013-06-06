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
    loadImages: false
    loadPlugins: false
    userAgent: hexBot.userAgent
)

argData = [{name:'email'}, {name:'password'}, {name:'code',csv:true}]
ARGS = hexBot.parseArgs(casper, argData)

casper.echo 'Logging in...' # give immediate output to the user
casper.start 'http://www.xbox.com/', ->
  if @exists 'a[name="RpsSignInLink"]'
    @click 'a[name="RpsSignInLink"]' # Go to the windows live login page
  else
    @echo 'Connection error occurred.'
    @exit 3 # exit if it isn't the right page

casper.then submitLoginData = ->
  require('./libs/Microsoft.coffee').login(@, ARGS[0], ARGS[1])

casper.each ARGS[2], enterCodes = (self, code) ->
  self.thenOpen 'https://live.xbox.com/en-US/RedeemCode', ->
    @echo "Entering code: #{code}"
  self.then redeemCode = ->
    @sendKeys 'input.TokenTextBox', code
    @click '#RedeemCode'
  self.then ->
    @waitForSelector('#RedeemResults', getStatus = ->
      @echo @fetchText '#RedeemResults p'
    )

casper.run ->
  @echo "Finished redeeming code#{if ARGS[2].length == 1 then '' else 's'}."
  @exit 0