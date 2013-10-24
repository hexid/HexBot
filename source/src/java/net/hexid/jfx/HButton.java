package net.hexid.jfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class HButton extends Button {
	public HButton(String text, EventHandler<ActionEvent> act) {
		super(text);
		setOnAction(act);
	}

	public HButton disable() {
		setDisable(true);
		return this;
	}
	public HButton enable() {
		setDisable(false);
		return this;
	}

	public HButton defaultBtn() {
		setDefaultButton(true);
		return this;
	}
	public HButton cancelBtn() {
		setCancelButton(true);
		return this;
	}

	public HButton wide() {
		setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(this, Priority.ALWAYS);
		return this;
	}
}