package net.hexid.hexbot.bot.cmd;

public class BotCommandProcess extends net.hexid.hexbot.bot.BotProcess {
	public BotCommandProcess(BotCommand cmd) throws java.io.IOException {
		super(cmd);
	}

	protected void processInput(final String in) {
		System.out.println(in);
	}
}