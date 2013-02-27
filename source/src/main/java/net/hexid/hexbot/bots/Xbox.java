package net.hexid.hexbot.bots;

import java.util.List;
import java.util.ArrayList;

public class Xbox implements net.hexid.hexbot.bot.Bot {
	private ArrayList<String> botArgs;
	public Xbox(ArrayList<String> botArgs) {
		this.botArgs = botArgs;
	}

	public List<String> getBotExecuteData() {
		if(botArgs.size() <= 0 || botArgs.size() > 4) {
			System.out.println("botArgs: email code");
			System.exit(1);
		} else {
			botArgs.add("--password="+new String(System.console().readPassword("Password: ")));
		}
		return botArgs;
	}
	public void processExitCode(int exitCode) {
		
	}
	public String getShortName() {
		return "Xbox";
	}
}
