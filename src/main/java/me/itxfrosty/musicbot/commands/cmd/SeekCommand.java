package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.SlashCommand;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import me.itxfrosty.musicbot.managers.audio.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SeekCommand extends SlashCommand {
	private final MusicManager musicManager;

	public SeekCommand(final MusicBot musicBot) {
		super("seek", "Seek to a position in the currently playing song", "/seek <seconds>",false);
		musicManager = musicBot.getMusicManager();

		getOptionData().add(new OptionData(OptionType.STRING,"time","How far ahead to skip the song.",true));
	}

	@Override
	public void execute(CommandEvent event) {
		if (musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()) == null || musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().getQueueCopy().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}

		TrackScheduler trackScheduler = musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler();
		String message = event.getEvent().getCommandString().replace("/seek time: ","");

		try {
			trackScheduler.getQueueCopy().get(0).setPosition(Math.min(Integer.parseInt(message) * 1000L, trackScheduler.getQueueCopy().get(0).getDuration()));
		} catch (IndexOutOfBoundsException e) {
			event.reply(new EmbedBuilder().setDescription("Please specify a time to seek to!").build()).queue();
			return;
		} catch (NumberFormatException e) {
			event.reply(new EmbedBuilder().setDescription("Please specify a *number*!").build()).queue();
			return;
		}

		event.reply(new EmbedBuilder().setDescription("Seeked to " + message + " seconds on song `" + trackScheduler.getQueueCopy().get(0).getInfo().title + "`!").build()).queue();
	}
}
