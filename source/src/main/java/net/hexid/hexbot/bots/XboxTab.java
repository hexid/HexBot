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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class XboxTab extends net.hexid.hexbot.bot.BotTab {
	private Button returnToLoginButton, repeatProcessButton, stopProcessButton, loginButton;
	private String emailData, passwordData, codeData;
	private TextField emailField, codeField;
	private PasswordField passwordField;

	public XboxTab() {
		super();
	}

	public String getShortName() {
		return "Xbox";
	}

	protected Node defaultContent() {
		return createSetupContent(true);
	}

	public void processExitCode(int exitCode) {
		switch (exitCode) {
		case 2: // login error
			returnToLoginButton.setDisable(false);
			break;
		case 0: // no error
		case 1: // phantomjs/casperjs error
		case 3: // internet connection error
		default: // unknown error
			returnToLoginButton.setDisable(false);
			repeatProcessButton.setDisable(false);
			stopProcessButton.setDisable(true);
			break;
		}
	}

	public ArrayList<String> getBotExecuteData() {
		// returns the data that will be used
		// to call the bot in the command line
		ArrayList<String> data = new ArrayList<String>();

		// user-entered data
		data.add("--email=" + emailData);
		data.add("--password=" + passwordData);
		data.add("--code=" + codeData);
		return data;
	}
	
	protected Node[] createBottomOutputContent() {
		repeatProcessButton = ButtonBuilder.create().text("Repeat")
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						createProcess();
					}
				}).disable(true).maxWidth(Double.MAX_VALUE).build();
		HBox.setHgrow(repeatProcessButton, Priority.ALWAYS);

		returnToLoginButton = ButtonBuilder.create().text("Setup")
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						setContent(createSetupContent(false));
					}
				}).disable(true).maxWidth(Double.MAX_VALUE).build();
		HBox.setHgrow(returnToLoginButton, Priority.ALWAYS);

		stopProcessButton = ButtonBuilder.create().text("Stop")
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						process.destroy(); // exit code may vary (143 encountered during tests)
					}
				}).build();

		return new Node[]{repeatProcessButton, returnToLoginButton, stopProcessButton};
	}

	public VBox createSetupContent(boolean setDefaultValues) {
		// create a setup pane withT/withoutF default values
		VBox tabContent = new VBox();
		Insets inset = new Insets(17.5d, 15.0d, 0.0d, 15.0d);

		HBox email = new HBox(3);
		VBox.setMargin(email, inset);
		Label emailLabel = new Label("Email: ");
		HBox.setHgrow(emailLabel, Priority.NEVER);
		emailField = new TextField();
		HBox.setHgrow(emailField, Priority.ALWAYS);
		email.getChildren().addAll(emailLabel, emailField);

		HBox password = new HBox(3);
		VBox.setMargin(password, inset);
		Label passwordLabel = new Label("Password: ");
		HBox.setHgrow(passwordLabel, Priority.NEVER);
		passwordField = new PasswordField();
		passwordField.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				loginButton.fire();
			}
		});
		HBox.setHgrow(passwordField, Priority.ALWAYS);
		password.getChildren().addAll(passwordLabel, passwordField);

		HBox code = new HBox(3);
		VBox.setMargin(code, inset);
		Label codeLabel = new Label("Code: ");
		HBox.setHgrow(emailLabel, Priority.NEVER);
		codeField = new TextField();
		HBox.setHgrow(codeField, Priority.ALWAYS);
		code.getChildren().addAll(codeLabel, codeField);

		HBox button = new HBox(7.5d);
		VBox.setMargin(button, inset);
		loginButton = ButtonBuilder.create()
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						emailData = emailField.getText();
						passwordData = passwordField.getText();
						codeData = codeField.getText();
						createProcess();
					}
				}).text("Login").maxWidth(Double.MAX_VALUE).build();
		HBox.setHgrow(loginButton, Priority.ALWAYS);
		button.getChildren().addAll(loginButton);

		emailField.setText(emailData);
		passwordField.setText(passwordData);
		codeField.setText(codeData);

		// add all the elements to the container that will be added to the tab
		tabContent.getChildren().addAll(email, password, code, button);
		return tabContent;
	}
}
