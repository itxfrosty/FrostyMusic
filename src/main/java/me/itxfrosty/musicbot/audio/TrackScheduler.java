package me.itxfrosty.musicbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.itxfrosty.musicbot.audio.guild.GuildAudioManager;
import me.itxfrosty.musicbot.utils.MusicUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackScheduler extends AudioEventAdapter {

	private final Guild guild;
	private final AudioPlayer audioPlayer;
	private final GuildAudioManager guildAudioManager;

	private boolean loop = false;
	private boolean loopQueue = false;

	private TextChannel logChannel;

	private final List<AudioTrack> trackQueue = new ArrayList<>();

	public TrackScheduler(AudioPlayer audioPlayer, GuildAudioManager guildAudioManager, Guild guild) {
		this.guild = guild;
		this.audioPlayer = audioPlayer;
		this.guildAudioManager = guildAudioManager;
	}

	/**
	 * Queues Song.
	 *
	 * @param track Track to Queue.
	 */
	public void queueSong(AudioTrack track) {
		this.trackQueue.add(track);
		if (audioPlayer.getPlayingTrack() == null) audioPlayer.playTrack(trackQueue.get(0));
	}

	/**
	 * Clear's Audio Queue.
	 */
	public void clearQueue() {
		this.trackQueue.clear();
		this.audioPlayer.stopTrack();
	}

	/**
	 * Skips Song.
	 */
	public void skip() {
		this.audioPlayer.getPlayingTrack().setPosition(this.audioPlayer.getPlayingTrack().getDuration());
	}

	/**
	 * Skip's to certain position in queue.
	 *
	 * @param pos Position to set song.
	 */
	public void skipTo(int pos) {
		if (pos > 1) {
			this.trackQueue.subList(1, pos).clear();
		}

		this.skip();
	}

	/**
	 * Check's if Audio is paused.
	 *
	 * @return If Paused.
	 */
	public boolean isPaused() {
		return this.audioPlayer.isPaused();
	}

	/**
	 * Set's the Audio to paused parameter.
	 *
	 * @param paused Pause or not.
	 */
	public void setPaused(boolean paused) {
		this.audioPlayer.setPaused(paused);
	}

	/**
	 * Set's Volume of Audio Player.
	 *
	 * @param volume Volume to set Audio player.
	 */
	public void setVolume(int volume) {
		this.audioPlayer.setVolume(volume);
	}

	/**
	 * Shuffles queue of songs.
	 */
	public void shuffle() {
		Collections.shuffle(this.trackQueue.subList(1, this.trackQueue.size()));
	}

	/**
	 * Toggles loop for singular song.
	 *
	 * @param channel Message Channel to send message.
	 */
	public void toggleLoop(@Nullable MessageChannel channel) {
		if (this.loopQueue && !this.loop) toggleLoopQueue(channel);
		if (!this.loop) {
			this.loop = true;
			if (channel != null) channel.sendMessageEmbeds(new EmbedBuilder().setDescription(":repeat_one: Loop Enabled!").build()).queue();
		} else {
			this.loop = false;
			if (channel != null) channel.sendMessageEmbeds(new EmbedBuilder().setDescription(":x: Loop Disabled!").build()).queue();
		}
	}

	/**
	 * Toggles Loop for queue of song.
	 *
	 * @param channel Message Channel to send message.
	 */
	public void toggleLoopQueue(@Nullable MessageChannel channel) {
		if (this.loop && !this.loopQueue) toggleLoop(channel);
		if (!this.loopQueue) {
			this.loopQueue = true;
			if (channel != null) channel.sendMessageEmbeds(new EmbedBuilder().setDescription(":repeat: Loop Queue Enabled!").build()).queue();
		} else {
			this.loopQueue = false;
			if (channel != null) channel.sendMessageEmbeds(new EmbedBuilder().setDescription(":x: Loop Queue Disabled!").build()).queue();
		}

	}

	/**
	 * Clear's Queue.
	 */
	public void resetQueue() {
		this.trackQueue.clear();
	}

	/**
	 * Gets copy of Current Queue.
	 *
	 * @return List of AudioTracks.
	 */
	public List<AudioTrack> getTrackQueue() {
		return this.trackQueue;
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
		return this.logChannel;
	}


	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Now Playing")
				.setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")")
				.addField("Song Duration", MusicUtils.getDuration(track), true)
				.setThumbnail(MusicUtils.getThumbnail(track));
		builder.addField("Up Next", (this.trackQueue.size() > 1) ? ("[" + this.trackQueue.get(1).getInfo().title + "](" + this.trackQueue.get(1).getInfo().uri + ")") : "Nothing", true);
		this.logChannel.sendMessageEmbeds(builder.build()).queue((message -> message.delete().queueAfter(track.getDuration(), TimeUnit.MILLISECONDS)));
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		this.trackQueue.remove(track);
		if (this.loop && endReason.mayStartNext) {
			this.guildAudioManager.addTrack(track.getInfo().uri,this. guild, false);
		} else if (this.loopQueue && endReason.mayStartNext) {
			this.guildAudioManager.addTrack(track.getInfo().uri, this.guild, true);
		} else if (endReason.mayStartNext && this.trackQueue.size() > 0) player.playTrack(this.trackQueue.get(0));
	}

	public AudioPlayer getAudioPlayer() {
		return this.audioPlayer;
	}
}