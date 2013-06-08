package net.hexid.hexbot.bot.gui;

public class BotTabProcess extends net.hexid.hexbot.bot.BotProcess {
	public BotTabProcess(BotTab tab) throws java.io.IOException {
		super(tab);
	}

	protected void processInput(final String in) {
		javafx.application.Platform.runLater(new Runnable() {
			@Override public void run() {
				((BotTab)bot).appendOutput(in);
			}
		});
	}
}