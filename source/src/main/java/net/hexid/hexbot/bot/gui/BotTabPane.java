package net.hexid.hexbot.bot.gui;

import javafx.event.Event;
import javafx.scene.control.Tab;

public class BotTabPane extends javafx.scene.control.TabPane {
	public BotTabPane() {
		super();
		setSide(javafx.geometry.Side.BOTTOM); // have the tabs along the bottom
	}

	public void addBotTab(final BotTab botTab) {
		Tab tab = botTab.getTab();
		// kill the process and remove the tab from the list when closed
		tab.setOnClosed(new javafx.event.EventHandler<Event>() {
			public void handle(Event e) {
				botTab.killProcess(); // stop the bot process in the tab
			}
		});
		getTabs().add(tab);
		getSelectionModel().select(tab);
	}
}