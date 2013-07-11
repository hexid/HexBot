package net.hexid.hexbot.bots;

import java.util.List;
import java.util.ArrayList;

public class Test extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Test(String botID, ArrayList<String> botArgs) {
		super(botID, botArgs);
	}

	public ArrayList<String> getBotExecuteData() {
		return new ArrayList<String>(); // ignore passed arguments
	}
}