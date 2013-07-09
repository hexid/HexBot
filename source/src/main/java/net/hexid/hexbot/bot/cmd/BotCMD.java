package net.hexid.hexbot.bot.cmd;

import java.util.Arrays;
import java.util.ArrayList;
import net.hexid.hexbot.bot.Bot;
import net.hexid.hexbot.bot.Bots;
import net.hexid.hexbot.bot.BotProcess;

public class BotCMD {
	String botClass;
	BotProcess process;
	BotCommand bot;

	public static void init(String[] args) {
		try {
			new BotCMD(new ArrayList<String>(Arrays.asList(args)));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public BotCMD(ArrayList<String> args) throws Exception {
		String botName = "";
		if(args.size() > 0) {
			botName = args.remove(0);
			botClass = Bots.getBotCliClassPath(botName);
		}

		if(botClass != null) {
			bot = (BotCommand)Class.forName(botClass).getConstructor(ArrayList.class).newInstance(args);
			createProcess();
		} else {
			if(botName.length() > 0)
				System.out.println(botName + " is an invalid bot.");
			System.out.println("Available bots: " + Bots.botNamesString());
		}
	}
	protected void createProcess() {
		System.out.println("Executing " + bot.getShortName() + " bot.");
		try { // create a new bot process and start it
			process = new BotCommandProcess(bot);
			process.start();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
}