package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.SlashCommand;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import me.itxfrosty.musicbot.managers.audio.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;

public class PauseCommand extends SlashCommand {
	private final MusicManager musicManager;

	public PauseCommand(final MusicBot bot) {
		super("pause", "Pauses the player", "/pause",false );
		this.musicManager = bot.getMusicManager();
	}

	@Override
	public void execute(CommandEvent event) {
		if (musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()) == null || musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().getQueueCopy().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}
		TrackScheduler scheduler = musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler();
		scheduler.setPaused(true);
		event.reply(new EmbedBuilder().setDescription(":pause_button: Paused the Player!").build()).queue();
	}
}