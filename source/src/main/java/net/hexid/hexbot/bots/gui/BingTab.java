package net.hexid.hexbot.bots.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.hexid.jfx.HButton;
import net.hexid.jfx.IntegerField;
import net.hexid.jfx.LabeledField;
import net.hexid.jfx.UtilsFX;

public class BingTab extends net.hexid.hexbot.bot.gui.BotTab {
	private HButton setupBtn, repeatBtn, stopBtn, loginBtn;
	private String emailData, passwordData, queryCountData, delayMinData, delayMaxData;
	private LabeledField email, password, queryCount, delayMin, delayMax;

	public BingTab(String botID) {
		super(botID);
	}

	protected Node defaultContent() {
		return createSetupContent(true);
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
			"--queryCount="+queryCountData,
			"--delayMin="+delayMinData,
			"--delayMax="+delayMaxData
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

		stopBtn = new HButton("Stop", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				killProcess();
			}
		}).cancelBtn();

		return new Node[]{repeatBtn, setupBtn, stopBtn};
	}

	private VBox createSetupContent(boolean setDefaultValues) {
		// create a setup pane withT/withoutF default values
		VBox tabContent = new VBox();

		email = new LabeledField(TextField.class, "Email: ");
		password = new LabeledField(PasswordField.class, "Password: ");
		queryCount = new LabeledField(IntegerField.class, "Queries: ");

		HBox delay = new HBox(3);
		UtilsFX.setVBoxMargin(delay);
		delayMin = new LabeledField(IntegerField.class, "Delay (Secs): ");
		delayMax = new LabeledField(IntegerField.class, " to ");
		delay.getChildren().addAll(delayMin, delayMax);

		HBox login = new HBox(7.5d);
		UtilsFX.setVBoxMargin(login);
		loginBtn = new HButton("Login", new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				emailData = email.getText();
				passwordData = password.getText();
				queryCountData = queryCount.getText();
				delayMinData = delayMin.getText();
				delayMaxData = delayMax.getText();
				createProcess();
			}
		}).wide().defaultBtn();
		login.getChildren().addAll(loginBtn);

		setLoginInfo(setDefaultValues);

		// add all the elements to the container that will be added to the tab
		tabContent.getChildren().addAll(email, password, queryCount, delay, login);
		return tabContent;
	}

	private void setLoginInfo(boolean setDefaultValues) {
		if(setDefaultValues) { // set the default values
			queryCount.setText("0");
			delayMin.setText("20");
			delayMax.setText("40");
		} else { // set previous values
			email.setText(emailData);
			password.setText(passwordData);
			queryCount.setText(queryCountData);
			delayMin.setText(delayMinData);
			delayMax.setText(delayMaxData);
		}
	}
}