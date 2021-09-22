package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.commands.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PlayCommand extends SlashCommand {
	private final MusicBot musicBot;

	public PlayCommand(final MusicBot musicBot) {
		super("play", "Play's Song from either link or keywords.", "/play", false);
		this.musicBot = musicBot;

		getOptionData().add(new OptionData(OptionType.STRING, "input", "A search term or link.", true));
	}

	@Override
	public void execute(CommandEvent event) {
		String message = event.getEvent().getCommandString().replace("/play input: ", "");
		musicBot.getGuildAudioManager().loadAndPlay(event, message,true);
	}
}

