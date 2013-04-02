package net.hexid.hexbot.bot;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import net.hexid.Utils;

public class Bots {
	private static TreeMap<String, HashMap<String, String>> bots = new TreeMap<>();
	public static void addBot(String shortName, final String longName, 
			final String cliClassPath, final String guiClassPath, final String fileName) {
		bots.put(shortName.toLowerCase(), new HashMap<String, String>() {
			private static final long serialVersionUID = -5362009654584574803L;
			{
				put("longName", longName);
				put("cliClassPath", cliClassPath);
				put("guiClassPath", guiClassPath);
				put("fileName", fileName);
			}
		});
	}

	public static void removeInvalidBots() {
		Iterator<Entry<String, HashMap<String, String>>> iter = bots.entrySet().iterator();
		while(iter.hasNext()) {
			if(!(new File(getBotFile(iter.next().getKey()))).exists())
				iter.remove();
		}
	}

	/**
	 * 
	 * @return TreeMap of botNames linked with HashMaps of botData
	 */
	public static TreeMap<String, HashMap<String, String>> getBots() {
		return bots;
	}
	
	/**
	 * 
	 * @return Array of all bot names (keys to map)
	 */
	public static String[] botNames() {
		return bots.keySet().toArray(new String[0]);
	}
	
	/**
	 * 
	 * @param botName
	 * @return HashMap with bot's data
	 */
	public static HashMap<String, String> getBot(String botName) {
		return bots.get(botName.toLowerCase()); // get the data associated with a bot
	}
	
	/**
	 * 
	 * @param botName
	 * @return true if the bot has data
	 */
	public static boolean hasBot(String botName) {
		return bots.containsKey(botName.toLowerCase());
	}
	
	/**
	 * 
	 * @param botName
	 * @return Full name of the bot; null if bot doesn't exist
	 */
	public static String getBotLongName(String botName) {
		return getFromBot(botName, "longName");
	}
	/**
	 * 
	 * @param botName
	 * @return Class path for CLI bot; null if bot doesn't exist
	 */
	public static String getBotCliClassPath(String botName) {
		return getFromBot(botName, "cliClassPath");
	}
	/**
	 * 
	 * @param botName
	 * @return Class path for GUI bot; null if bot doesn't exist
	 */
	public static String getBotGuiClassPath(String botName) {
		return getFromBot(botName, "guiClassPath");
	}
	/**
	 * 
	 * @param botName
	 * @return Bot file name; null if bot doesn't exist
	 */
	public static String getBotFileName(String botName) {
		return getFromBot(botName, "fileName");
	}

	/**
	 * 
	 * @param botName
	 * @param key Key to query for in bot's HashMap
	 * @return value from bot's data; null if bot doesn't exist
	 */
	private static String getFromBot(String botName, String key) {
		// if the bot exists then get the value from that bot, otherwise null
		return (hasBot(botName)) ? getBot(botName).get(key) : null;
	}

	/**
	 * Get the directory that the application is being run from
	 * @return directory
	 */
	public static File getPWD() {
		return new File(Bots.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
	}

	public static String getBotEnvPath(String oldEnvPath) {
		String dir = getPWD().getPath();
		String sysSpecific = System.getProperty("os.name").toLowerCase();
		String casperBin = "bin";

		if(sysSpecific.contains("win")) {
			sysSpecific = "windows";
			casperBin = "batchbin";
		} else if(sysSpecific.contains("mac")) {
			sysSpecific = "macosx";
		} else {
			sysSpecific += ("-" + System.getProperty("os.arch"));
		}

		String phantom = Utils.joinFile(dir, "libs", "phantomjs");
		String phantomBin = Utils.joinFile(phantom, "bin");
		String phantomOS = Utils.joinFile(phantomBin, sysSpecific);
		String casper = Utils.joinFile(dir, "libs", "casperjs", casperBin);
		return Utils.join(File.pathSeparator, oldEnvPath, phantomOS, phantomBin, phantom, casper);
	}
	
	public static String getBotFile(String botName) {
		return Utils.joinFile(getPWD().getPath(), "bots", getBotFileName(botName));
	}
	public static String getBotFile(Bot bot) {
		return getBotFile(bot.getShortName());
	}
	
	public static String getAvailableBots() {
		return java.util.Arrays.toString(bots.keySet().toArray());
	}
}
