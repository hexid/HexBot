package net.hexid.hexbot.bots;

import java.util.ArrayList;
import net.hexid.Utils;

public class Imgur extends net.hexid.hexbot.bot.cmd.BotCommand {
	public Imgur(String botID, ArrayList<String> botArgs) {
		super(botID, botArgs);
	}

	public ArrayList<String> getBotExecuteData() {
		if(botArgs.size() == 1) {
			botArgs.add("--output=" + Utils.joinFile(Utils.getPWD().getPath(), "output", "Imgur"));
		} else if(botArgs.size() != 2) {
			System.out.println("botArgs: album [output]");
			System.exit(1);
		}
		return botArgs;
	}
}