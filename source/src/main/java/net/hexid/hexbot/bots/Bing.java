package net.hexid.hexbot.bots;

import java.util.ArrayList;

public class Bing extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Bing(String botID, ArrayList<String> botArgs) {
		super(botID, botArgs);
	}

	public ArrayList<String> getBotExecuteData() {
		if(botArgs.size() == 0 || botArgs.size() > 5) {
			System.out.println("botArgs: email [, password] [, queryCount [, minDelay [, maxDelay]]]");
			System.exit(1);
		} else if(botArgs.size() == 1 || botArgs.get(1).matches("-?\\d+(\\.\\d+)?")) {
			botArgs.add("--password="+new String(System.console().readPassword("Password: ")));
		}
		return botArgs;
	}
}