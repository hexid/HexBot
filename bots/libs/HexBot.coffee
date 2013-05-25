### Common variables and functions for bots ###

#require = patchRequire(global.require)
#utils = require('utils')

exports.userAgent = 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1516.3 Safari/537.36'

#argumentData = [{name:'argName1',default:'optionalArg'}, {name:'argName2',csv:true}, ...]
exports.parseArgs = (argumentData, casper) ->
  ARGS = []; missingRequired = []; posArg = -1; argCount = 0

  for arg in argumentData # get the arguments from the command line
    if casper.cli.raw.has arg.name
      ARGS[argCount] = casper.cli.raw.get arg.name
    else if casper.cli.raw.has posArg+1
      ARGS[argCount] = casper.cli.raw.get ++posArg
    else if arg.default? # use the default value if one exists
      ARGS[argCount] = arg.default
    else # if no default value is given then it is a required argument
      missingRequired.push arg.name

    if arg.csv and ARGS[argCount]?
      ARGS[argCount] = ARGS[argCount].split(',')
    argCount++

  if missingRequired.length > 0 # if any required arguments were missing
    casper.echo "argErr: #{missingRequired} are required parameters."
    casper.exit 1
  ARGS # return