package net.hexid.hexbot.bot.cmd;

import java.util.ArrayList;

public abstract class BotCommand implements net.hexid.hexbot.bot.Bot {
	protected ArrayList<String> botArgs;
	public BotCommand(ArrayList<String> botArgs) {
		this.botArgs = botArgs;
	}

	public void processExitCode(int exitCode) {
		System.out.println("Exit code: " + exitCode);
	}
}