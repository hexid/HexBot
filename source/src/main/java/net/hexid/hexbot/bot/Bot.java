package net.hexid.hexbot.bot;

public interface Bot {
	/**
	 * @return Console parameters for the bot
	 */
	public java.util.List<String> getBotExecuteData();
	
	/**
	 * 
	 * @param exitCode Code returned when the Process exits
	 */
	public void processExitCode(int exitCode);
	
	/**
	 * 
	 * @return The name of the bot that is its key in {@link net.hexid.hexbot.bot.Bots}
	 */
	public String getShortName();
}
