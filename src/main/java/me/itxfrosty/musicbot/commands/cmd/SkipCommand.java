package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.Command;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public class SkipCommand extends Command {
	private final MusicManager musicManager;

	public SkipCommand(@NotNull MusicBot bot) {
		super("skip","Skip's Song.","/skip", false);
		this.musicManager = bot.getMusicManager();

		setCommandData(new CommandData("skip","Skip's Song and play's next one in queue."));
	}

	@Override
	public void execute(CommandEvent event) {
		if (event.getMember() == null || event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel() || event.getMember().getVoiceState().getChannel() == null) {
			event.reply(new EmbedBuilder().setDescription("Skipped to the next song!").build()).queue();
			return;
		}

		if (musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()) == null) {
			event.reply(new EmbedBuilder().setDescription("Please use `!join` first!").build()).queue();
			return;
		}

		if (musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().getQueueCopy().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}

		event.reply(new EmbedBuilder().setDescription("Skipped to the next song.").build()).queue();

		musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().skip();
	}
}