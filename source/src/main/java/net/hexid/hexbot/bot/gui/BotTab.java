package net.hexid.hexbot.bot.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class BotTab extends javafx.scene.control.Tab implements net.hexid.hexbot.bot.Bot {
	protected BotTabProcess process;
	protected TextArea output;

	public BotTab() {
		super();
		setText(getShortName());
		setContent(defaultContent());
	}

	public abstract String getShortName();
	public abstract void processExitCode(int exitCode);
	protected abstract Node defaultContent(); // what is shown when the tab is created
	protected abstract Node[] createBottomOutputContent();

	protected void createProcess() {
		setContent(createOutputContent()); // change to output mode
		try { // create a new bot process and start it
			process = new BotTabProcess(this);
			process.start();
		} catch (java.io.IOException e) {
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

	public void destroyProcess() { // stop the process, if one exists
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