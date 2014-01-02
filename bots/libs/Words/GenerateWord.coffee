require = patchRequire(global.require)
chain = require('./libs/Words/_markov.json')

depth   = chain['depth']
numChar = '#' # holds the total number of words for that sequence
endChar = '.' # holds the number of words that end with that sequence
exports.generateWord = ->
  chars = (' ' for i in [0...depth-1])
  word  = ''

  while true
    nextList = chain
    for i in [0...depth-1]
      (nextList = nextList[chars[i]]) if chars[i] of nextList

    return word if numChar not of nextList

    randNext = Math.floor(Math.random() * nextList[numChar])
    sum = 0
    for letter, letterCount of nextList
      continue if letter == numChar
      sum += letterCount
      if randNext <= sum
        return word if letter == endChar
        chars.shift()
        chars.push(letter)
        word += letter
        break
