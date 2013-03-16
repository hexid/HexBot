### Common variables and functions for bots ###

#require = patchRequire(global.require)
#utils = require('utils')

exports.userAgent = 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.33 Safari/537.31'

#argumentData = [{name:'argName1',default:'default1'}, {name:'argName2'}, ...]
exports.parseArgs = (argumentData, casper) ->
  ARGS=[]; missingRequired=[]; posArg=1; argCount=0

  for arg in argumentData # get the arguments from the command line
    if casper.cli.raw.has arg.name
      ARGS[argCount] = casper.cli.raw.get arg.name # use the option argument
    else if casper.cli.raw.has posArg
      ARGS[argCount] = casper.cli.raw.get posArg # use the positional argument
      posArg++ # move to the next positional argument
    else if arg.default? # use the default value if one exists
      ARGS[argCount] = arg.default
    else # if no default value is given then it is a require argument
      missingRequired.push arg.name
    argCount++

  if missingRequired.length > 0 # if any required arguments were missing
    casper.echo "argErr: #{missingRequired} cannot be empty."
    casper.exit 1
  return ARGS
