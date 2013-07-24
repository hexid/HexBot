package net.hexid.hexbot.bots.cmd;

import net.hexid.Utils;

public class Bing extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Bing(String botID, String[] botArgs) {
		super(botID, botArgs);
	}

	public String[] getBotExecuteData() {
		if(botArgs.length == 0 || botArgs.length > 5) {
			System.out.println("botArgs: email [, password] [, queryCount [, minDelay [, maxDelay]]]");
			System.exit(1);
		} else if(botArgs.length == 1 || botArgs[1].matches("-?\\d+(\\.\\d+)?")) {
			botArgs = Utils.appendStrToArray(botArgs, "--password=" +
					new String(System.console().readPassword("Password: ")));
		}
		return botArgs;
	}
}