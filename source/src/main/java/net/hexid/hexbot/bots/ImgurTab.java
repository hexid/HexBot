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
import net.hexid.hexbot.bot.Bots;

public class ImgurTab extends net.hexid.hexbot.bot.BotTab {
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
		switch (exitCode) {
		case 0: // no error
		default: // unknown error
			downloadAlbumButton.setDisable(false);
			break;
		}
	}

	public ArrayList<String> getBotExecuteData() {
		// returns the data that will be used
		// to call the bot in the command line
		ArrayList<String> data = new ArrayList<String>();

		data.add("--output="+Utils.join(java.io.File.separator, Bots.getJarDir().getPath(), "output", "Imgur"));
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
		HBox.setHgrow(albumLabel, Priority.NEVER);
		HBox.setHgrow(albumField, Priority.ALWAYS);
		
		downloadAlbumButton = ButtonBuilder.create()
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						exit();
						albumData = albumField.getText();
						createProcess();
						downloadAlbumButton.setDisable(true);
					}
				}).text("Download Album").build();
		HBox.setHgrow(downloadAlbumButton, Priority.NEVER);
		
		return new Node[]{albumLabel, albumField, downloadAlbumButton};
	}
}
