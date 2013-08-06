package net.hexid.jfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.hexid.hexbot.bot.BotTab;

public class LabeledField extends HBox {
	private TextField field;

	public TextField getField() {
		return field;
	}

	public void setText(String text) {
		field.setText(text);
	}
	public String getText() {
		return field.getText();
	}

	public LabeledField(Class<? extends TextField> cls, String label) {
		super(3.0);
		HBox.setHgrow(this, Priority.ALWAYS);
		VBox.setMargin(this, BotTab.INSETS);

		try {
			field = cls.newInstance();
		} catch(InstantiationException | IllegalAccessException ex) {
			field = new TextField();
		}
		HBox.setHgrow(field, Priority.ALWAYS);

		getChildren().addAll(new Label(label), field);
	}
}