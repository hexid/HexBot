#!/bin/env python

from sys import argv, exit, platform
from platform import machine as arch
from os import environ, path, pathsep
import subprocess, getpass

base = path.dirname(path.realpath(__file__))

def getUpdatedPath():
  oper = platform
  casperBin = 'bin'

  if oper.startswith('win32') or oper.startswith('cygwin'):
    oper = 'windows'
  elif oper.startswith('darwin'):
    oper = 'macosx'
  elif oper.startswith('linux'):
    oper = 'linux-' + ('amd64' if arch() == 'x86_64' else 'i386')

  libs = path.join(base, "libs")
  casper = path.join(libs, 'casperjs', 'bin')
  phantom = path.join(libs, "phantomjs")
  phantomBin = path.join(phantom, 'bin')
  phantomBinOS = path.join(phantomBin, oper)

  newPath = environ.copy()
  newPath['PATH'] = pathsep.join([newPath['PATH'], casper, phantomBinOS, phantom, phantomBin])
  return newPath

def getBotFile(botIndex):
  bots = {k:v for k,v in {
    'astral': 'Astral.coffee',
    'bing': 'Bing.coffee',
    'imgur': 'Imgur.coffee',
    'molten': 'Molten.coffee',
    'test-cf': 'Test.coffee',
    'test-js': 'Test.js',
    'xbox': 'Xbox.coffee'
  }.items() if path.isfile(path.join(base, 'bots', v))}

  if len(argv) > botIndex:
    botFile = bots.get(argv[botIndex].lower())
    if botFile != None:
      return botFile
    else:
      print("Bot not found: %s" % argv[botIndex].lower())

  print("Usage:\t<botName> [<botArgs>...]"
        "\n\tpw <botName> [<botArgs>...]"
        "\n\ttest <botName>")
  print("Available bots: %s" % sorted(list(bots.keys())))
  exit()

def firstArgEquals(equals):
  return True if len(argv) > 1 and argv[1].lower() == equals else False

def execute():
  botTest = firstArgEquals('test')
  botPassword = firstArgEquals('pw')
  botIndex = 2 if botTest or botPassword else 1
  botFile = getBotFile(botIndex)

  botArgs = ["casperjs", path.join(base, 'bots', botFile)]
  if botTest:
    botArgs.insert(1, 'test')
  else:
    botArgs.extend(argv[botIndex+1::])

  if botPassword:
    botArgs.append('--password=' + getpass.getpass())

  print('Executing %s' % argv[botIndex])
  newEnv = getUpdatedPath()
  p = subprocess.Popen(botArgs, env=newEnv)
  p.wait()

if __name__=="__main__":
  execute()
