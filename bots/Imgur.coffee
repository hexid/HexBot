### Imgur Album Downloader by Hexid
#Arguments# album , output
#Return Codes#
 <0 = finished with -(failed queries)
  0 = no errors encountered
  1 = argument error
  2 = error logging in (most likely an incorrect password)
  3 = internet connection error
###
hexBot = require('./HexBot.coffee')
casper = require('casper').create(
  pageSettings:
    loadImages:false, loadPlugins:false # don't load images or plugins
    webSecurityEnabled:false # disable xss so that images can be downloaded without changing urls
    userAgent:hexBot.userAgent
); fs = require('fs')
sep = require('fs').separator
imgs=[]; albumDesc=''; count=0

argData=[{c:0,n:'album',d:''},{c:1,n:'output',d:''}]
ARGS = hexBot.parseArgs(argData, casper.cli)
if '' in ARGS[0..1] # if the album or output is not set
  casper.echo 'argErr: album and output cannot be empty'; casper.exit 1

if not ARGS[1].match(new RegExp(".+\\#{sep}$"))
  ARGS[1] += sep # add the path separator if it is missing from the end

matches = ARGS[0].match /^(?:(?:(?:(?:(?:(?:https?:\/\/)?(?:www\.)?imgur\.com)?\/)?a)?\/)?(\w+)(?:[\/?#](?:.+)?)?)$/i
if matches is null
  casper.echo "argErr: album id can't be found in `#{ARGS[0]}`"; casper.exit 1
ARGS[0] = matches[1]
folder = "#{ARGS[1]}#{ARGS[0]}#{sep}"

casper.echo "Fetching album #{ARGS[0]} from Imgur."
casper.start "http://imgur.com/a/#{ARGS[0]}/layout/blog", (data) ->
  if data['status'] is 200
    @reload -> # reload the page to ensure that the page is in the blog layout
      [imgs, albumDesc] = @evaluate ->
        images = []
        for img in document.querySelectorAll('.image')
          images.push(
            link: img.querySelector('.album-view-image-link a').href # link to image
            desc: img.querySelector('.description')?.innerText # description if it exists
          )
        return [images, document.querySelector('.panel .description h1').innerText];
      @echo "Found #{imgs.length} images in the album."
  else
    @echo 'Album either empty or not found.'; @exit 2

casper.then ->
  infoFile = "#{folder}0 - #{ARGS[0]}.txt"
  @echo 'Downloading album...'
  fs.write infoFile, "#{albumDesc}\n\n", 'w' # override old file (if exists)
  @each imgs, (self,img) ->
    name = img.link.match /[\w_.-]*?(?=\?)|[\w_.-]*$/ # find the file name from the link
    self.download img.link, "#{folder}#{++count} - #{name}", 'GET' # download the image
    if img.desc then fs.write infoFile, "#{count}_#{name}) #{img.desc}\n", 'a' # output the image description
    self.echo "   #{count} - #{name}" # tell the user about the image

casper.run ->
  @echo "Finished downloading album (#{count} images)."; @exit 0
