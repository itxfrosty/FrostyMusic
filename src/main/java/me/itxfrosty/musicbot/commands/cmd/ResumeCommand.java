package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.SlashCommand;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.audio.MusicManager;
import me.itxfrosty.musicbot.audio.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;

public class ResumeCommand extends SlashCommand {
	private final MusicManager musicManager;

	public ResumeCommand(final MusicBot musicBot) {
		super("resume", "Resumes the player.", "/resume",false);
		musicManager = musicBot.getMusicManager();
	}

	@Override
	public void execute(CommandEvent event) {
		if (musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()) == null || musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().getQueueCopy().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}

		TrackScheduler scheduler = musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler();
		scheduler.setPaused(false);
		event.reply(new EmbedBuilder().setDescription(":arrow_forward: Unpaused the Player!").build()).queue();
	}
}
