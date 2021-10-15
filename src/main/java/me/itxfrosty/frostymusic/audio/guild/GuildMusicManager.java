package me.itxfrosty.frostymusic.audio.guild;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import me.itxfrosty.frostymusic.audio.AudioSendProvider;
import me.itxfrosty.frostymusic.audio.TrackScheduler;
import net.dv8tion.jda.api.entities.Guild;

public class GuildMusicManager {
	private final AudioPlayer audioPlayer;
	private final TrackScheduler trackScheduler;
	private final AudioSendProvider audioSendProvider;

	public GuildMusicManager(AudioPlayerManager playerManager, GuildAudioManager guildAudioManager, Guild guild) {
		this.audioPlayer = playerManager.createPlayer();

		this.trackScheduler = new TrackScheduler(this.audioPlayer, guildAudioManager, guild);
		this.audioSendProvider = new AudioSendProvider(audioPlayer);

		this.audioPlayer.addListener(this.trackScheduler);
	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public TrackScheduler getTrackScheduler() {
		return trackScheduler;
	}

	public AudioSendProvider getAudioSendProvider() {
		return audioSendProvider;
	}
}
