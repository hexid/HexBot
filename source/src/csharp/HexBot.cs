using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.ComponentModel;
using System.Diagnostics;
using System.Security;

class HexBot {
	static string BaseDir = Path.GetFullPath(Path.Combine(System.Reflection.Assembly.GetCallingAssembly().Location, ".."));
	static List<string> args;
	static void Main(string[] a) {
		args = new List<string>(a);
		bool BotTest = FirstArgEquals("test");
		bool BotPassword = FirstArgEquals("pw");
		int BotIndex = (BotTest || BotPassword) ? 1 : 0;
		string BotFile = "";
		try {
			BotFile = GetBotFile(BotIndex);
		} catch(Exception e) {
			Console.WriteLine(e.Message);
			Environment.Exit(1);
		}
		Console.WriteLine("Executing {0}", args[BotIndex]);

		List<string> BotArgs = new List<string> {
			"--ignore-ssl-errors=true",
			Path.Combine(BaseDir, "bots", BotFile)
		};

		args.RemoveRange(0, BotIndex+1);
		if(BotTest)
			BotArgs.Insert(1, "test");
		else
			BotArgs.AddRange(args);

		if(BotPassword) {
			string pw = ReadPassword();
			Console.WriteLine(pw.Length);
			BotArgs.Add("--password=" + pw);
		}

		UpdateEnvironment();

		CreateProcess(BotArgs.ToArray());
	}

	static bool FirstArgEquals(string eq) {
		return (args.Count > 0 && args[0].ToLower().Equals(eq));
	}
	static string GetBotFile(int BotIndex) {
		Dictionary<string,string> Bots = new Dictionary<string,string>() {
			{"astral", "Astral.coffee"},
			{"bing", "Bing.coffee"},
			{"imgur", "Imgur.coffee"},
			{"molten", "Molten.coffee"},
			{"test-coffee", "Test.coffee"},
			{"test-js", "Test.js"}
		};

		List<string> invalid = new List<string>();
		foreach(KeyValuePair<string,string> b in Bots)
			if(!File.Exists(Path.Combine(BaseDir, "bots", b.Value)))
				invalid.Add(b.Key);
		foreach(string i in invalid)
			Bots.Remove(i);

		if(args.Count > BotIndex) {
			string BotFile;
			if(Bots.TryGetValue(args[BotIndex].ToLower(), out BotFile))
				return BotFile;
			else
				Console.WriteLine("Bot not found: {0}", args[BotIndex].ToLower());
		}

		Console.WriteLine("Usage:\t<botName> [<botArgs>...]");
		Console.WriteLine("\tpw <botName> [<botArgs>...]");
		Console.WriteLine("\ttest <botName>");
		string[] BotNames = Bots.Keys.ToArray();
		if(BotNames.Length > 0) {
			Array.Sort(BotNames);
			throw new Exception(String.Format("Available bots: {0}", String.Join(", ", BotNames)));
		} else {
			throw new Exception(String.Format("No bots available: {0}", Path.Combine(BaseDir, "bots")));
		}
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

	static void CreateProcess(string[] args) {
		ProcessStartInfo psi = new ProcessStartInfo();
		psi.FileName = "casperjs.exe";
		psi.UseShellExecute = false;
		psi.RedirectStandardOutput = true;
		psi.Arguments = String.Join(" ", args);

		Console.WriteLine(psi.Arguments);
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
	static string ReadPassword() {
		List<string> pw = new List<string>();
		Console.Write("Password: ");
		ConsoleKeyInfo nextKey = Console.ReadKey(true);
		while(nextKey.Key != ConsoleKey.Enter) {
			if(nextKey.Key == ConsoleKey.Backspace) {
				if(pw.Count > 0) {
					pw.RemoveAt(pw.Count - 1);
				}
			} else {
				pw.Add(nextKey.KeyChar.ToString());
			}
			nextKey = Console.ReadKey(true);
		}
		Console.Write('\n');
		return String.Join("", pw.ToArray());
	}
}