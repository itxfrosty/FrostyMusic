package me.itxfrosty.frostymusic.commands.cmd;

import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.MusicManager;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class RestartCommand extends SlashCommand {
	private final MusicManager musicManager;

	public RestartCommand(final FrostyMusic frostyMusic) {
		super("restart", "Restarts the song from the beginning.", "/restart", false);
		this.musicManager = frostyMusic.getGuildAudioManager();
	}

	@Override
	public void execute(CommandEvent event, String args) {
		if ((event.getMember() == null) ||
				(event.getMember().getVoiceState() == null) ||
				!event.getMember().getVoiceState().inVoiceChannel() ||
				(event.getMember().getVoiceState().getChannel() == null)) {
			event.reply(new EmbedBuilder().setDescription("You are not in a voice channel!").build()).queue();
			return;
		}

		if (musicManager.getGuildAudio(event.getGuild()) == null || musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing!").build()).queue();
			return;
		}

		musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().restartSong();

		event.reply(new EmbedBuilder().setDescription("Restarted Song!").build()).queue();
	}
}
