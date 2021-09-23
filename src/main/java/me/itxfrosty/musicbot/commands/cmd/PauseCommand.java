package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.audio.TrackScheduler;
import me.itxfrosty.musicbot.audio.guild.GuildAudioManager;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class PauseCommand extends SlashCommand {
	private final GuildAudioManager musicManager;

	public PauseCommand(final MusicBot musicBot) {
		super("pause", "Pauses the player", "/pause",false );
		this.musicManager = musicBot.getGuildAudioManager();
	}

	@Override
	public void execute(CommandEvent event) {
		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}
		TrackScheduler scheduler = musicManager.getGuildAudio(event.getGuild()).getTrackScheduler();
		scheduler.setPaused(true);
		event.reply(new EmbedBuilder().setDescription(":pause_button: Paused the Player!").build()).queue();
	}
}