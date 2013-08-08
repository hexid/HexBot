![HexBot](/source/src/main/resources/HexBot.png "HexBot")
===


###Installation:
##### Dependencies
- Install both [PhantomJS](http://phantomjs.org/download.html) and [CasperJS](http://casperjs.org/installation.html)
  - Refer to the individual instructions in the `libs` directory.
  - These can also be installed independently on the system, but the binaries would need to be included in the PATH environment variable

##### Compile GUI
    cd source/
    mvn jfx:jar
    mv target/jfx/app/HexBot.jar ../HexBot.jar

---

###File Structure
    # Bots (moving these will remove them from the launcher)
    bots/Bing.coffee # Bing Rewards
    bots/Astral.coffee # Astral WoW
    bots/Imgur.coffee # Imgur Albums

    # Modules (moving these will cause the associated bots to fail)
    bots/libs/HexBot.coffee # Parse Arguments/Common Functions

    # Libraries (CasperJS/PhantomJS - required to run the bots)
    $PATH/%PATH% # these take precedence over the libs folder
    libs/**

    # Launchers
    HexBot.jar # GUI
    HexBot.py  # CLI

---

###Usage
##### CLI
    python HexBot.py <botName> [<botArgs>...]

    # Read in a password that will be passed on to the bot
    python HexBot.py pw <botName> [<botArgs>...]

    # Run bot in test mode
    python HexBot.py test <botName>

##### GUI
    # In addition to double-clicking the jar file
    java -jar HexBot.jar [<botName>...]

In both cases, leaving out `botName` will print out a list of currently available bots

---

###Requirements
- PhantomJS >= 1.9.0 (>= 1.9.1 for Windows)
- CasperJS >= 1.1.0-DEV (from [GitHub/n1k0/casperjs](http://github.com/n1k0/casperjs))
- Maven (for compiling from source)
- Java (>= 7u6 required for GUI)
- Python (required for CLI or for GUI on Linux/Mac)