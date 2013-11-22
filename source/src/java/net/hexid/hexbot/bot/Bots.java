package net.hexid.hexbot.bot;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import net.hexid.Utils;

public class Bots {
	private static TreeMap<String, String[]> bots = new TreeMap<>();
	public static int INDEX_NAME = 0, INDEX_CLASS = 1, INDEX_FILE = 2;

	/**
	 * Add a new bot to the list of usable bots
	 * @param id
	 * @param name
	 * @param cliClassPath
	 * @param guiClassPath
	 * @param fileName
	 */
	public static void addBot(String id, String name, String classPath, String file) {
		bots.put(id.toLowerCase(), new String[]{name, classPath, file});
	}

	/**
	 * Remove all bots whose file does not exist
	 */
	public static void removeInvalidBots() {
		Iterator<Entry<String, String[]>> iter = bots.entrySet().iterator();
		while(iter.hasNext())
			if(!(new File(getBotFile(iter.next().getKey()))).exists())
				iter.remove();
	}

	/**
	 * @return TreeMap of botIDs linked with HashMaps of botData
	 */
	public static TreeMap<String, String[]> getBots() {
		return bots;
	}

	public static String getBotEnvPath(String oldEnvPath) {
		String dir = Utils.getPWD().getPath();
		String sysSpecific = System.getProperty("os.name").toLowerCase();
		String casperBin = "bin";

		if(sysSpecific.contains("win")) {
			sysSpecific = "windows";
			casperBin = "batchbin";
		} else if(sysSpecific.contains("mac")) {
			sysSpecific = "macosx";
		} else {
			sysSpecific += "-" + System.getProperty("os.arch");
		}

		String casper = Utils.joinFile(dir, "libs", "casperjs", casperBin);
		String phantom = Utils.joinFile(dir, "libs", "phantomjs");
		String phantomBin = Utils.joinFile(phantom, "bin");
		String phantomOS = Utils.joinFile(phantomBin, sysSpecific);
		return Utils.join(File.pathSeparator, oldEnvPath, phantomOS, phantomBin, phantom, casper);
	}

	/**
	 * @return Array of all bot IDs (keys to map)
	 */
	public static String[] botIDs() {
		return bots.keySet().toArray(new String[0]);
	}

	/**
	 * @return String representation of the array of bot IDs
	 */
	public static String botIDsString() {
		return java.util.Arrays.toString(botIDs());
	}

	/**
	 * @param botID
	 * @return HashMap with bot's data
	 */
	public static String[] getBot(String botID) {
		return bots.get(botID.toLowerCase()); // get the data associated with a bot
	}

	/**
	 * @param botID
	 * @return true if a key exists for the bot
	 */
	public static boolean hasBot(String botID) {
		return bots.containsKey(botID.toLowerCase());
	}

	/**
	 * Get the specified bot's value at a given key
	 * @param botID
	 * @param key Key to query for in bot's HashMap
	 * @return botData; null if nonexistant
	 */
	public static String getFromBot(String botID, int index) {
		return hasBot(botID) ? getBot(botID)[index] : null;
	}
	/**
	 * @param botID
	 * @param key
	 * @param value
	 * @return True if the bot exists
	 */
	public static boolean setInBot(String botID, int index, String value) {
		if(hasBot(botID)) {
			getBot(botID)[index] = value;
			return true;
		}
		return false;
	}

	public static String getBotName(String botID) {
		return getFromBot(botID, Bots.INDEX_NAME);
	}
	public static String getBotClassPath(String botID) {
		return getFromBot(botID, Bots.INDEX_CLASS);
	}
	public static String getBotFileName(String botID) {
		return getFromBot(botID, Bots.INDEX_FILE);
	}

	public static boolean setBotName(String botID, String newBotName) {
		return setInBot(botID, Bots.INDEX_NAME, newBotName);
	}
	public static boolean setBotClass(String botID, String newClassPath) {
		return setInBot(botID, Bots.INDEX_CLASS, newClassPath);
	}
	public static boolean setBotFileName(String botID, String newBotFileName) {
		return setInBot(botID, Bots.INDEX_FILE, newBotFileName);
	}

	public static String getBotFile(String botID) {
		return Utils.joinFile(Utils.getPWD().getPath(), "bots", getBotFileName(botID));
	}
	public static String getBotFile(BotTab bot) {
		return getBotFile(bot.getBotID());
	}
}
