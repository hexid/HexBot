package net.hexid.hexbot.bot;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class BotTabPane extends TabPane {
	public BotTabPane() {
		super();
		setSide(Side.BOTTOM); // have the tabs along the bottom
	}

	public void addBotTab(final BotTab botTab) {
		Tab tab = botTab.getTab();
		// kill the process and remove the tab from the list when closed
		tab.setOnClosed(new EventHandler<Event>() {
			public void handle(Event e) {
				botTab.killProcess(); // stop the bot process in the tab
			}
		});
		getTabs().add(tab);
		getSelectionModel().select(tab);
	}
}
