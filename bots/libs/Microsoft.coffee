### Common variables and functions for Microsoft bots ###

exports.login = (casper, username, password) ->
  casper.fill 'form[name="f1"]',
    login: username
    passwd: password
    KMSI: true
  , true
  casper.wait 100

exports._login = (casper, username, password) ->
  casper.thenEvaluate (submitLoginData = (user,pass) ->
    f = document.querySelector 'form[name="f1"]' # login form
    f.querySelector('input[name="login"]').value = user # username input
    f.querySelector('input[name="passwd"]').value = pass # password input
    f.querySelector('input[name="KMSI"]').checked = true # keep me signed in
    f.querySelector('input[name="SI"]').click() # login button
  ), username, password

exports.__login = (casper, username, password) ->
  casper.sendKeys 'input[name="login"]', username
  casper.sendKeys 'input[name="passwd"]', password
  casper.click 'input[name="KMSI"]'
  casper.click 'input[name="SI"]'
  casper.wait 100