HexBot
==========

###Installation:
* Download necessary libraries and place them in their respective folders in the `libs` directory
  * [CasperJS](http://casperjs.org/)
  * [PhantomJS](http://phantomjs.org/download.html)
    * When building from source, copy the phantomjs file from the bin directory into the `libs\phantomjs` directory

* Compile:
  * Inside the source directory, run `mvn jfx:build-jar` and copy the jar file from the `source\target` directory to the base directory

###Current Bots Include:
* Bing
* Astral WoW
* Imgur Albums

###Usage:
* CLI
  * `java -jar HexBot.jar botName [botArgs]`
* GUI
  * `java -jar HexBot.jar ['gui' , botName]`
* In both cases, leaving out `botName` will print out a list of currently available bots
  * (i.e. bots that have corresponding files in the `bots` directory)
