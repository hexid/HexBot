package net.hexid.hexbot.bot;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import net.hexid.Utils;

public class Bots {
	private static TreeMap<String, HashMap<String, String>> bots = new TreeMap();
	public static void addBot(String shortName, final String longName, 
			final String cliClassPath, final String guiClassPath, final String fileName) {
		bots.put(shortName.toLowerCase(), new HashMap<String, String>() {{
			put("longName", longName);
			put("cliClassPath", cliClassPath);
			put("guiClassPath", guiClassPath);
			put("fileName", fileName);
		}});
	}

	public static void removeInvalidBots() {
		Iterator<Entry<String, HashMap<String, String>>> iter = bots.entrySet().iterator();
		while(iter.hasNext()) {
			if(!(new File(getBotFile(iter.next().getKey()))).exists())
				iter.remove();
		}
	}

	public static TreeMap<String, HashMap<String, String>> getBots() {
		return bots; // get the map of botNames and their map of data (in chronological order)
	}
	public static String[] botNames() {
		return bots.keySet().toArray(new String[0]);
	}
	
	public static HashMap<String, String> getBot(String botName) {
		return bots.get(botName.toLowerCase()); // get the data associated with a bot
	}
	public static boolean hasBot(String botName) {
		return bots.containsKey(botName.toLowerCase());
	}
	
	public static String getBotLongName(String botName) {
		return getFromBot(botName, "longName");
	}
	public static String getBotCliClassPath(String botName) {
		return getFromBot(botName, "cliClassPath");
	}
	public static String getBotGuiClassPath(String botName) {
		return getFromBot(botName, "guiClassPath");
	}
	public static String getBotFileName(String botName) {
		return getFromBot(botName, "fileName");
	}
	private static String getFromBot(String botName, String value) {
		// if the bot exists then get the value from that bot, otherwise null
		return (hasBot(botName)) ? getBot(botName).get(value) : null;
	}

	public static File getJarDir() {
		return new File(Bots.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
	}

	public static String getBotEnvPath(String oldEnvPath) {
		String dir = getJarDir().getPath();
		String phantom = Utils.join(File.separator, dir, "libs", "phantomjs", "bin");
		String phantomBin = Utils.join(File.separator, dir, "libs", "phantomjs");
		String casper = Utils.join(File.separator, dir, "libs", "casperjs", 
				(File.pathSeparator.equals(":") ? "bin" : "batchbin"));
		return Utils.join(File.pathSeparator, oldEnvPath, phantomBin, phantom, casper);
	}
	
	public static String getBotFile(String botName) {
		return Utils.join(File.separator, getJarDir().getPath(), "bots", getBotFileName(botName));
	}
	public static String getBotFile(Bot bot) {
		return getBotFile(bot.getShortName());
	}
	
	public static String getAvailableBots() {
		return java.util.Arrays.toString(bots.keySet().toArray());
	}
}
