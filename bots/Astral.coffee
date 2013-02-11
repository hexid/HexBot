### AstralWoW Bot by Hexid
#Arguments# username , password
#Return Codes#
  0 = no errors encountered
  1 = argument error
  2 = error logging in
###
casper = require('casper').create(
  verbose:false; logLevel:'debug'
  pageSettings:
    loadImages:false, loadPlugins:false # don't load images or plugins (flash, silverlight, etc.)
    userAgent:'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.58 Safari/537.22'
)

ARGS=[]; cliCount=0; FIRST_LOGIN=true; count=0
CLI=[{c:0,n:'username',d:''},{c:1,n:'password',d:''}]#{count, name, default}#
for arg in CLI # get the arguments from the command line (unnamed arguments must be in order)
  if casper.cli.has(arg.n) and (typeof casper.cli.raw.get(arg.n)) isnt 'boolean' # if --arg.n=argument not --arg.n
    ARGS[arg.c] = casper.cli.get arg.n # use the named argument
  else if casper.cli.has cliCount # if passed without argument name
    ARGS[arg.c] = casper.cli.get cliCount; cliCount++ # use the numbered argument
  else ARGS[arg.c] = arg.d # use the default value
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
