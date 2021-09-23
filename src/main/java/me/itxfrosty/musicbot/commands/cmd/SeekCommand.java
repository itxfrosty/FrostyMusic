package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.audio.TrackScheduler;
import me.itxfrosty.musicbot.audio.guild.GuildAudioManager;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SeekCommand extends SlashCommand {
	private final GuildAudioManager musicManager;

	public SeekCommand(final MusicBot musicBot) {
		super("seek", "Seek to a position in the currently playing song", "/seek <seconds>",false);
		musicManager = musicBot.getGuildAudioManager();

		getOptionData().add(new OptionData(OptionType.STRING,"time","How far ahead to skip the song.",true));
	}

	@Override
	public void execute(CommandEvent event) {
		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}

		TrackScheduler trackScheduler = musicManager.getGuildAudio(event.getGuild()).getTrackScheduler();
		String message = event.getEvent().getCommandString().replace("/seek time: ","");

		try {
			trackScheduler.getTrackQueue().get(0).setPosition(Math.min(Integer.parseInt(message) * 1000L, trackScheduler.getTrackQueue().get(0).getDuration()));
		} catch (IndexOutOfBoundsException e) {
			event.reply(new EmbedBuilder().setDescription("Please specify a time to seek to!").build()).queue();
			return;
		} catch (NumberFormatException e) {
			event.reply(new EmbedBuilder().setDescription("Please specify a *number*!").build()).queue();
			return;
		}

		event.reply(new EmbedBuilder().setDescription("Seeked to " + message + " seconds on song `" + trackScheduler.getTrackQueue().get(0).getInfo().title + "`!").build()).queue();
	}
}
