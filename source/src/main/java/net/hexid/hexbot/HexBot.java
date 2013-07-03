package net.hexid.hexbot;

import java.util.Arrays;
import net.hexid.hexbot.bot.Bots;
import net.hexid.hexbot.bot.gui.BotGUI;
import net.hexid.hexbot.bot.cmd.BotCMD;

public class HexBot {
	public HexBot() {
		Bots.addBot("astral", "Astral WoW", "net.hexid.hexbot.bots.Astral", "net.hexid.hexbot.bots.AstralTab", "Astral.coffee");
		Bots.addBot("bing", "Bing Rewards", "net.hexid.hexbot.bots.Bing", "net.hexid.hexbot.bots.BingTab", "Bing.coffee");
		Bots.addBot("imgur", "Imgur Albums", "net.hexid.hexbot.bots.Imgur", "net.hexid.hexbot.bots.ImgurTab", "Imgur.coffee");
		Bots.addBot("molten", "Molten WoW", "net.hexid.hexbot.bots.Molten", "net.hexid.hexbot.bots.MoltenTab", "Molten.coffee");
		Bots.addBot("test", "Test", "net.hexid.hexbot.bots.Test", "net.hexid.hexbot.bots.TestTab", "Test.coffee");
		Bots.addBot("xbox", "Xbox Codes", "net.hexid.hexbot.bots.Xbox", "net.hexid.hexbot.bots.XboxTab", "Xbox.coffee");
	}

	protected void init(String[] args) {
		Bots.removeInvalidBots();

		// if there are no arguments of the first is `gui`
		if(args.length == 0 || args[0].equalsIgnoreCase("gui")) {
			BotGUI.init(Arrays.copyOfRange(args, (args.length > 0) ? 1 : 0, args.length));
		} else { // otherwise use the command line
			BotCMD.init(args);
		}
	}

	public static void main(String[] args) {
		HexBot hexbot = new HexBot();
		hexbot.init(args);
	}
}
