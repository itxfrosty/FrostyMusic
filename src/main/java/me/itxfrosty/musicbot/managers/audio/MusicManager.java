package me.itxfrosty.musicbot.managers.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.Getter;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.utils.MusicUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

public class MusicManager {

	private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

	@Getter public final Map<Long, MusicSendHandler> musicGuildManager = new HashMap<>();

	public MusicManager() {
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	/**
	 * Join's Voice Chanel and set's log channel.
	 *
	 * @param voiceChannel Voice Channel.
	 * @param messageChannel Message Channel.
	 * @param guild Guild.
	 */
	public void joinVoiceChannel(VoiceChannel voiceChannel, MessageChannel messageChannel, Guild guild) {
		AudioManager audio = guild.getAudioManager();

		if (audio.getSendingHandler() == null) {
			this.musicGuildManager.putIfAbsent(guild.getIdLong(), new MusicSendHandler(this, playerManager.createPlayer(), guild));
			audio.setSendingHandler(musicGuildManager.get(guild.getIdLong()));
		}

		this.musicGuildManager.get(guild.getIdLong()).getTrackScheduler().setLogChannel(messageChannel);
		audio.openAudioConnection(voiceChannel);
	}

	/**
	 * Leave's Voice Channel.
	 *
	 * @param guild Guild
	 */
	public void leaveVoiceChannel(Guild guild) {
		guild.getAudioManager().closeAudioConnection();
		this.musicGuildManager.get(guild.getIdLong()).getTrackScheduler().clearQueue();
	}

	/**
	 * Adds track.
	 * @param user User to issued the adding of the track.
	 * @param url Load's Song.
	 * @param channel Channel to send message.
	 * @param guild Guild.
	 */
	public void addTrack(User user, String url, MessageChannel channel, Guild guild, CommandEvent event) {
		playerManager.loadItem(url, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				if (!musicGuildManager.get(guild.getIdLong()).getTrackScheduler().getTrackQueue().isEmpty()) {
					EmbedBuilder embed = new EmbedBuilder()
							.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
							.addField("Song Duration", MusicUtils.getDuration(audioTrack), true)
							.addField("Position in Queue", String.valueOf(musicGuildManager.get(guild.getIdLong()).getTrackScheduler().getTrackQueue().size()), true)
							.setFooter("Added by " + user.getAsTag(), user.getEffectiveAvatarUrl())
							.setThumbnail(MusicUtils.getThumbnail(audioTrack));

					channel.sendMessageEmbeds(new EmbedBuilder().setDescription(":white_check_mark: **" + audioTrack.getInfo().title + "** successfully added to the queue!").build()).queue();
					event.reply(embed.build()).queue();
				} else {
					event.reply(new EmbedBuilder().setDescription("Song added to queue.").build()).queue();
				}

				musicGuildManager.get(guild.getIdLong()).getTrackScheduler().queueSong(audioTrack);
			}

			@Override
			public void playlistLoaded(AudioPlaylist audioPlaylist) {
				channel.sendMessageEmbeds(new EmbedBuilder().setDescription("Loading playlist `" + audioPlaylist.getName() + "`").build()).queue();
				for (AudioTrack track : audioPlaylist.getTracks()) {
					musicGuildManager.get(guild.getIdLong()).getTrackScheduler().queueSong(track);
				}
			}

			@Override
			public void noMatches() {
				channel.sendMessageEmbeds(new EmbedBuilder().setDescription("Could not find song!").build()).queue();
			}

			@Override
			public void loadFailed(FriendlyException e) {
				channel.sendMessageEmbeds(new EmbedBuilder().setDescription("An error occurred!").build()).queue();
			}
		});
	}

	/**
	 * Adds track and loads it.
	 *
	 * @param name Name of Song.
	 * @param guild Guild.
	 * @param scheduler TrackScheduler
	 * @param queueSong If song need's to be queued.
	 */
	public void addTrack(String name, Guild guild, TrackScheduler scheduler, boolean queueSong) {
		playerManager.loadItem(name, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				if (queueSong) {
					scheduler.queueSong(audioTrack);
				}
				scheduler.getTrackQueue().add(0, audioTrack);
				scheduler.getAudioPlayer().playTrack(scheduler.getTrackQueue().get(0));
			}

			@Override
			public void playlistLoaded(AudioPlaylist audioPlaylist) {
				for (AudioTrack track : audioPlaylist.getTracks()) {
					musicGuildManager.get(guild.getIdLong()).getTrackScheduler().queueSong(track);
				}
			}

			@Override
			public void noMatches() {}

			@Override
			public void loadFailed(FriendlyException e) {}
		});
	}
}