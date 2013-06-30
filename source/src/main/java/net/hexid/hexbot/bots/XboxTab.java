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

public class XboxTab extends net.hexid.hexbot.bot.gui.BotTab {
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
		return createSetupContent();
	}

	public void processExitCode(int exitCode) {
		stopProcessButton.setDisable(true);
		returnToLoginButton.setDisable(false);
		if(exitCode != 2) repeatProcessButton.setDisable(false);
	}

	public ArrayList<String> getBotExecuteData() {
		ArrayList<String> data = new ArrayList<String>();
		data.add("--email=" + emailData);
		data.add("--password=" + passwordData);
		data.add("--code=" + codeData);
		return data;
	}

	protected Node[] createBottomOutputContent() {
		ButtonBuilder<? extends ButtonBuilder> buttons = ButtonBuilder.create();

		stopProcessButton = buttons.text("Stop")
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						destroyProcess();
					}
				}).build();

		buttons = buttons.disable(true).maxWidth(Double.MAX_VALUE);

		repeatProcessButton = buttons.text("Repeat")
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						createProcess();
					}
				}).build();
		HBox.setHgrow(repeatProcessButton, Priority.ALWAYS);

		returnToLoginButton = buttons.text("Setup")
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						setContent(createSetupContent());
					}
				}).build();
		HBox.setHgrow(returnToLoginButton, Priority.ALWAYS);

		return new Node[]{repeatProcessButton, returnToLoginButton, stopProcessButton};
	}

	private VBox createSetupContent() {
		VBox tabContent = new VBox();
		Insets inset = new Insets(17.5d, 15.0d, 0.0d, 15.0d);

		HBox email = new HBox(3);
		VBox.setMargin(email, inset);
		Label emailLabel = new Label("Email: ");
		emailField = new TextField();
		HBox.setHgrow(emailField, Priority.ALWAYS);
		email.getChildren().addAll(emailLabel, emailField);

		HBox password = new HBox(3);
		VBox.setMargin(password, inset);
		Label passwordLabel = new Label("Password: ");
		passwordField = new PasswordField();
		passwordField.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				loginButton.fire();
			}
		});
		HBox.setHgrow(passwordField, Priority.ALWAYS);
		password.getChildren().addAll(passwordLabel, passwordField);

		HBox code = new HBox(3);
		VBox.setMargin(code, inset);
		Label codeLabel = new Label("Code: ");
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