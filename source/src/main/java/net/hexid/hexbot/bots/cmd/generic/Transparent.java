package net.hexid.hexbot.bots.cmd.generic;

import java.util.ArrayList;

public class Transparent extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Transparent(String botID, ArrayList<String> botArgs) {
		super(botID, botArgs);
	}

	public ArrayList<String> getBotExecuteData() {
		return botArgs;
	}
}