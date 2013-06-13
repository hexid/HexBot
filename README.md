![HexBot](/source/src/main/resources/HexBot.png "HexBot") v1.0.3
==========


###Installation:
##### Dependencies
- Install both [PhantomJS](http://phantomjs.org/download.html) and [CasperJS](http://casperjs.org/installation.html)
  - Refer to the individual instructions in the `libs` directory.

##### Compile
- Inside the `source` directory, run `mvn jfx:build-jar` and copy the jar file from the `source/target` directory to the base directory

###Current Bots Include:
- Bing Rewards (bots/Bing.coffee)
- Astral WoW (bots/Astral.coffee)
- Imgur Albums (bots/Imgur.coffee)
- Xbox Codes (bots/Xbox.coffee)

###Current Modules Include:
- HexBot (bots/libs/HexBot.coffee)
- Microsoft (bots/libs/Microsoft.coffee)

###Usage:
- CLI
  - `java -jar HexBot.jar botName [botArgs...]`
- GUI
  - Double-clicking the `HexBot.jar` file, or
  - `java -jar HexBot.jar ['gui' [, botName...]`
- In both cases, leaving out `botName` will print out a list of currently available bots
  - (i.e. bots that have corresponding files in the `bots` directory)

###Requirements:
- PhantomJS >= 1.8.1 (>= 1.9.1 for Windows)
- CasperJS >= 1.1.0-DEV (from [GitHub/n1k0/casperjs](http://github.com/n1k0/casperjs))
- Maven (for compiling from source)
- Java (>= 7u6)
