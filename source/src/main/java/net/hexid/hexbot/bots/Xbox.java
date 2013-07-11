package net.hexid.hexbot.bots;

import java.util.ArrayList;

public class Xbox extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Xbox(String botID, ArrayList<String> botArgs) {
		super(botID, botArgs);
	}

	public ArrayList<String> getBotExecuteData() {
		if(botArgs.size() <= 0 || botArgs.size() > 4) {
			System.out.println("botArgs: email code");
			System.exit(1);
		} else {
			botArgs.add("--password="+new String(System.console().readPassword("Password: ")));
		}
		return botArgs;
	}
}