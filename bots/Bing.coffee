### Bing Bot by Hexid
#Arguments# email , password [, queryCount=0 [, minTime=20 [, maxTime=40|minTime]]]
#Return Codes#
 <0 = finished with -(failed queries)
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
CONSONANTS='bcdfghjklmnpqrstvwxyz'; VOWELS='aeiou'
DASHBOARD='http://www.bing.com/rewards/dashboard'
offers=[]; executed=0; facebookLogin=false

argData=[{name:'email'},{name:'password'},{name:'queryCount',default:0},
         {name:'minTime',default:20},{name:'maxTime',default:40}]
ARGS = hexBot.parseArgs(argData, casper)
if ARGS[4] < ARGS[3] then ARGS[4] = ARGS[3] # ensure that maxTime >= minTime

casper.echo 'Logging in...' # give near-immediate output to the user
casper.start 'http://www.bing.com/rewards/signin', ->
  if not @exists '#WLSignin'
    @echo 'Connection error occurred.'
    @exit 3 # exit if it isn't the right page
  @click '#WLSignin' # Go to the windows live login page

casper.thenEvaluate (submitLoginData = (user,pass) ->
  f = document.querySelector 'form[name="f1"]' # login form
  f.querySelector('input[name="login"]').value  = user # username input
  f.querySelector('input[name="passwd"]').value = pass # password input
  f.querySelector('input[name="KMSI"]').checked = true # keep me signed in
  f.querySelector('input[name="SI"]').click() # login button
), ARGS[0], ARGS[1]

casper.thenOpen DASHBOARD, getDashboardItems = ->
  [offers,ARGS[2],facebookLogin] = @evaluate (examineDashboard = (offers,queryCount,facebookLogin) ->
    for e in document.querySelectorAll 'ul.row li a div.check-wrapper div.open-check'
      elem = e.parentNode.parentNode 
      title = elem.querySelector('.title').innerText
      if title is 'Join Now' # failed to login
        return [['Login'],0] # return fake offer to tell bot to halt
      else if queryCount <= 0 and title in ['Search and Earn','Search Bing']
        [earn,per,upTo] = elem.querySelector('.desc').innerText.match(/\d+/g)[0..2]
        soFar = elem.querySelector('.progress').innerText.match(/\d+/)[0]
        # remainingQueries = queriesPerPts * remainingPoints / earnedPerQueries
        queryCount = Math.ceil(per * (upTo - soFar) / earn)
      else if title is 'Connect to Facebook'
        facebookLogin = true # free points by logging in with facebook
      else if title not in ['Refer a Friend','Bing Newsletter']
        offers.push elem.href # add the link of the offer
    return [offers,queryCount,facebookLogin]
  ), offers, ARGS[2], facebookLogin

casper.then openDailyOffers = ->
  @each offers, (self,offer) -> # iterate over offers
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
      # visit site to retrieve random word
      @thenOpen 'http://randomword.setgetgo.com/get.php', (randomWordData) ->
        word = ''
        if randomWordData['status'] is 200 # if page loaded successfully
          word = @fetchText('body').trim() # get the random word (without excess whitespace)
        else # generate a random word
          len = Math.floor(Math.random()*5)+5 # letters in word (5..9)
          for i in [1..len] by 2 # add letters two at a time (consonant followed by a vowel)
            word += CONSONANTS[Math.floor(Math.random()*CONSONANTS.length)] # add a consonant
            if i < len # add a vowel if there is room
              word += VOWELS[Math.floor(Math.random()*VOWELS.length)]
          if Math.floor(Math.random()*2) is 1
            word = word[0].toUpperCase() + word[1..-1] # randomly capitalize word
        @thenOpen ("http://www.bing.com/search?scope=web&setmkt=en-US&q="+word), (data) ->
          if data['status'] is 200
            @echo "#{++executed}) #{word}" # query bing with the word
          else
            @echo "Failed) #{word}"

casper.thenOpen DASHBOARD, getTotalPoints = -> # get the number of unused points on the account
  totalPoints = @evaluate -> 
    document.querySelector('#user-status .user-balance .data-value-text').innerText
  @echo "You currently have #{totalPoints} points on this account."

casper.run ->
  @echo "Completed #{executed}/#{ARGS[2]} quer#{if executed is 1 then 'y' else 'ies'}."
  if facebookLogin # if there was an offer to log in with a facebook account
    @echo "Earn more points by linking your Facebook account (not done through bot)."
  @exit executed-ARGS[2] # exit with code (-failed)
