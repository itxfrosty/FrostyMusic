package me.itxfrosty.musicbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackScheduler extends AudioEventAdapter {

	private final Guild guild;
	private final AudioPlayer audioPlayer;

	private boolean loop = false;
	private boolean loopQueue = false;

	private TextChannel logChannel;

	private final List<AudioTrack> trackQueue = new ArrayList<>();

	public TrackScheduler(AudioPlayer audioPlayer, Guild guild) {
		this.guild = guild;
		this.audioPlayer = audioPlayer;
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
	 * Gets copy of Current Queue.
	 *
	 * @return List of AudioTracks.
	 */
	public List<AudioTrack> getTrackQueue() {
		return new ArrayList<>(trackQueue);
	}

	/**
	 * Sets Log Channel to specified MessageChannel
	 *
	 * @param logChannel MessageChannel.
	 */
	public void setLogChannel(TextChannel logChannel) {
		this.logChannel = logChannel;
	}

	/**
	 * Gets Log Channel.
	 *
	 * @return MessageChannel
	 */
	public TextChannel getLogChannel() {
		return logChannel;
	}

}