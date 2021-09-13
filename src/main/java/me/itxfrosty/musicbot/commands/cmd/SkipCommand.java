package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.SlashCommand;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

public class SkipCommand extends SlashCommand {
	private final MusicManager musicManager;

	public SkipCommand(@NotNull MusicBot bot) {
		super("skip","Skip's Song and play's next one in queue.","/skip", false);
		this.musicManager = bot.getMusicManager();
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
