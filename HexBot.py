#!/bin/env python

from sys import argv, exit, platform, stdout
from platform import machine as arch
from os import environ, path, pathsep
import subprocess, getpass, argparse

base = path.dirname(path.realpath(__file__))
parser = None
bots = {k:v for k,v in {
  'astral': 'Astral.coffee',
  'bing': 'Bing.coffee',
  'imgur': 'Imgur.coffee',
  'molten': 'Molten.coffee',
  'test-coffee': 'Test.coffee',
  'test-js': 'Test.js'
}.items() if path.isfile(path.join(base, 'bots', v))}

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

def getBotFile(botName):
  botFile = bots.get(botName.lower())
  if botFile != None:
    return path.join(base, 'bots', botFile)

  parser.print_help()
  print('Bot not found: %s' % botName.lower())
  exit()

def parseArgs():
  global parser
  parser = argparse.ArgumentParser(
    epilog='Valid bots: %s' % sorted(list(bots.keys()))
  )
  parser.add_argument('botName', metavar='botName', type=str,
    help='Bot name (valid bots below)')
  parser.add_argument('botArgs', type=str, nargs='*',
    help='Arguments that will be passed on to the bot')
  parser.add_argument('-p', '--pw', dest='botPassword', action='store_true',
    help='Prompt for password and sent it on to the bot')
  parser.add_argument('-t', '--test', dest='botTest', action='store_true',
    help='Launch CasperJS in test mode')

  return parser.parse_known_args()

def createProcess():
  (args, unknown) = parseArgs()
  botFile = getBotFile(args.botName)

  stdout.write("\x1b]2;HexBot : " + args.botName.title() + "\x07")
  print('Executing %s' % args.botName.title())

  cmdExec = ['casperjs', '--ignore-ssl-errors=true', botFile]
  if unknown:
    cmdExec.extend(unknown)
  if args.botTest:
    cmdExec.insert(1, 'test')
  cmdExec.extend(args.botArgs)

  if args.botPassword:
    cmdExec.append('--password=' + getpass.getpass())

  updateEnviron()

  p = subprocess.call(' '.join(cmdExec), env=environ, shell=True)

if __name__ == '__main__':
  try:
    if len(bots) is 0:
      print('No bots available in: %s' % path.join(base, 'bots'))
    else:
      createProcess()
  except KeyboardInterrupt:
    print()
    pass
