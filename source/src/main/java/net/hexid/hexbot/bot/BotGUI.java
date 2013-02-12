package net.hexid.hexbot.bot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BotGUI extends javafx.application.Application {
	protected AnchorPane root;
	protected BotTabPane tabPane;
	
	public static void init(String[] args) {
		launch(args);
	}
	@Override public void start(Stage stage) {
		root = new AnchorPane();
		tabPane = createTabPane();
		root.getChildren().addAll(tabPane, createMenuBar());

		if(getParameters().getRaw().size() > 0) {
			for(String arg : getParameters().getRaw()) {
				if(Bots.hasBot(arg)) addTab(arg);
			}
		}
		if(tabPane.getTabs().size() == 0) {
			System.out.println("args: 'gui' [BotName...]");
			System.out.println("Available bots: " + Bots.getAvailableBots());
		}

		stage.setTitle("HexBot by Hexid");
		stage.setScene(new javafx.scene.Scene(root, 500, 300));
		stage.show();
	}

	protected void addTab(String botName) {
		try {
			tabPane.addBotTab((BotTab)Class.forName(Bots.getBotGuiClassPath(botName)).newInstance());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();
		ArrayList<Menu> menus = new ArrayList<>();
		for(final String botName : Bots.botNames()) {
			menus.add(MenuBuilder.create().text(Bots.getBotLongName(botName))
					.onShown(new EventHandler<Event>() {
						@Override public void handle(Event e) {
							((Menu)e.getSource()).hide();
							addTab(botName);
						}
					}).items(new javafx.scene.control.MenuItem()).build());
		}

		if(menus.size() == 0)
			menus.add(MenuBuilder.create().text("No bots found").build());
		menuBar.getMenus().addAll(menus);
		
		AnchorPane.setTopAnchor(menuBar, 0.0d);
		AnchorPane.setLeftAnchor(menuBar, 0.0d);
		AnchorPane.setRightAnchor(menuBar, 0.0d);
		return menuBar;
	}

	protected BotTabPane createTabPane() {
		tabPane = new BotTabPane();
		AnchorPane.setTopAnchor(tabPane, 24.0d);
		AnchorPane.setBottomAnchor(tabPane, 0.0d);
		AnchorPane.setLeftAnchor(tabPane, 0.0d);
		AnchorPane.setRightAnchor(tabPane, 0.0d);
		return tabPane;
	}
}
