### Bing Bot by Hexid
#Arguments# email , password [, queryCount=0 [, minTime=20 [, maxTime=40|minTime]]]
#Return Codes#
 <0 = finished with -(failed queries)
  0 = no errors encountered
  1 = argument error
  2 = error logging in (most likely an incorrect password)
  3 = internet connection error
###
casper = require('casper').create(
  pageSettings:
    loadImages:false, loadPlugins:false # don't load images or plugins
    userAgent:'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.58 Safari/537.22'
  verbose:false; logLevel:'debug' # output logs at specified level
)
ARGS=[]; DASHBOARD='http://www.bing.com/rewards/dashboard'
cliCount=0; offers=[]; executed=0; facebookLogin=false
CLI=[{c:0,n:'email',     d:''},{c:1,n:'password',d:''},
     {c:2,n:'queryCount',d:0 },#{count, name, default}#
     {c:3,n:'minTime',   d:20},{c:4,n:'maxTime', d:40}]

for arg in CLI # get the arguments from the command line (unnamed arguments must be in order)
  if casper.cli.has(arg.n) and (typeof casper.cli.raw.get(arg.n)) isnt 'boolean' # if --arg.n=argument and not --arg.n
    ARGS[arg.c] = casper.cli.get arg.n # use the named argument
  else if casper.cli.has cliCount # if passed without argument name
    ARGS[arg.c] = casper.cli.get cliCount; cliCount++ # use the numbered argument
  else ARGS[arg.c] = arg.d # use the default value
if '' in ARGS[0..1] # if the username or password was not set
  casper.echo 'argErr: username and password cannot be empty'; casper.exit 1
if ARGS[4] < ARGS[3] then ARGS[4] = ARGS[3] # to make sure that maxTime >= minTime

casper.echo 'Logging in...' # give immediate output to the user
casper.start 'http://www.bing.com/rewards/signin', ->
  if @exists '#WLSignin' then @click '#WLSignin' # Go to the windows live login page
  else @echo 'Connection error occurred.'; @exit 3 # exit if it isn't the right page

casper.thenEvaluate (submitLoginData = (user,pass) ->
  f = document.querySelector 'form[name="f1"]' # login form
  f.querySelector('input[name="login"]').value  = user # username input
  f.querySelector('input[name="passwd"]').value = pass # password input
  f.querySelector('input[name="KMSI"]').checked = true # keep me signed in
  f.querySelector('input[name="SI"]').click() # login button
), ARGS[0], ARGS[1]

casper.thenOpen DASHBOARD, getDashboardItems = ->
  [offers,ARGS[2],facebookLogin] = @evaluate (examineDashboard = (offers,queryCount,facebookLogin) ->
    for e in document.querySelectorAll '.open-check'
      elem = e.parentNode.parentNode; title = elem.querySelector('.title').innerText
      if title is 'Join Now' then return [['Login'],0] # failed to login
      else if queryCount <= 0 and title in ['Search and Earn','Search Bing']
        [earn,per,upTo] = elem.querySelector('.desc').innerText.match(/\d+/g)[0..2]
        soFar = elem.querySelector('.progress').innerText.match(/\d+/)[0]
        queryCount = Math.ceil(per * (upTo - soFar) / earn) # queriesPerPts * remainingPoints / earnedPerQueries
      else if title is 'Connect to Facebook' then facebookLogin = true
      else if title not in ['Refer a Friend','Bing Newsletter']
        offers.push elem.href # add the link of the offer
    return [offers,queryCount,facebookLogin]
  ), offers, ARGS[2], facebookLogin

casper.then openDailyOffers = ->
  @each offers, (self,offer) -> # iterate over offers
    if offer is 'Login'
      @echo "Error logging in as #{ARGS[0]}."; @exit 2 # login failed
    self.thenOpen offer # visit the offer
  @echo "Logged in successfully as #{ARGS[0]}." # tell user that login was successful
  @echo "#{offers.length} daily offer#{if offers.length is 1 then '' else 's'} clicked."

casper.then execQueries = ->
  @echo "Executing #{ARGS[2]} quer#{if ARGS[2] is 1 then 'y' else 'ies'}." # tell user how many queries are being executed
  @repeat ARGS[2], execQuery = -> # repeat `queryCount` times
    @wait Math.floor(Math.random() * ((ARGS[4] - ARGS[3]) * 1000 + 1)) + (ARGS[3] * 1000), -> # wait between queries
      @thenOpen 'http://randomword.setgetgo.com/get.php', execQuery = -> # visit site to retrieve random word
        if not @exists 'body > *' # if body does not have any children (check to make sure the site is correct)
          word = @fetchText('body').trim() # get the random word (without excess whitespace)
          @thenOpen ("http://www.bing.com/search?q=#{word}"), -> @echo "#{++executed}) #{word}" # query bing with the word

casper.thenOpen DASHBOARD, getTotalPoints = -> # get the number of unused points on the account
  totalPoints = @evaluate -> document.querySelector('#user-status .user-balance .data-value-text').innerText
  @echo "You currently have #{totalPoints} points on this account."

casper.run ->
  @echo "Completed #{executed}/#{ARGS[2]} quer#{if executed is 1 then 'y' else 'ies'}."
  if facebookLogin then @echo "Earn more points by linking your Facebook account."
  @exit executed-ARGS[2] # exit with code (-failed)
