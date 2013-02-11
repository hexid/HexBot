package net.hexid;

import java.util.List;

public class Utils {
	public static String join(String glue, String... s) {
		StringBuilder sb = new StringBuilder();
		int i;
		for(i = 0; i < s.length-1; i++)
			sb.append(s[i] + glue);
		return sb.toString() + s[i];
	}
	public static String join(String glue, List<String> s) {
		return join(glue, s.toArray(new String[0]));
	}
}
