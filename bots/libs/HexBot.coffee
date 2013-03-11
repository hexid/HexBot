### Common variables and functions for bots ###

#require = patchRequire(global.require)
#casper = require('casper')

exports.userAgent = 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.19 Safari/537.31'

#argumentData = [{c:0,n:'argName1',d:'default1'}, {c:1,n:'argName2',d:'default2'}, ...]
exports.parseArgs = (argumentData, casper) ->
  ARGS=[]; cliCount=0
  # get the arguments from the command line (unnamed arguments must be in order)
  for arg in argumentData
    # if --arg.n=argument and not --arg.n
    if casper.cli.has(arg.n) and (typeof casper.cli.raw.get(arg.n)) isnt 'boolean'
      ARGS[arg.c] = casper.cli.get arg.n # use the named argument
    else if casper.cli.has cliCount # if passed without argument name
      ARGS[arg.c] = casper.cli.get cliCount # use the numbered argument
      cliCount++ # move to the next unnamed argument
    else # use the default value
      ARGS[arg.c] = arg.d
  return ARGS

exports.parseArgsWithErrorMsg = (argumentData, casper, firstRequired, lastRequired, errorText) ->
  ARGS = exports.parseArgs(argumentData, casper)
  if '' in ARGS[firstRequired..lastRequired]
    casper.echo "argErr: #{errorText} cannot be empty"
    casper.exit 1
  return ARGS
