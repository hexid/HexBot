package net.hexid.hexbot.bot;

import java.util.Arrays;
import java.util.ArrayList;

public class BotCLI {
	String botClass;
	BotProcess process;
	Bot bot;

	public static void init(String[] args) {
		try {
			new BotCLI(new ArrayList<String>(Arrays.asList(args)));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public BotCLI(ArrayList<String> args) throws Exception {
		String botName = args.remove(0);
		botClass = Bots.getBotCliClassPath(botName);
		if(botClass != null) {
			bot = (Bot)Class.forName(botClass).getConstructor(ArrayList.class).newInstance(args);
			createProcess();
		} else {
			System.out.println(botName + " is an invalid bot.");
			System.out.println("Available bots: " + Bots.getAvailableBots());
		}
	}
	protected void createProcess() {
		try { // create a new bot process and start it
			process = new BotProcess(bot);
			process.start();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
}
