### Imgur Album Downloader by Hexid
#Arguments# album , output
#Return Codes#
  0 = no errors encountered
  1 = argument error
  2 = error logging in (most likely an incorrect password)
  3 = internet connection error
###
hexBot = require('./libs/HexBot.coffee')
fs = require('fs')
casper = require('casper').create(
  pageSettings:
    loadImages: false
    loadPlugins: false
    userAgent: hexBot.userAgent
    webSecurityEnabled: false # disable xss so that images can be downloaded without changing urls
)
imgs = []; albumDesc = ''; count = 0

argData = [{name:'album'}, {name:'output'}]
ARGS = hexBot.parseArgs(argData, casper)

if not ARGS[1].match(new RegExp(".+\\#{fs.separator}$"))
  ARGS[1] += fs.separator # add the path separator if it is missing from the end

matches = ARGS[0].match /^(?:(?:(?:(?:(?:(?:https?:\/\/)?(?:www\.)?imgur\.com)?\/)?a)?\/)?(\w+)(?:[\/?#](?:.+)?)?)$/i
if matches is null
  casper.echo "argErr: album id can't be found in `#{ARGS[0]}`"
  casper.exit 1
ARGS[0] = matches[1]
folder = "#{ARGS[1]}#{ARGS[0]}#{fs.separator}"

casper.echo "Fetching album #{ARGS[0]} from Imgur."
casper.start "http://imgur.com/a/#{ARGS[0]}/layout/blog", (data) ->
  if data['status'] is 200
    @reload -> # reload the page to ensure that the page is in the blog layout
      [imgs, albumDesc] = @evaluate ->
        images = []
        for img in document.querySelectorAll('.image')
          images.push(
            link: img.querySelector('.album-view-image-link a').href # link to image
            desc: img.querySelector('h2')?.innerText # description if it exists
          )
        return [images, document.querySelector('.panel .description h1').innerText]
      @echo "Found #{imgs.length} images in the album."
  else
    @echo 'Album either empty or not found.'
    @exit 2

casper.then ->
  infoFile = "#{folder}0 - #{ARGS[0]}.txt"
  @echo 'Downloading album...'
  fs.write infoFile, "#{albumDesc}\n\n", 'w' # override old file (if exists)
  @each imgs, (self,img) ->
    name = img.link.match /[\w_.-]*?(?=\?)|[\w_.-]*$/ # find the file name from the link
    self.download img.link, "#{folder}#{++count} - #{name}", 'GET' # download the image
    if img.desc
      fs.write infoFile, "#{count} - #{name}) #{img.desc}\n", 'a' # append the image description
    self.echo "   #{count} - #{name}" # tell the user about the image

casper.run ->
  @echo "Finished downloading album (#{count} images)."
  @exit 0