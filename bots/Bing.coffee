### Bing Bot by Hexid
#Arguments# email , password [, queryCount=0 [, minTime=20 [, maxTime=40|minTime]]]
#Return Codes#
 <0 = finished with -(failed queries)
  0 = no errors encountered
  1 = argument error
  2 = error logging in (most likely an incorrect password)
  3 = internet connection error
###
hexBot = require('libs/HexBot')
words = require('libs/Words/GenerateWord')
casper = hexBot.createCasper()
DASHBOARD = 'http://www.bing.com/rewards/dashboard'
offers = []; executed = 0; fb = false

argData = [{name:'email'}, {name:'password'}, {name:'queryCount',default:0},
           {name:'minTime',default:20}, {name:'maxTime',default:40}]
argStr = 'email , password , [queryCount , [minTime , [maxTime]]]]'
ARGS = hexBot.parseArgs(casper, argData, argStr)
ARGS[3] = Math.abs ARGS[3]
ARGS[4] = Math.abs ARGS[4]
if ARGS[4] < ARGS[3] then ARGS[4] = ARGS[3] # ensure that maxTime >= minTime

casper.echo 'Logging in...' # give near-immediate output to the user
casper.start 'http://www.bing.com/rewards/signin', goToLogin = ->
  if not @exists '#WLSignin'
    @echo 'Connection error occurred.'
    @exit 3 # exit if it isn't the right page
  @click '#WLSignin' # Go to the windows live login page

casper.then login = ->
  url = @getCurrentUrl()
  @fill 'form[name="f1"]',
    login: ARGS[0]
    passwd: ARGS[1]
    KMSI: true
  , true
  @waitFor urlChange = ->
    return @getCurrentUrl() != url

casper.thenOpen DASHBOARD, openDashboard = ->
  [offers, ARGS[2], fb] = @evaluate (examineDashboard = (offers, queryCount, fb) ->
    for e in document.querySelectorAll 'ul.row li a div.check-wrapper div.open-check'
      elem = e.parentNode.parentNode
      title = elem.querySelector('.title').innerText.toLowerCase()
      if title is 'join now' # failed to login
        return [['Login'], 0] # return fake offer to tell bot to halt
      else if queryCount <= 0 and title in ['search and earn', 'search bing']
        [earn, per, upTo] = elem.querySelector('.desc').innerText.match(/\d+/g)[0..2]
        soFar = elem.querySelector('.progress').innerText.match(/\d+/)[0]
        # remainingQueries = queriesPerPoint * remainingPoints / earnedPerQueries
        queryCount = Math.ceil(per * (upTo - soFar) / earn)
      else if title is 'connect to facebook'
        fb = true # free points by logging in with facebook
      else if title not in ['refer a friend', 'bing newsletter', 'invite friends']
        offers.push elem.href # add the link of the offer
    [offers, queryCount, fb] # return
  ), offers, ARGS[2], fb

casper.then openDailyOffers = ->
  @each offers, (self, offer) -> # iterate over offers
    if offer is 'Login'
      @echo "Error logging in as #{ARGS[0]}."
      @exit 2 # login failed
    self.thenOpen offer # visit the offer
  @echo "Logged in successfully as #{ARGS[0]}." # tell user that login was successful
  @echo "#{offers.length} daily offer#{if offers.length is 1 then '' else 's'} clicked."

casper.then execQueries = ->
  # tell user how many queries are being executed
  @echo "Executing #{ARGS[2]} quer#{if ARGS[2] is 1 then 'y' else 'ies'}."
  @repeat ARGS[2], execQuery = -> # repeat `queryCount` times
    # wait between queries
    @wait Math.floor(Math.random() * ((ARGS[4] - ARGS[3]) * 1000 + 1)) + (ARGS[3] * 1000), ->
      word = words.generateWord()
      # query bing with the word
      @thenOpen "http://www.bing.com/search?scope=web&setmkt=en-US&q=#{word}", (data) ->
        @echo "#{if data['status'] is 200 then ++executed else 'Failed'}) #{word}"

percentToGoal = ''
casper.thenOpen DASHBOARD, checkForPoints = ->
  percentToGoal = @evaluate ->
    return document.querySelector('.progress-value').children[0].innerText
casper.then ->
  if not (percentToGoal in ['100%'])
    @echo "You are #{percentToGoal} of the way to your goal."
    @bypass 4 # skip the next (3?) steps since there aren't enough points for the goal

casper.thenOpen "http://www.bing.com/rewards/redeem/all", ->
  @click "#goal" # visit the goal reward's page
casper.then ->
  @waitForSelector '#SingleProduct_SubmitForm', ->
    @click '#SingleProduct_SubmitForm' # click the redeem button
casper.then ->
  @waitForSelector '#CheckoutReview_SubmitForm', ->
    @click '#CheckoutReview_SubmitForm' # confirm order
    @echo 'Redeemed your goal.'

casper.thenOpen DASHBOARD, getTotalPoints = -> # get the number of unused points on the account
  totalPoints = @getElementInfo('#user-status .user-balance .data-value-text').text
  @echo "You currently have #{totalPoints} points on this account."

casper.run ->
  @echo "Completed #{executed}/#{ARGS[2]} quer#{if executed is 1 then 'y' else 'ies'}."
  if fb # if there was an offer to log in with a facebook account
    @echo 'Earn more points by linking your Facebook account (not done through bot).'
  @exit executed - ARGS[2] # exit with code (-failed)
