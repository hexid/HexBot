using System;
using System.IO;
using System.Collections.Generic;
using System.Diagnostics;
using System.ComponentModel;
using System.Linq;
using NDesk.Options;
using Hexid.Tools;

namespace Hexid.HexBot {
	class HexBot {
		static string BaseDir = Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location);
		static Dictionary<string,string> Bots = (new Dictionary<string,string>() {
			{"astral", "Astral.coffee"},
			{"bing", "Bing.coffee"},
			{"imgur", "Imgur.coffee"},
			{"molten", "Molten.coffee"},
			{"test-coffee", "Test.coffee"},
			{"test-js", "Test.js"}
		}).Where(b => File.Exists(Path.Combine(BaseDir, "bots", b.Value))).ToDictionary(i => i.Key, i => i.Value);

		static void Main(string[] a) {
			if(Bots.Count == 0) {
				PrintUsage();
				Console.WriteLine("No bots available in: " + Path.Combine(BaseDir, "bots"));
				Environment.Exit(0);
			}

			bool BotTest = false, BotPassword = false;
			OptionSet options = new OptionSet()
				.Add("t|test", t => BotTest = t != null)
				.Add("p|pw", p => BotPassword = p != null);
			List<string> ExtraArgs = options.Parse(a);

			string BotFile = "";
			string BotName = "";
			if(ExtraArgs.Count > 0) {
				BotName = ExtraArgs[0].ToLower();
				if(Bots.TryGetValue(BotName, out BotFile)) {
					Console.WriteLine("Executing {0}", BotName);
				} else {
					Console.WriteLine("Bot not found: {0}", BotName);
				}
				ExtraArgs.RemoveAt(0); // remove BotName
			} else {
				PrintUsage();

				string[] BotNames = Bots.Keys.ToArray();
				Array.Sort(BotNames);
				Console.WriteLine("Available bots: " + String.Join(", ", BotNames));
				Environment.Exit(0);
			}

			List<string> BotArgs = new List<string> {
				"--ignore-ssl-errors=true",
				Path.Combine(BaseDir, "bots", BotFile)
			};
			if(BotTest) BotArgs.Insert(1, "test");
			if(BotPassword) BotArgs.Add("--password=" + Password.GetPassword());
			BotArgs.AddRange(ExtraArgs);

			UpdateEnvironment();

			Console.Title = ("HexBot : " + BotName);
			CreateProcess(BotArgs);
		}

		static void PrintUsage() {
			Console.WriteLine("usage: HexBot.exe [-h] [-p] [-t] botName [botArgs [botArgs ...]]");
			Console.WriteLine("\tpw <botName> [<botArgs>...]");
			Console.WriteLine("\ttest <botName>");
		}

		static void UpdateEnvironment() {
			PlatformID oper = Environment.OSVersion.Platform;
			string os;
			if(oper == PlatformID.Unix) {
				os = "linux-" + (Environment.Is64BitProcess ? "amd64" : "i386");
			} else if(oper == PlatformID.MacOSX) {
				os = "macosx";
			} else {
				os = "windows";
			}

			string libs = Path.Combine(BaseDir, "libs");
			string casper = Path.Combine(Path.Combine(libs, "casperjs"), "bin");
			string phantom = Path.Combine(libs, "phantomjs");
			string phantomBin = Path.Combine(phantom, "bin");
			string phantomBinOS = Path.Combine(phantomBin, os);

			string pathEnv = Environment.GetEnvironmentVariable("PATH");
			string[] paths = new string[] {pathEnv, libs, casper, phantom, phantomBin, phantomBinOS};
			string newPath = String.Join(Path.PathSeparator.ToString(), paths);

			Environment.SetEnvironmentVariable("PATH", newPath);
		}

		static void CreateProcess(List<string> args) {
			ProcessStartInfo psi = new ProcessStartInfo();
			psi.FileName = "casperjs.exe";
			psi.UseShellExecute = false;
			psi.RedirectStandardOutput = true;
			psi.Arguments = String.Join(" ", args);

			try {
				Process p = Process.Start(psi);
				while(!p.StandardOutput.EndOfStream) {
					string line = p.StandardOutput.ReadLine();
					Console.WriteLine(line);
				}
			} catch(Win32Exception e) {
				Console.WriteLine("Fatal: " + e.Message + "; did you install CasperJS?");
			}
		}
	}
}
