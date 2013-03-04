### Common variables and functions for bots ###

exports.userAgent = 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.19 Safari/537.31'

exports.parseArgs = (argumentData, cli) ->
  ARGS=[]; cliCount=0
  for arg in argumentData # get the arguments from the command line (unnamed arguments must be in order)
    if cli.has(arg.n) and (typeof cli.raw.get(arg.n)) isnt 'boolean' # if --arg.n=argument and not --arg.n
      ARGS[arg.c] = cli.get arg.n # use the named argument
    else if cli.has cliCount # if passed without argument name
      ARGS[arg.c] = cli.get cliCount; cliCount++ # use the numbered argument
    else ARGS[arg.c] = arg.d # use the default value
  return ARGS
