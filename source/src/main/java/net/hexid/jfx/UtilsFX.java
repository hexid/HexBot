package net.hexid.jfx;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

public class UtilsFX {
	public static Insets INSETS = new Insets(17.5d, 15.0d, 0.0d, 15.0d);

	public static void setVBoxMargin(Node child) {
		VBox.setMargin(child, INSETS);
	}
}