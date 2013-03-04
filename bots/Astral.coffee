### AstralWoW Bot by Hexid
#Arguments# username , password
#Return Codes#
  0 = no errors encountered
  1 = argument error
  2 = error logging in
###
hexBot = require('./HexBot.coffee')
casper = require('casper').create(
  pageSettings:
    loadImages:false, loadPlugins:false # don't load images or plugins
    userAgent:hexBot.userAgent
)

FIRST_LOGIN=true; count=0

argData=[{c:0,n:'username',d:''},{c:1,n:'password',d:''}]#{count, name, default}#
ARGS = hexBot.parseArgs(argData, casper.cli)
if '' in ARGS[0..1] # if the username or password was not set
  casper.echo 'argErr: username and password cannot be empty'; casper.exit 1

login = ->
  casper.thenEvaluate ((user, pass) ->
    document.querySelector('input#ContentPlaceHolderThemeMain_ContentPlaceHolderMain_TBUsername').value = user
    document.querySelector('input#ContentPlaceHolderThemeMain_ContentPlaceHolderMain_TBPassword').value = pass
    document.querySelector('input#ContentPlaceHolderThemeMain_ContentPlaceHolderMain_ButtonLogin').click()
  ), ARGS[0], ARGS[1]
  casper.then ->
    if @getTitle() is 'Login'
      @echo "Error logging in as #{ARGS[0]}."; @exit 2
    @echo "Logged in as #{ARGS[0]}."
    FIRST_LOGIN = false

casper.on 'url.changed', (url) ->
  if not FIRST_LOGIN and url.match '^http://www.astralwow.info/Account/Login.aspx' then login()

casper.echo "Logging in..."
casper.start "http://www.astralwow.info/Account/Login.aspx", -> login()

casper.thenOpen "http://www.astralwow.info/Account/Vote.aspx", ->
  @each @evaluate(->
    votes = []
    panel1 = document.querySelector("#ContentPlaceHolderThemeMain_ContentPlaceHolderMain_LabelVote1").innerText is "You can vote!"
    panel2 = document.querySelector("#ContentPlaceHolderThemeMain_ContentPlaceHolderMain_LabelVote2").innerText is "You can vote!"
    for p in document.querySelectorAll("#contentCenter a[id^='ContentPlaceHolderThemeMain']")
      panel = p.parentNode.innerText.trim()
      if (panel is "Vote Panel #1" and panel1) or (panel is "Vote Panel #2" and panel2)
        votes.push p.href
    return votes
  ), (self, link) ->
    @wait 5000, ->
      @thenOpen link, ->
        @echo link
        count++

casper.run ->
  @echo "#{count} voting site#{if count is 1 then '' else 's'} clicked."; @exit()
