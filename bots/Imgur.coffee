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
casper = hexBot.createCasper(
  pageSettings:
    webSecurityEnabled: false # disable xss so that images can be downloaded without changing urls
)

argData = [{name:'album',csv:true}, {name:'output'}]
ARGS = hexBot.parseArgs(casper, argData)

if not ARGS[1].match(new RegExp(".+\\#{fs.separator}$"))
  ARGS[1] += fs.separator # add the path separator if it is missing from the end

casper.start()

for i in [0...ARGS[0].length] by 1
  matches = ARGS[0][i].match /^(?:(?:(?:(?:(?:(?:https?:\/\/)?(?:www\.)?imgur\.com)?\/)?a)?\/)?(\w+)(?:[\/?#](?:.+)?)?)$/i
  if matches is null
    casper.echo "argErr: album id can't be found in `#{ARGS[1]}`"
    casper.exit 1
  ARGS[0][i] = matches[1]


casper.each ARGS[0], searchAlbum = (self, albumId) ->
  self.then ->
    @echo "#{albumId}) Fetching album from Imgur."

    imgs = []; count = 0; albumDesc = ''
    folder = "#{ARGS[1]}#{albumId}#{fs.separator}"

    @thenOpen "http://imgur.com/a/#{albumId}/layout/blog", (data) ->
      if data['status'] is 200
        @reload -> # reload the page to ensure that the page is in the blog layout
          [imgs, albumDesc] = @evaluate ->
            images = []
            for img in document.querySelectorAll('.image')
              images.push(
                link: img.querySelector('.album-view-image-link a').href # link to image
                desc: img.querySelector('h2')?.innerText # description if it exists
              )
            [images, document.querySelector('.panel .description h1').innerText] #return
          @echo "#{albumId}) Found #{imgs.length} images in the album."
      else
        @echo "#{albumId}) Album either empty or not found."
        @exit 2

    @then ->
      infoFile = "#{folder}0 - #{albumId}.txt"
      @echo "#{albumId}) Downloading album..."
      fs.write infoFile, "#{albumDesc}\n\n", 'w' # override old file (if exists)
      @each imgs, (self,img) ->
        name = img.link.match /[\w_.-]*?(?=\?)|[\w_.-]*$/ # find the file name from the link
        self.download img.link, "#{folder}#{++count} - #{name}", 'GET' # download the image
        if img.desc
          fs.write infoFile, "#{count} - #{name}) #{img.desc}\n", 'a' # append the image description
        self.echo "   #{count} - #{name}" # tell the user about the image
      @echo "#{albumId}) Finished downloading album (#{count} images)."

casper.run()