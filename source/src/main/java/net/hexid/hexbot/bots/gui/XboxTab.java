package net.hexid.hexbot.bots.gui;

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

public class XboxTab extends net.hexid.hexbot.bot.gui.BotTab {
	private HButton setupBtn, repeatBtn, stopBtn, loginBtn;
	private String emailData, passwordData, codeData;
	private TextField emailField, codeField;
	private PasswordField passwordField;
	private LabeledField email, password, code;

	public XboxTab(String botID) {
		super(botID);
	}

	protected Node defaultContent() {
		return createSetupContent();
	}

	public void processExitCode(int exitCode) {
		stopBtn.disable();
		setupBtn.enable();
		if(exitCode != 2) repeatBtn.enable();
	}

	public String[] getBotExecuteData() {
		return new String[]{
			"--email="+emailData,
			"--password="+passwordData,
			"--code="+codeData
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
				setContent(createSetupContent());
			}
		}).disable().wide();

		stopBtn = new HButton("Stop", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				killProcess();
			}
		}).cancelBtn();

		return new Node[]{repeatBtn, setupBtn, stopBtn};
	}

	private VBox createSetupContent() {
		VBox tabContent = new VBox();

		email = new LabeledField(TextField.class, "Email: ");
		password = new LabeledField(PasswordField.class, "Password: ");
		code = new LabeledField(TextField.class, "Code: ");

		HBox login = new HBox(7.5d);
		UtilsFX.setVBoxMargin(login);
		loginBtn = new HButton("Login", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				emailData = email.getText();
				passwordData = password.getText();
				codeData = code.getText();
				createProcess();
			}
		}).wide().defaultBtn();
		login.getChildren().addAll(loginBtn);

		email.setText(emailData);
		password.setText(passwordData);
		code.setText(codeData);

		// add all the elements to the container that will be added to the tab
		tabContent.getChildren().addAll(email, password, code, login);
		return tabContent;
	}
}