using System;
using System.IO;
using System.Collections.Generic;
using System.Security;

namespace Hexid.Tools {
	class Password {
		public static string GetPassword() {
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
}
