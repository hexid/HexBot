package net.hexid.hexbot.bot;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class BotTab{
	public static final Insets INSETS = new Insets(17.5d, 15.0d, 0.0d, 15.0d);

	protected String botID;
	protected BotProcess process;
	protected TextArea output;
	protected Tab tab;

	public BotTab(String botID) {
		this.botID = botID;
		tab = new Tab(Bots.getBotName(botID));
		setContent(defaultContent());
	}

	public String getBotID() {
		return botID;
	}
	public Tab getTab() {
		return tab;
	}
	protected void setContent(Node node) {
		tab.setContent(node);
	}

	public abstract void processExitCode(int exitCode);
	protected abstract Node defaultContent(); // what is shown when the tab is created
	protected abstract Node[] createBottomOutputContent();
	public abstract String[] getBotExecuteData();

	protected void createProcess() {
		tab.setContent(createOutputContent()); // change to output mode
		try { // create a new bot process and start it
			process = new BotProcess(this);
			process.start();
		} catch (IOException e) {
			appendOutput(e.getMessage());
		}
	}

	protected VBox createOutputContent() { // create an output pane
		VBox outputContent = new VBox();

		output = new TextArea();
		output.setEditable(false);
		VBox.setVgrow(output, Priority.ALWAYS);
		outputContent.getChildren().add(output);

		Node[] bottomNodes = createBottomOutputContent();
		if(bottomNodes != null) {
			HBox bottom = new HBox(7.5d);
			VBox.setMargin(bottom, new Insets(5.0d, 15.0d, 5.0d, 15.0d));
			VBox.setVgrow(bottom, Priority.NEVER);
			HBox.setHgrow(bottom, Priority.ALWAYS);
			bottom.getChildren().addAll(bottomNodes);
			outputContent.getChildren().add(bottom);
		}

		return outputContent;
	}

	public void killProcess() { // stop the process, if one exists
		if(process != null)
			process.killProcess();
	}

	/**
	 * Append a string to the output and move the cursor to the end of that string
	 * @param str
	 */
	public void appendOutput(String str) {
		if(output != null) {
			output.appendText((output.getText().equals("") ? "" : '\n') + str);
			output.positionCaret(output.getLength() - str.length() + 2);
		}
	}
}
