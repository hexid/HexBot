### Xbox Live Code Redeemer by Hexid
#Arguments# email , password
#Return Codes#
  0 = no errors encountered
  1 = argument error
  2 = error logging in (most likely an incorrect password)
  3 = internet connection error
###
casper = require('casper').create(
  pageSettings:
    loadImages:false, loadPlugins:false # don't load images or plugins
    userAgent:'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.84 Safari/537.22'
  verbose:true; logLevel:'debug' # output logs at specified level
)
ARGS=[]; cliCount=0
CLI=[{c:0,n:'email',d:''},{c:1,n:'password',d:''},
     {c:2,n:'code' ,d:''}]#{count, name, default}#

for arg in CLI # get the arguments from the command line (unnamed arguments must be in order)
  if casper.cli.has(arg.n) and (typeof casper.cli.raw.get(arg.n)) isnt 'boolean' # if --arg.n=argument and not --arg.n
    ARGS[arg.c] = casper.cli.get arg.n # use the named argument
  else if casper.cli.has cliCount # if passed without argument name
    ARGS[arg.c] = casper.cli.get cliCount; cliCount++ # use the numbered argument
  else ARGS[arg.c] = arg.d # use the default value
if '' in ARGS[0..2] # if the username or password was not set
  casper.echo 'argErr: username, password, and code cannot be empty'; casper.exit 1
#if ARGS[4] < ARGS[3] then ARGS[4] = ARGS[3] # to make sure that maxTime >= minTime

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
casper.then redeemCode = ->
  @sendKeys 'input.TokenTextBox', ARGS[2]
  @click '#RedeemCode'

casper.run ->
  @exit 0
