package net.hexid.hexbot.bot;

public abstract class Bot {
	String botID;

	public Bot(String botID) {
		this.botID = botID;
	}

	/**
	 * @return The id of the bot that is its key in {@link net.hexid.hexbot.bot.Bots}
	 */
	public String getBotID() {
		return botID;
	}

	/**
	 * @return Console parameters for the bot
	 */
	public abstract java.util.List<String> getBotExecuteData();

	/**
	 * @param exitCode Code returned when the Process exits
	 */
	public abstract void processExitCode(int exitCode);
}