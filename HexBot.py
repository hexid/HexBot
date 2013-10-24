#!/bin/env python

from sys import argv, exit, platform
from platform import machine as arch
from os import environ, path, pathsep
import subprocess, getpass

base = path.dirname(path.realpath(__file__))

def updateEnviron():
  oper = platform
  if oper.startswith('win32'):
    oper = 'windows'
  elif oper.startswith('darwin'):
    oper = 'macosx'
  elif oper.startswith('linux'):
    oper = 'linux-' + ('amd64' if arch() == 'x86_64' else 'i386')

  libs = path.join(base, 'libs')
  casper = path.join(libs, 'casperjs', ('bin' if oper is not 'windows' else 'batchbin'))
  phantom = path.join(libs, 'phantomjs')
  phantomBin = path.join(phantom, 'bin')

  if oper.startswith('cygwin'):
    oper = 'windows'

  phantomBinOS = path.join(phantomBin, oper)

  environ['PATH'] += pathsep + pathsep.join([casper, phantomBinOS, phantom, phantomBin])

def getBotFile(botIndex):
  bots = {k:v for k,v in {
    'astral': 'Astral.coffee',
    'bing': 'Bing.coffee',
    'imgur': 'Imgur.coffee',
    'molten': 'Molten.coffee',
    'test-coffee': 'Test.coffee',
    'test-js': 'Test.js'
  }.items() if path.isfile(path.join(base, 'bots', v))}

  if len(argv) > botIndex:
    botFile = bots.get(argv[botIndex].lower())
    if botFile != None:
      return botFile
    else:
      print('Bot not found: %s' % argv[botIndex].lower())

  print('Usage:\t<botName> [<botArgs>...]'
        '\n\tpw <botName> [<botArgs>...]'
        '\n\ttest <botName>')
  if len(bots) > 0:
    print('Available bots: %s' % sorted(list(bots.keys())))
  else:
    print('No bots available: %s' % path.join(base, 'bots'))
  exit()

def firstArgEquals(equals):
  return True if len(argv) > 1 and argv[1].lower() == equals else False

def createProcess():
  botTest = firstArgEquals('test')
  botPassword = firstArgEquals('pw')
  botIndex = 2 if botTest or botPassword else 1
  botFile = getBotFile(botIndex)
  print('Executing %s' % argv[botIndex])

  botArgs = ['casperjs', '--ignore-ssl-errors=true', path.join(base, 'bots', botFile)]
  if botTest:
    botArgs.insert(1, 'test')
  else:
    botArgs.extend(argv[botIndex+1::])

  if botPassword:
    botArgs.append('--password=' + getpass.getpass())

  updateEnviron()

  p = subprocess.call(' '.join(botArgs), env=environ, shell=True)

if __name__ == '__main__':
  try:
    createProcess()
  except KeyboardInterrupt:
    pass