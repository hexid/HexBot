require = patchRequire(global.require)
markov = require('./libs/Words/_markov.json')

exports.generateWord = () ->
  getRandom = (max, min = 0) ->
    (Math.random() * (max - min)) + min

  degree = markov['degree']
  length = Math.floor(getRandom(9, 5))
  alphabet = 'abcdefghijklmnopqrstuvwxyz'
  chars = []
  word = ""
  for i in [0...degree-1]
    chars.push(' ')

  for i in [0..length]
    nextList = markov
    for i in [0...degree-1]
      if nextList && nextList.hasOwnProperty(chars[i])
        nextList = nextList[chars[i]]
      else
        break

    if nextList && nextList.hasOwnProperty('_')
      randNext = Math.floor(getRandom(nextList['_']))
      sum = 0
      for i in alphabet
        if nextList[i]
          sum += nextList[i]
          if randNext <= sum
            chars.shift()
            chars.push(i)
            word += i
            break
    else
      break

  word
