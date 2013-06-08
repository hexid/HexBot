package net.hexid.hexbot.bots;

import java.util.ArrayList;

public class Bing extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Bing(ArrayList<String> botArgs) {
		super(botArgs);
	}

	public ArrayList<String> getBotExecuteData() {
		if(botArgs.size() == 0 || botArgs.size() > 4) {
			System.out.println("botArgs: email [, queryCount [, minDelay [, maxDelay]]]");
			System.exit(1);
		} else {
			botArgs.add("--password="+new String(System.console().readPassword("Password: ")));
		}
		return botArgs;
	}

	public String getShortName() {
		return "Bing";
	}
}