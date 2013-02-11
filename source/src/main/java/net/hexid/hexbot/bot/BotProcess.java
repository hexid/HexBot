package net.hexid.hexbot.bot;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import net.hexid.Utils;

public class BotProcess extends Thread {
	private Process process;
	private Scanner in;
	protected Bot bot;

	public BotProcess(Bot bot) throws IOException {
		super();
		this.bot = bot;
		this.process = createProcess();
		this.in = new Scanner(this.process.getInputStream());
	}
	
	protected Process createProcess() throws IOException {
		List<String> botExecuteData = bot.getBotExecuteData();
		botExecuteData.add(0, "casperjs"); // program (will be added to PATH)
		botExecuteData.add(1, Bots.getBotFile(bot));

		String processData = Utils.join(" ", botExecuteData);
		String pathName;
		if(File.pathSeparator.equals(";")) {
			pathName = "Path";
			botExecuteData = Arrays.asList(new String[]{"cmd.exe","/C",processData});
		} else {
			pathName = "PATH";
			botExecuteData = Arrays.asList(new String[]{"/usr/bin/env","bash","-c",processData});
		}

		ProcessBuilder pb = new ProcessBuilder(botExecuteData).redirectErrorStream(true);
		pb.environment().put(pathName, Bots.getBotEnvPath(pb.environment().get(pathName)));

		return pb.start();
	}

	protected void processInput(final String in) {
		System.out.println(in);
	}
	@Override public void run() {
		while(in.hasNext()) {
			processInput(in.nextLine());
		}
		in.close();

		try {
			process.waitFor();
			bot.processExitCode(process.exitValue());
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void exit() {
		process.destroy();
	}
}
