package net.hexid.hexbot.bot;

public class BotTabProcess extends BotProcess {
	public BotTabProcess(BotTab tab) throws java.io.IOException {
		super(tab);
	}

	@Override protected void processInput(final String in) {
		javafx.application.Platform.runLater(new Runnable() {
			@Override public void run() {
				((BotTab)bot).appendOutput(in);
			}
		});
	}
}
