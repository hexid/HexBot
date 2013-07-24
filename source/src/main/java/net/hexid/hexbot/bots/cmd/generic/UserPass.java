package net.hexid.hexbot.bots.cmd.generic;

import net.hexid.Utils;

public class UserPass extends net.hexid.hexbot.bot.cmd.BotCommand {
	public UserPass(String botID, String[] botArgs) {
		super(botID, botArgs);
	}

	public String[] getBotExecuteData() {
		if(botArgs.length == 1) {
			botArgs = Utils.appendStrToArray(botArgs, "--password=" +
					new String(System.console().readPassword("Password: ")));
		} else if(botArgs.length != 2) {
			System.out.println("botArgs: username [, password]");
			System.exit(1);
		}
		return botArgs;
	}
}