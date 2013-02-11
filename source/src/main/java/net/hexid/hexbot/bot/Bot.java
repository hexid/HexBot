package net.hexid.hexbot.bot;

public interface Bot {
	public java.util.List<String> getBotExecuteData();
	public void processExitCode(int exitCode);
	public String getShortName();
}
