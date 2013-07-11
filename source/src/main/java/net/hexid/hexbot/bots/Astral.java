package net.hexid.hexbot.bots;

import java.util.List;
import java.util.ArrayList;

public class Astral extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Astral(String botID, ArrayList<String> botArgs) {
		super(botID, botArgs);
	}

	public List<String> getBotExecuteData() {
		if(botArgs.size() == 1) {
			botArgs.add("--password="+new String(System.console().readPassword("Password: ")));
		} else if(botArgs.size() != 2) {
			System.out.println("botArgs: username [, password]");
			System.exit(1);
		}
		return botArgs;
	}
}