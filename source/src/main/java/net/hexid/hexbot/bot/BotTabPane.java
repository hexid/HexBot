package net.hexid.hexbot.bot;

import javafx.event.Event;

public class BotTabPane extends javafx.scene.control.TabPane {
	public BotTabPane() {
		super();
		setSide(javafx.geometry.Side.BOTTOM); // have the tabs along the bottom
	}

	public void addBotTab(final BotTab tab) {
		// kill the process and remove the tab from the list when closed
		tab.setOnClosed(new javafx.event.EventHandler<Event>() {
			public void handle(Event e) {
				tab.exit(); // stop the bot process in the tab
			}
		});
		getTabs().add(tab);
		getSelectionModel().select(tab);
	}
}
