package me.itxfrosty.musicbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import lombok.Getter;
import lombok.var;
import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.audio.sources.SpotifySource;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.objects.enums.RequestType;
import me.itxfrosty.musicbot.objects.enums.SpotifyType;
import me.itxfrosty.musicbot.objects.enums.YoutubeType;
import me.itxfrosty.musicbot.utils.MusicUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.hc.core5.http.ParseException;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MusicManager {
	private final MusicBot bot;

	private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

	@Getter public final Map<Long, MusicSendHandler> musicGuildManager = new HashMap<>();

	public MusicManager(MusicBot musicBot) {
		this.bot = musicBot;

		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);

		playerManager.registerSourceManager(new YoutubeAudioSourceManager());
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
	public void addTrack(User user, String url, MessageChannel channel, Guild guild, boolean message , CommandEvent event) {
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

					if (message) {
						channel.sendMessageEmbeds(new EmbedBuilder().setDescription(":white_check_mark: **" + audioTrack.getInfo().title + "** successfully added to the queue!").build()).queue();
						event.reply(embed.build()).queue();
					}
				} else {
					if (message) {
						event.reply(new EmbedBuilder().setDescription("Song added to queue.").build()).queue();
					}

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
	 * @param queueSong If song need's to be queued.
	 */
	public void addTrack(String name, Guild guild, boolean queueSong) {
		playerManager.loadItem(name, new AudioLoadResultHandler() {
			final TrackScheduler scheduler = getMusicGuildManager().get(guild.getIdLong()).getTrackScheduler();

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

	public void playSong(String message, CommandEvent event) {
		final RequestType requestType = Objects.requireNonNull(RequestType.getRequestType(message));

		switch (requestType) {
			case YOUTUBE: {
				YoutubeType youtubeType = Objects.requireNonNull(YoutubeType.getYouTubeType(message));

				switch (youtubeType) {
					case TRACK: {
						String url;

						try {
							url = new URL(message).toString();

							if (url != null) {
								bot.getMusicManager().addTrack(event.getUser(), url, event.getChannel(), event.getGuild(),true,  event);
								bot.getMusicManager().getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().setPaused(false);
							} else {
								EmbedBuilder embed = new EmbedBuilder();
								embed.setColor(Color.RED);
								embed.setDescription("You have reached the maximum quota for today!");
								event.reply(embed.build()).queue();
							}
						} catch (MalformedURLException e) {
							event.reply(new EmbedBuilder().setDescription("An error occurred!").build()).queue();
						}

						return;
					}

					case PLAYLIST: {
						try {
							java.util.List<String> playlist = bot.getYoutubeSource().getPlaylistItemsByLink(message);

							for (String song : playlist) {
								bot.getMusicManager().addTrack(
										song, event.getGuild(),
										!bot.getMusicManager().getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().getTrackQueue().isEmpty());
							}

							event.reply(new EmbedBuilder().setDescription("The playlist was added to the queue!").build()).queue();
						} catch (IOException e) {
							e.printStackTrace();
						}

						return;
					}
				}
			}

			case SPOTIFY: {
				SpotifyType spotifyType = Objects.requireNonNull(SpotifyType.getSpotifyType(message));

				switch (spotifyType) {
					case TRACK: {
						// Track

						if (message.contains("open.spotify.com/track/")) {
							message = message.replace("https://", "");
							message = message.split("/")[2];
							message = message.split("\\?")[0];
						} else if (message.startsWith("spotify:track:")) {
							message = message.split(":")[2];
						}

						try {
							SpotifySource spotify = bot.getSpotifySource();

							String search = spotify.getTrackArtists(message) + " " + spotify.getTrackName(message);
							searchForSong(search,true, event);

						} catch (IOException | ParseException | SpotifyWebApiException e) {
							e.printStackTrace();
						}
					}

					case PLAYLIST: {

						try {
							String id = null;
							if (message.startsWith("spotify:playlist:")) {
								id = message.split(":")[2];
							}
							if (message.startsWith("open.spotify.com/playlist/")) {
								var temp = message.split("/")[2];
								id = temp.split("\\?")[0];
							}
							if (message.startsWith("https://open.spotify.com/playlist/")) {
								var temp = message.split("/")[4];
								id = temp.split("\\?")[0];
							}

							event.reply(new EmbedBuilder().setDescription("Loading playlist `" + bot.getSpotifySource().getPlayListName(id) + "`").build()).queue();

							List<String> playlist = bot.getSpotifySource().getPlaylist(id);
							for (String link : playlist) {
								searchForSong(link,false, event);
							}

							event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription("Loaded playlist `" + bot.getSpotifySource().getPlayListName(id) + "`").build()).queue();

						} catch (IOException | ParseException | SpotifyWebApiException e) {
							e.printStackTrace();
						}

					}

					case ALBUM: {
						// Album

						System.out.println("Album: Not Today");
					}

					case ARTIST: {
						// Artist

						System.out.println("Artist: Not Today");
					}
				}
			}

			case SEARCH: {
				searchForSong(message,true, event);
				return;
			}
		}
	}

	private void searchForSong(String message, boolean mess, CommandEvent event) {
		String search_song = bot.getYoutubeSource().search(message);

		if (search_song != null) {
			bot.getMusicManager().addTrack(event.getUser(), search_song, event.getChannel(), event.getGuild(), mess, event);
			bot.getMusicManager().getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().setPaused(false);
		} else {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.RED);
			embed.setDescription(":x: You have reached the maximum quota for today!");
			event.reply(embed.build()).queue();
		}
	}


}