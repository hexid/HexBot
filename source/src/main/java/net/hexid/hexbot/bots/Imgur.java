package net.hexid.hexbot.bots;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import net.hexid.Utils;
import net.hexid.hexbot.bot.Bots;

public class Imgur implements net.hexid.hexbot.bot.Bot {
	private ArrayList<String> botArgs;
	public Imgur(ArrayList<String> botArgs) {
		this.botArgs = botArgs;
	}

	public List<String> getBotExecuteData() {
		if(botArgs.size() == 1) {
			botArgs.add("--output=" + Utils.join(File.separator, Bots.getPWD().getPath(), "output", "Imgur"));
		} else if(botArgs.size() != 2) {
			System.out.println("botArgs: album [output]");
			System.exit(1);
		}
		System.out.println(Arrays.toString(botArgs.toArray()));
		return botArgs;
	}
	public void processExitCode(int exitCode) {
		
	}
	public String getShortName() {
		return "Imgur";
	}
}
