package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.SlashCommand;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.audio.MusicManager;
import me.itxfrosty.musicbot.audio.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Timer;
import java.util.TimerTask;

public class SkipToCommand extends SlashCommand {
	private final MusicManager musicManager;

	public SkipToCommand(final MusicBot bot) {
		super("skipto", "Skips to song index in queue", "/skipto <number>", false);
		this.musicManager = bot.getMusicManager();

		getOptionData().add(new OptionData(OptionType.STRING,"number","The position to skip the song too.",true));
	}

	@Override
	public void execute(CommandEvent event) {
		if (musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()) == null || musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().getQueueCopy().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();

			return;
		}

		String message = event.getEvent().getCommandString().replace("/skipto number: ","");

		try {
			TrackScheduler scheduler = musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler();
			int queueSize = scheduler.getQueueCopy().size();
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
				event.reply(new EmbedBuilder().setDescription("Skipped to " + musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().getQueueCopy().get(0).getInfo().title).build()).queue();
			}
		}, 1000L);

	}

}
