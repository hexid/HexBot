package net.hexid.hexbot.bots;

import java.util.List;
import java.util.ArrayList;

public class Test extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Test(ArrayList<String> botArgs) {
		super(botArgs);
	}

	public ArrayList<String> getBotExecuteData() {
		return new ArrayList<String>(); // ignore passed arguments
	}

	public String getShortName() {
		return "Test";
	}
}