package net.hexid.hexbot.bots.cmd;

import net.hexid.Utils;

public class Xbox extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Xbox(String botID, String[] botArgs) {
		super(botID, botArgs);
	}

	public String[] getBotExecuteData() {
		if(botArgs.length == 2) {
			botArgs = Utils.appendStrToArray(botArgs, "--password=" +
					new String(System.console().readPassword("Password: ")));
		} else if(botArgs.length != 3) {
			System.out.println("botArgs: email [, password] , code");
			System.exit(1);
		}
		return botArgs;
	}
}