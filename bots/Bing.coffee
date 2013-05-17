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
    loadImages: false
    loadPlugins: false
    userAgent: hexBot.userAgent
)
DASHBOARD = 'http://www.bing.com/rewards/dashboard'
offers = []; executed = 0; facebook = false

argData = [{name:'email'}, {name:'password'}, {name:'queryCount',default:0},
           {name:'minTime',default:20}, {name:'maxTime',default:40}]
ARGS = hexBot.parseArgs(argData, casper)
ARGS[3] = Math.abs ARGS[3]
ARGS[4] = Math.abs ARGS[4]
if ARGS[4] < ARGS[3] then ARGS[4] = ARGS[3] # ensure that maxTime >= minTime

casper.echo 'Logging in...' # give near-immediate output to the user
casper.start 'http://www.bing.com/rewards/signin', goToLogin = ->
  if not @exists '#WLSignin'
    @echo 'Connection error occurred.'
    @exit 3 # exit if it isn't the right page
  @click '#WLSignin' # Go to the windows live login page

casper.then submitLoginData = ->
  require('./libs/Microsoft.coffee').login(@, ARGS[0], ARGS[1])

casper.thenOpen DASHBOARD, openDashboard = ->
  [offers, ARGS[2], facebook] = @evaluate (examineDashboard = (offers, queryCount, facebook) ->
    for e in document.querySelectorAll 'ul.row li a div.check-wrapper div.open-check'
      elem = e.parentNode.parentNode
      title = elem.querySelector('.title').innerText
      if title is 'Join Now' # failed to login
        return [['Login'], 0] # return fake offer to tell bot to halt
      else if queryCount <= 0 and title in ['Search and Earn', 'Search Bing']
        [earn, per, upTo] = elem.querySelector('.desc').innerText.match(/\d+/g)[0..2]
        soFar = elem.querySelector('.progress').innerText.match(/\d+/)[0]
        # remainingQueries = queriesPerPoint * remainingPoints / earnedPerQueries
        queryCount = Math.ceil(per * (upTo - soFar) / earn)
      else if title is 'Connect to Facebook'
        facebook = true # free points by logging in with facebook
      else if title not in ['Refer a Friend', 'Bing Newsletter']
        offers.push elem.href # add the link of the offer
    [offers, queryCount, facebook] # return
  ), offers, ARGS[2], facebook

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
      @thenOpen 'http://randomword.setgetgo.com/get.php', (randomWordData) ->
        word = ''; gen = false
        # check if the random word was retrieved and if not then generate one
        if not /^[a-zA-Z]{1,20}$/.test (word = @fetchText('body').trim())
          gen = true; CONSONANTS = 'bcdfghjklmnpqrstvwxyz'; VOWELS = 'aeiou'
          len = Math.floor(Math.random() * 5) + 5 # letters in word (5..9)
          for i in [1..len] by 2 # add letters two at a time (consonant followed by a vowel)
            word += CONSONANTS[Math.floor(Math.random() * 21)] # add a consonant
            if i < len # add a vowel if there is room
              word += VOWELS[Math.floor(Math.random() * 5)]
          if Math.floor(Math.random()*2) is 1
            word = word[0].toUpperCase() + word[1..-1] # randomly capitalize word
        # query bing with the word
        @thenOpen "http://www.bing.com/search?scope=web&setmkt=en-US&q=#{word}", (data) ->
          @echo "#{if data['status'] is 200 then ++executed else 'Failed'}#{if gen then '-gen' else ''}) #{word}"

casper.thenOpen DASHBOARD, getTotalPoints = -> # get the number of unused points on the account
  totalPoints = @getElementInfo('#user-status .user-balance .data-value-text').text
  @echo "You currently have #{totalPoints} points on this account."

casper.run ->
  @echo "Completed #{executed}/#{ARGS[2]} quer#{if executed is 1 then 'y' else 'ies'}."
  if facebook # if there was an offer to log in with a facebook account
    @echo 'Earn more points by linking your Facebook account (not done through bot).'
  @exit executed - ARGS[2] # exit with code (-failed)