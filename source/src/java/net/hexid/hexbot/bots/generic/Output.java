package net.hexid.hexbot.bots.generic;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import net.hexid.hexbot.bot.BotTab;
import net.hexid.jfx.HButton;

public class Output extends BotTab {
	private HButton startBtn, stopBtn;

	public Output(String botID) {
		super(botID);
	}

	protected Node defaultContent() {
		return createOutputContent();
	}

	public void processExitCode(int exitCode) {
		// print the return code and swap the state of buttons
		appendOutput("Exit Code: " + exitCode);
		startBtn.enable();
		stopBtn.disable();
	}

	public String[] getBotExecuteData() {
		return new String[0];
	}

	protected Node[] createBottomOutputContent() {
		startBtn = new HButton("Start", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				createProcess();
				startBtn.disable();
				stopBtn.enable();
			}
		}).wide().defaultBtn();

		stopBtn = new HButton("Stop", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				killProcess();
			}
		}).disable().wide().cancelBtn();

		return new Node[]{startBtn, stopBtn};
	}
}
