package net.hexid.hexbot.bots.gui.generic;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.hexid.jfx.HButton;
import net.hexid.jfx.LabeledField;
import net.hexid.jfx.UtilsFX;

public class UserPassTab extends net.hexid.hexbot.bot.gui.BotTab {
	private HButton setupBtn, repeatBtn, loginBtn;
	private String usernameData, passwordData;
	private LabeledField username, password;

	public UserPassTab(String botID) {
		super(botID);
	}

	protected Node defaultContent() {
		return createSetupContent(true);
	}

	public void processExitCode(int exitCode) {
		setupBtn.enable();
		if(exitCode != 2) repeatBtn.enable();
	}

	public String[] getBotExecuteData() {
		return new String[]{
			"--username="+usernameData,
			"--password="+passwordData
		};
	}

	protected Node[] createBottomOutputContent() {
		repeatBtn = new HButton("Repeat", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				createProcess();
			}
		}).disable().wide();

		setupBtn = new HButton("Setup", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				setContent(createSetupContent(false));
			}
		}).disable().wide();

		return new Node[]{repeatBtn, setupBtn};
	}

	private VBox createSetupContent(boolean setDefaultValues) {
		// create a setup pane withT/withoutF default values
		VBox tabContent = new VBox();

		username = new LabeledField(TextField.class, "Username: ");
		password = new LabeledField(PasswordField.class, "Password: ");

		HBox login = new HBox(7.5d);
		UtilsFX.setVBoxMargin(login);
		loginBtn = new HButton("Login", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				usernameData = username.getText();
				passwordData = password.getText();
				createProcess();
			}
		}).wide().defaultBtn();
		login.getChildren().addAll(loginBtn);

		if(!setDefaultValues) { // set previous values
			username.setText(usernameData);
			password.setText(passwordData);
		}

		// add all the elements to the container that will be added to the tab
		tabContent.getChildren().addAll(username, password, login);
		return tabContent;
	}
}