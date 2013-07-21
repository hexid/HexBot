package net.hexid.hexbot.bot.cmd;

import java.util.ArrayList;
import net.hexid.hexbot.bot.Bot;

public abstract class BotCommand extends Bot {
	protected ArrayList<String> botArgs;
	public BotCommand(String botID, ArrayList<String> botArgs) {
		super(botID);
		this.botArgs = botArgs;
	}

	public void processExitCode(int exitCode) {
		System.out.println("Exit code: " + exitCode);
	}
}