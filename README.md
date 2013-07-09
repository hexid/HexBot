![HexBot](/source/src/main/resources/HexBot.png "HexBot")
===


###Installation:
##### Dependencies
- Install both [PhantomJS](http://phantomjs.org/download.html) and [CasperJS](http://casperjs.org/installation.html)
  - Refer to the individual instructions in the `libs` directory.
  - These can also be installed independently on the system, but the binaries must be included in the PATH environment variable

##### Compile
    cd source/
    mvn jfx:jar
    mv target/jfx/app/HexBot.jar ../HexBot.jar

---

###File Structure
    # Bots (moving these will remove them from the launcher)
    bots/Bing.coffee # Bing Rewards
    bots/Astral.coffee # Astral WoW
    bots/Imgur.coffee # Imgur Albums
    bots/Xbox.coffee # Xbox Codes

    # Modules (moving these will cause the associated bots to fail)
    bots/libs/HexBot.coffee # HexBot
    bots/libs/Microsoft.coffee # Microsoft

    # Libraries (CasperJS/PhantomJS - required to run the bots)
    $PATH/%PATH% # these take precedence over the libs folder
    libs/**

    # Launcher
    HexBot.jar

---

###Usage
    # CLI
    java -jar HexBot.jar botName [botArgs...]

    # GUI
    java -jar HexBot.jar ['gui' [, botName...]

In both cases, leaving out `botName` will print out a list of currently available bots

---

###Requirements
- PhantomJS >= 1.8.1 (>= 1.9.1 for Windows)
- CasperJS >= 1.1.0-DEV (from [GitHub/n1k0/casperjs](http://github.com/n1k0/casperjs))
- Maven (for compiling from source)
- Java (>= 7u6 required for GUI)
- Python (only on Linux and Mac)