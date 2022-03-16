package me.itxfrosty.frostymusic.commands.cmd;

import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PlayCommand extends SlashCommand {
	private final FrostyMusic musicBot;

	public PlayCommand(final FrostyMusic musicBot) {
		super("play", "Play's Song from either link or keywords.", "/play", false);
		this.musicBot = musicBot;

		this.addOption(new OptionData(OptionType.STRING, "input", "A search term or link.", true));
	}

	@Override
	public void execute(CommandEvent event, String args) {
		musicBot.getGuildAudioManager().loadAndPlay(event, args, true);
	}
}

