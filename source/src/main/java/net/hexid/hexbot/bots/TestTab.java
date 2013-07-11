package net.hexid.hexbot.bots;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.hexid.hexbot.bot.Bots;

public class TestTab extends net.hexid.hexbot.bot.gui.BotTab {
	private Button startProcessButton, stopProcessButton;

	public TestTab(String botID) {
		super(botID);
	}

	protected Node defaultContent() {
		return createOutputContent();
	}

	public void processExitCode(int exitCode) {
		// print the return code and
		// swap the state of buttons
		appendOutput("Exit Code: " + exitCode);
		startProcessButton.setDisable(false);
		stopProcessButton.setDisable(true);
	}

	public ArrayList<String> getBotExecuteData() {
		return new ArrayList<String>();
	}

	protected Node[] createBottomOutputContent() {
		startProcessButton = ButtonBuilder.create().text("Start")
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						createProcess();
						startProcessButton.setDisable(true);
						stopProcessButton.setDisable(false);
					}
				}).maxWidth(Double.MAX_VALUE).build();
		HBox.setHgrow(startProcessButton, Priority.ALWAYS);

		stopProcessButton = ButtonBuilder.create().text("Stop")
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						killProcess();
					}
				}).disable(true).maxWidth(Double.MAX_VALUE).build();
		HBox.setHgrow(stopProcessButton, Priority.ALWAYS);

		return new Node[]{startProcessButton, stopProcessButton};
	}
}