package net.hexid.hexbot.bots.cmd;

import java.util.ArrayList;

public class Xbox extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Xbox(String botID, ArrayList<String> botArgs) {
		super(botID, botArgs);
	}

	public ArrayList<String> getBotExecuteData() {
		if(botArgs.size() < 2 || botArgs.size() > 3) {
			System.out.println("botArgs: email [, password] , code");
			System.exit(1);
		} else if(botArgs.size() == 2) {
			botArgs.add("--password="+new String(System.console().readPassword("Password: ")));
		}
		return botArgs;
	}
}