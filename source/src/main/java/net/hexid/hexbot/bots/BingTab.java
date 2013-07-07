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
import net.hexid.jfx.IntegerField;

public class BingTab extends net.hexid.hexbot.bot.gui.BotTab {
	private Button returnToLoginButton, repeatProcessButton, stopProcessButton, loginButton;
	private IntegerField queryCount, delayMin, delayMax;
	private String emailData, passwordData, queryCountData, delayMinData, delayMaxData;
	private TextField emailField;
	private PasswordField passwordField;

	public BingTab() {
		super();
	}

	public String getShortName() {
		return "Bing";
	}

	protected Node defaultContent() {
		return createSetupContent(true);
	}

	public void processExitCode(int exitCode) {
		stopProcessButton.setDisable(true);
		returnToLoginButton.setDisable(false);
		if(exitCode != 2) repeatProcessButton.setDisable(false);
	}

	public ArrayList<String> getBotExecuteData() {
		ArrayList<String> data = new ArrayList<>();
		data.add("--email=" + emailData);
		data.add("--password=" + passwordData);
		data.add("--queryCount=" + queryCountData);
		data.add("--delayMin=" + delayMinData);
		data.add("--delayMax=" + delayMaxData);
		return data;
	}

	protected Node[] createBottomOutputContent() {
		ButtonBuilder<? extends ButtonBuilder> buttons = ButtonBuilder.create();

		stopProcessButton = buttons.text("Stop")
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						killProcess();
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
						setContent(createSetupContent(false));
					}
				}).build();
		HBox.setHgrow(returnToLoginButton, Priority.ALWAYS);

		return new Node[]{repeatProcessButton, returnToLoginButton, stopProcessButton};
	}

	private VBox createSetupContent(boolean setDefaultValues) {
		// create a setup pane withT/withoutF default values
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

		HBox query = new HBox(3);
		VBox.setMargin(query, inset);
		Label queryLabel = new Label("Queries: ");
		queryCount = new IntegerField();
		HBox.setHgrow(queryCount, Priority.ALWAYS);
		query.getChildren().addAll(queryLabel, queryCount);

		HBox delay = new HBox(3);
		VBox.setMargin(delay, inset);
		Label delayLabel = new Label("Delay (Secs): ");
		delayMin = new IntegerField();
		HBox.setHgrow(delayMin, Priority.ALWAYS);
		Label delayToLabel = new Label(" to ");
		delayMax = new IntegerField();
		HBox.setHgrow(delayMax, Priority.ALWAYS);
		delay.getChildren().addAll(delayLabel, delayMin, delayToLabel, delayMax);

		HBox button = new HBox(7.5d);
		VBox.setMargin(button, inset);
		loginButton = ButtonBuilder.create()
				.onAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						emailData = emailField.getText();
						passwordData = passwordField.getText();
						queryCountData = queryCount.getText();
						delayMinData = delayMin.getText();
						delayMaxData = delayMax.getText();
						createProcess();
					}
				}).text("Login").maxWidth(Double.MAX_VALUE).build();
		HBox.setHgrow(loginButton, Priority.ALWAYS);
		button.getChildren().addAll(loginButton);

		if(setDefaultValues) { // set the default values
			queryCount.setInteger(0);
			delayMin.setInteger(20);
			delayMax.setInteger(40);
		} else { // set previous values
			emailField.setText(emailData);
			passwordField.setText(passwordData);
			queryCount.setText(queryCountData);
			delayMin.setText(delayMinData);
			delayMax.setText(delayMaxData);
		}

		// add all the elements to the container that will be added to the tab
		tabContent.getChildren().addAll(email, password, query, delay, button);
		return tabContent;
	}
}