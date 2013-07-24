package net.hexid.hexbot.bots.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import net.hexid.jfx.HButton;
import net.hexid.jfx.LabeledField;
import net.hexid.Utils;

public class ImgurTab extends net.hexid.hexbot.bot.gui.BotTab {
	private String albumData;
	private LabeledField album;
	private HButton downloadBtn;

	public ImgurTab(String botID) {
		super(botID);
	}

	protected Node defaultContent() {
		return createOutputContent();
	}

	public void processExitCode(int exitCode) {
		downloadBtn.enable();
	}

	public String[] getBotExecuteData() {
		return new String[]{
			"--output="+Utils.joinFile(Utils.getPWD().getPath(), "output", "Imgur"),
			"--album="+albumData
		};
	}

	protected Node[] createBottomOutputContent() {
		album = new LabeledField(TextField.class, "Album ID: ");

		downloadBtn = new HButton("Download Album", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				killProcess();
				albumData = album.getText();
				createProcess();
				downloadBtn.disable();
			}
		}).defaultBtn();

		return new Node[]{album, downloadBtn};
	}
}