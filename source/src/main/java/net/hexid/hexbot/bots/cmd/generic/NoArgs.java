package net.hexid.hexbot.bots.cmd.generic;

import java.util.ArrayList;

public class NoArgs extends net.hexid.hexbot.bot.cmd.BotCommand {
	public NoArgs(String botID, ArrayList<String> botArgs) {
		super(botID, botArgs);
	}

	public ArrayList<String> getBotExecuteData() {
		return new ArrayList<String>(); // ignore passed arguments
	}
}