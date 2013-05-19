HexBot Changelog
==========

###v1.0.3
- Create module that will hold variables/functions useful to most/all bots
  - `userAgent`
  - `parseArgs(argData, casper)`
- Released beta version of Xbox Code Redeemer
  - At current stage, there is no output on success
- Fix spacing and refactor code in bots
- Require at least CasperJS 1.1.0-DEV
- Upgrade to PhantomJS 1.9
- Added a test bot for use with helping with StackOverflow/Github questions

###v1.0.2
- Bing GUI can now has a button to stop itself mid-execution
- Updated Bing Bot to generate a random word when it can't be retrieved online
- Fixed Maven build script (updated javafx-maven-plugin to 1.5)
- Updated Imgur Bot to match album id from more URL variants

###v1.0.1
- Passing `gui` to the jar is no longer case-sensitive
- Moved library READMEs to be in `lib/` instead of their respective folders
- Imgur CLI can now accept a single argument
- GUI loads when no arguments are passed to the jar

###v1.0.0
- Initial release.
- Includes bots:
  - Bing Rewards
  - Astral WoW
  - Imgur Albums
- Each of these bots has a java graphical/command-line interface
