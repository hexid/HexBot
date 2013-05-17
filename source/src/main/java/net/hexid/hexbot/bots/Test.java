package net.hexid.hexbot.bots;

import java.util.List;
import java.util.ArrayList;

public class Test implements net.hexid.hexbot.bot.Bot {
	private ArrayList<String> botArgs;
	public Test(ArrayList<String> botArgs) {
		this.botArgs = botArgs;
	}

	public List<String> getBotExecuteData() {
		return new ArrayList<String>(); // ignore passed arguments
	}
	public void processExitCode(int exitCode) {
		
	}
	public String getShortName() {
		return "Test";
	}
}
