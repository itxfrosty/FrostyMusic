package me.itxfrosty.musicbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.Setter;
import me.itxfrosty.musicbot.utils.MusicUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackScheduler extends AudioEventAdapter {

	private final Guild guild;
	@Getter private final AudioPlayer audioPlayer;
	@Getter private final MusicManager musicManager;

	private boolean loop = false;
	private boolean loopQueue = false;

	@Getter @Setter private MessageChannel logChannel;

	@Getter private final List<AudioTrack> trackQueue = new ArrayList<>();

	public TrackScheduler(final MusicManager musicManager, AudioPlayer player, Guild guild) {
		this.musicManager = musicManager;
		this.audioPlayer = player;
		this.guild = guild;
	}

	/**
	 * Queues Song.
	 *
	 * @param track Track to Queue.
	 */
	public void queueSong(AudioTrack track) {
		trackQueue.add(track);
		if (audioPlayer.getPlayingTrack() == null) audioPlayer.playTrack(trackQueue.get(0));
	}

	/**
	 * Clear's Audio Queue.
	 */
	public void clearQueue() {
		trackQueue.clear();
		audioPlayer.stopTrack();
	}

	/**
	 * Skips Song.
	 */
	public void skip() {
		audioPlayer.getPlayingTrack().setPosition(audioPlayer.getPlayingTrack().getDuration());
	}

	/**
	 * Skip's to certain position in queue.
	 *
	 * @param pos Position to set song.
	 */
	public void skipTo(int pos) {
		if (pos > 1) {
			trackQueue.subList(1, pos).clear();
		}

		skip();
	}

	/**
	 * Toggles loop for singular song.
	 *
	 * @param channel Message Channel to send message.
	 */
	public void toggleLoop(@Nullable MessageChannel channel) {
		if (loopQueue && !loop) toggleLoopQueue(channel);
		if (!loop) {
			loop = true;
			if (channel != null) channel.sendMessageEmbeds(new EmbedBuilder().setDescription(":repeat_one: Loop Enabled!").build()).queue();
		} else {
			loop = false;
			if (channel != null) channel.sendMessageEmbeds(new EmbedBuilder().setDescription(":x: Loop Disabled!").build()).queue();
		}
	}

	/**
	 * Toggles Loop for queue of song.
	 *
	 * @param channel Message Channel to send message.
	 */
	public void toggleLoopQueue(@Nullable MessageChannel channel) {
		if (loop && !loopQueue) toggleLoop(channel);
		if (!loopQueue) {
			loopQueue = true;
			if (channel != null) channel.sendMessageEmbeds(new EmbedBuilder().setDescription(":repeat: Loop Queue Enabled!").build()).queue();
		} else {
			loopQueue = false;
			if (channel != null) channel.sendMessageEmbeds(new EmbedBuilder().setDescription(":x: Loop Queue Disabled!").build()).queue();
		}

	}

	/**
	 * Check's if Audio is paused.
	 *
	 * @return If Paused.
	 */
	public boolean isPaused() {
		return audioPlayer.isPaused();
	}

	/**
	 * Set's the Audio to paused parameter.
	 *
	 * @param paused Pause or not.
	 */
	public void setPaused(boolean paused) {
		audioPlayer.setPaused(paused);
	}

	/**
	 * Set's Volume of Audio Player.
	 *
	 * @param volume Volume to set Audio player.
	 */
	public void setVolume(int volume) {
		audioPlayer.setVolume(volume);
	}

	/**
	 * Shuffles queue of songs.
	 */
	public void shuffle() {
		Collections.shuffle(trackQueue.subList(1, trackQueue.size()));
	}

	/**
	 * Gets copy of Current Queue.
	 *
	 * @return List of AudioTracks.
	 */
	public List<AudioTrack> getQueueCopy() {
		return new ArrayList<>(trackQueue);
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Now Playing")
				.setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")")
				.addField("Song Duration", MusicUtils.getDuration(track), true)
				.setThumbnail(MusicUtils.getThumbnail(track));
		builder.addField("Up Next", (trackQueue.size() > 1) ? ("[" + trackQueue.get(1).getInfo().title + "](" + trackQueue.get(1).getInfo().uri + ")") : "Nothing", true);

		logChannel.sendMessageEmbeds(builder.build()).queue();
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		trackQueue.remove(track);
		if (loop && endReason.mayStartNext) {
			musicManager.addTrack(track.getInfo().uri, guild, false);
		} else if (loopQueue && endReason.mayStartNext) {
			musicManager.addTrack(track.getInfo().uri, guild, true);
		} else if (endReason.mayStartNext && trackQueue.size() > 0) player.playTrack(trackQueue.get(0));
	}
}