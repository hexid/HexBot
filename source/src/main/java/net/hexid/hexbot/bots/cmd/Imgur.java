package net.hexid.hexbot.bots.cmd;

import net.hexid.Utils;

public class Imgur extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Imgur(String botID, String[] botArgs) {
		super(botID, botArgs);
	}

	public String[] getBotExecuteData() {
		if(botArgs.length == 1) {
			botArgs = Utils.appendStrToArray(botArgs, "--output=" +
					Utils.joinFile(Utils.getPWD().getPath(), "output", "Imgur"));
		} else if(botArgs.length != 2) {
			System.out.println("botArgs: album [, output]");
			System.exit(1);
		}
		return botArgs;
	}
}