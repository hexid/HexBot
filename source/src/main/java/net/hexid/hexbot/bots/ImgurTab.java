package net.hexid.hexbot.bots;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.hexid.Utils;

public class ImgurTab extends net.hexid.hexbot.bot.gui.BotTab {
	private String albumData;
	private TextField albumField;
	private Button downloadAlbumButton;

	public ImgurTab() {
		super();
	}

	public String getShortName() {
		return "Imgur";
	}

	protected Node defaultContent() {
		return createOutputContent();
	}

	public void processExitCode(int exitCode) {
		downloadAlbumButton.setDisable(false);
	}

	public ArrayList<String> getBotExecuteData() {
		ArrayList<String> data = new ArrayList<>();
		data.add("--output="+Utils.joinFile(Utils.getPWD().getPath(), "output", "Imgur"));
		data.add("--album="+albumData);
		return data;
	}

	protected Node[] createBottomOutputContent() {
		Label albumLabel = new Label("Album ID: ");

		albumField = new TextField();
		albumField.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				downloadAlbumButton.fire();
			}
		});
		HBox.setHgrow(albumField, Priority.ALWAYS);

		downloadAlbumButton = ButtonBuilder.create()
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						killProcess();
						albumData = albumField.getText();
						createProcess();
						downloadAlbumButton.setDisable(true);
					}
				}).text("Download Album").build();

		return new Node[]{albumLabel, albumField, downloadAlbumButton};
	}
}