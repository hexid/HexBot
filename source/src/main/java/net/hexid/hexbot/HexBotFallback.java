package net.hexid.hexbot;

import java.util.Arrays;
import net.hexid.hexbot.bot.Bots;
import net.hexid.hexbot.bot.cmd.BotCMD;

public class HexBotFallback extends HexBot {
	public HexBotFallback() {
		super();
	}

	protected void init(String[] args) {
		Bots.removeInvalidBots();

		if(args.length > 0 && args[0].equalsIgnoreCase("gui"))
			args = Arrays.copyOfRange(args, 1, args.length);

		BotCMD.init(args);
	}

	public static void main(String[] args) {
		new HexBotFallback().init(args);
	}
}
