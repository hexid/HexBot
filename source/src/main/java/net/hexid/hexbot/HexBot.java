package net.hexid.hexbot;

import java.util.Arrays;
import net.hexid.hexbot.bot.Bots;
import net.hexid.hexbot.bot.BotGUI;
import net.hexid.hexbot.bot.BotCLI;

public class HexBot {
	public HexBot() {
		Bots.addBot("astral", "Astral WoW", "net.hexid.hexbot.bots.Astral", "Astral.coffee");
		Bots.addBot("bing", "Bing Rewards", "net.hexid.hexbot.bots.Bing", "Bing.coffee");
		Bots.addBot("imgur", "Imgur Albums", "net.hexid.hexbot.bots.Imgur", "Imgur.coffee");
	}

	public static void main(String[] args) {
		new HexBot();
		Bots.removeInvalidBots();
		if(args.length >= 1 && args[0].equals("gui")) {
			BotGUI.init(Arrays.copyOfRange(args, 1, args.length));
		} else {
			BotCLI.init(args);
		}
	}
}
