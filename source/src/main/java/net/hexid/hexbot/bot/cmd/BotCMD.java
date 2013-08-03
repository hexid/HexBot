package net.hexid.hexbot.bot.cmd;

import java.util.Arrays;
import net.hexid.hexbot.bot.Bot;
import net.hexid.hexbot.bot.Bots;
import net.hexid.hexbot.bot.BotProcess;

public class BotCMD {
	String botClass;
	BotProcess process;
	BotCommand bot;

	public static void init(String[] args) {
		try {
			new BotCMD(args);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public BotCMD(String[] args) throws Exception {
		String botID = "";
		if(args.length > 0) {
			botClass = Bots.getBotCliClassPath(botID = args[0]);
			args = Arrays.copyOfRange(args, 1, args.length);
		}

		if(botClass != null) {
			bot = (BotCommand)Class.forName(botClass)
					.getConstructor(String.class, String[].class)
					.newInstance(botID, args);
			createProcess();
		} else {
			System.out.println("args: BotName [botArgs...]")
			if(botID.length() > 0)
				System.out.println(botID + " is an invalid bot.");
			System.out.println("Available bots: " + Bots.botIDsString());
		}
	}

	protected void createProcess() {
		System.out.println("Executing " + bot.getBotID());
		try { // create a new bot process and start it
			process = new BotCommandProcess(bot);
			process.start();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
}