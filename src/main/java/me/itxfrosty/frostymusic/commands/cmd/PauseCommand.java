package me.itxfrosty.frostymusic.commands.cmd;

import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.TrackScheduler;
import me.itxfrosty.frostymusic.audio.guild.GuildAudioManager;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class PauseCommand extends SlashCommand {
	private final GuildAudioManager musicManager;

	public PauseCommand(final FrostyMusic musicBot) {
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