package net.hexid.hexbot.bot.cmd;

public abstract class BotCommand extends net.hexid.hexbot.bot.Bot {
	protected String[] botArgs;
	public BotCommand(String botID, String[] botArgs) {
		super(botID);
		this.botArgs = botArgs;
	}

	public void processExitCode(int exitCode) {
		System.out.println("Exit code: " + exitCode);
	}
}