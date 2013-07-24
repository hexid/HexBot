package net.hexid.hexbot.bots.cmd.generic;

public class Transparent extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Transparent(String botID, String[] botArgs) {
		super(botID, botArgs);
	}

	public String[] getBotExecuteData() {
		return botArgs;
	}
}