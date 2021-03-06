package net.hexid.hexbot;

import java.util.Arrays;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBuilder;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.hexid.hexbot.bot.Bots;
import net.hexid.hexbot.bot.BotTab;
import net.hexid.hexbot.bot.BotTabPane;

public class HexBot extends javafx.application.Application {
	protected AnchorPane root;
	protected BotTabPane tabPane;

	public static void main(String[] args) {
		new HexBot().init(args);
	}

	public HexBot() {
		Bots.addBot("bing", "Bing Rewards", "net.hexid.hexbot.bots.Bing", "Bing.coffee");
		Bots.addBot("imgur", "Imgur Albums", "net.hexid.hexbot.bots.Imgur", "Imgur.coffee");
		Bots.addBot("test-coffee", "Test-CF", "net.hexid.hexbot.bots.generic.Output", "Test.coffee");
		Bots.addBot("test-js", "Test-JS", "net.hexid.hexbot.bots.generic.Output", "Test.js");
	}

	protected void init(String[] args) {
		Bots.removeInvalidBots();
		launch(args);
	}

	@Override public void start(Stage stage) {
		root = new AnchorPane();
		tabPane = createTabPane();
		root.getChildren().addAll(tabPane, createMenuBar());

		if(getParameters().getRaw().size() > 0) // if there are arguments
			for(String arg : getParameters().getRaw()) // iterate over bot names
				if(Bots.hasBot(arg)) addTab(arg); // add bot if the name exists

		if(tabPane.getTabs().size() == 0) // if no arguments were passed to the gui
			System.out.println("args: [BotName...]\nAvailable bots: " + Bots.botIDsString());

		stage.getIcons().add(new Image(HexBot.class.getResourceAsStream("/HexBot-Icon.png")));
		stage.setTitle("HexBot by Hexid");
		stage.setScene(new Scene(root, 750, 360));
		stage.show();
	}

	protected void addTab(String botID) {
		try { // create a new tab based on the bot's guiClassPath
			tabPane.addBotTab((BotTab)Class.forName(Bots.getBotClassPath(botID))
					.getConstructor(String.class).newInstance(botID));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();
		ObservableList<Menu> menus = menuBar.getMenus();
		MenuBuilder<? extends MenuBuilder> mb = MenuBuilder.create();

		for(final String botID : Bots.botIDs()) // iterate over botNames
			// create a menu item with the bot's long name
			menus.add(mb.text(Bots.getBotName(botID))
					.onShown(new EventHandler<Event>() {
						@Override public void handle(Event e) {
							((Menu)e.getSource()).hide();
							addTab(botID); // create a new tab
						}
					}).items(new MenuItem()).build());

		if(menus.size() == 0) // if no bot tabs were created
			menus.add(mb.text("No bots found").build());

		AnchorPane.setTopAnchor(menuBar, 0.0d);
		AnchorPane.setLeftAnchor(menuBar, 0.0d);
		AnchorPane.setRightAnchor(menuBar, 0.0d);
		return menuBar;
	}

	protected BotTabPane createTabPane() {
		tabPane = new BotTabPane();
		AnchorPane.setTopAnchor(tabPane, 24.0d); // fit to the top menu bar
		AnchorPane.setBottomAnchor(tabPane, 0.0d);
		AnchorPane.setLeftAnchor(tabPane, 0.0d);
		AnchorPane.setRightAnchor(tabPane, 0.0d);
		return tabPane;
	}
}
