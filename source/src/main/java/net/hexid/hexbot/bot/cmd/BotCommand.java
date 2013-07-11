package net.hexid.hexbot.bot.cmd;

import java.util.ArrayList;

public abstract class BotCommand implements net.hexid.hexbot.bot.Bot {
	protected String botID;
	protected ArrayList<String> botArgs;
	public BotCommand(String botID, ArrayList<String> botArgs) {
		this.botID = botID;
		this.botArgs = botArgs;
	}

	public String getBotID() {
		return botID;
	}

	public void processExitCode(int exitCode) {
		System.out.println("Exit code: " + exitCode);
	}
}