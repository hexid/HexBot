package net.hexid.hexbot.bots;

import java.util.List;
import java.util.ArrayList;
import net.hexid.Utils;

public class Imgur implements net.hexid.hexbot.bot.Bot {
	private ArrayList<String> botArgs;
	public Imgur(ArrayList<String> botArgs) {
		this.botArgs = botArgs;
	}

	public List<String> getBotExecuteData() {
		if(botArgs.size() == 1) {
			botArgs.add("--output=" + Utils.joinFile(Utils.getPWD().getPath(), "output", "Imgur"));
		} else if(botArgs.size() != 2) {
			System.out.println("botArgs: album [output]");
			System.exit(1);
		}
		return botArgs;
	}
	public void processExitCode(int exitCode) {
		
	}
	public String getShortName() {
		return "Imgur";
	}
}
