package net.hexid.hexbot;

import java.util.Arrays;
import java.util.Scanner;
import net.hexid.hexbot.bot.Bots;
import net.hexid.hexbot.bot.gui.BotGUI;
import net.hexid.hexbot.bot.cmd.BotCMD;

public class HexBot {
	public HexBot() {
		Bots.addBot("astral", "Astral WoW", "net.hexid.hexbot.bots.cmd.generic.UserPass", "net.hexid.hexbot.bots.gui.AstralTab", "Astral.coffee");
		Bots.addBot("bing", "Bing Rewards", "net.hexid.hexbot.bots.cmd.Bing", "net.hexid.hexbot.bots.gui.BingTab", "Bing.coffee");
		Bots.addBot("imgur", "Imgur Albums", "net.hexid.hexbot.bots.cmd.Imgur", "net.hexid.hexbot.bots.gui.ImgurTab", "Imgur.coffee");
		Bots.addBot("molten", "Molten WoW", "net.hexid.hexbot.bots.cmd.generic.UserPass", "net.hexid.hexbot.bots.gui.MoltenTab", "Molten.coffee");
		Bots.addBot("test-coffee", "Test-CF", "net.hexid.hexbot.bots.cmd.generic.Transparent", "net.hexid.hexbot.bots.gui.TestTab", "Test.coffee");
		Bots.addBot("test-js", "Test-JS", "net.hexid.hexbot.bots.cmd.generic.Transparent", "net.hexid.hexbot.bots.gui.TestTab", "Test.js");
		Bots.addBot("xbox", "Xbox Codes", "net.hexid.hexbot.bots.cmd.Xbox", "net.hexid.hexbot.bots.gui.XboxTab", "Xbox.coffee");
	}

	protected void init(String[] args) {
		Bots.removeInvalidBots();

		// if there are no arguments of the first is `gui`
		if(args.length == 0 || args[0].equalsIgnoreCase("gui"))
			BotGUI.init(Arrays.copyOfRange(args, (args.length > 0) ? 1 : 0, args.length));
		else // otherwise use the command line
			BotCMD.init(args);
	}

	public static void main(String[] args) {
		new HexBot().init(args);
	}
}
