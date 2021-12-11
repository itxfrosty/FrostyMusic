package me.itxfrosty.frostymusic.commands.cmd;

import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.TrackScheduler;
import me.itxfrosty.frostymusic.audio.MusicManager;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Timer;
import java.util.TimerTask;

public class SkipToCommand extends SlashCommand {
	private final MusicManager musicManager;

	public SkipToCommand(final FrostyMusic musicBot) {
		super("skipto", "Skips to song index in queue", "/skipto <number>", false);
		this.musicManager = musicBot.getGuildAudioManager();

		this.addOption(new OptionData(OptionType.STRING,"number","The position to skip the song too.",true));
	}

	@Override
	public void execute(CommandEvent event, String args) {
		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();

			return;
		}

		String message = event.getEvent().getCommandString().replace("/skipto number: ","");

		try {
			TrackScheduler scheduler = musicManager.getGuildAudio(event.getGuild()).getTrackScheduler();
			int queueSize = scheduler.getTrackQueue().size();
			if (Integer.parseInt(message) >= queueSize || Integer.parseInt(message) <= 0) {
				event.reply(new EmbedBuilder().setDescription("That is not a valid track number!").build()).queue();
				return;
			}
			scheduler.skipTo(Math.min(Integer.parseInt(message), queueSize));
		} catch (IndexOutOfBoundsException e) {
			event.reply(new EmbedBuilder().setDescription("Please specify a position to skip to!").build()).queue();
			return;
		} catch (NumberFormatException e) {
			event.reply(new EmbedBuilder().setDescription("That is not a number!").build()).queue();
			return;
		}

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				event.reply(new EmbedBuilder().setDescription("Skipped to " + musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().get(0).getInfo().title).build()).queue();
			}
		}, 1000L);

	}

}
