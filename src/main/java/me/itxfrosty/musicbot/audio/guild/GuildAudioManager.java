package me.itxfrosty.musicbot.audio.guild;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.getyarn.GetyarnAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import me.itxfrosty.musicbot.audio.TrackScheduler;
import me.itxfrosty.musicbot.audio.handlers.AudioSoundLoadHandler;
import me.itxfrosty.musicbot.audio.sources.SpotifySource;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.factories.SearchFactory;
import me.itxfrosty.musicbot.objects.RequestType;
import me.itxfrosty.musicbot.objects.SpotifyType;
import me.itxfrosty.musicbot.objects.YoutubeType;
import me.itxfrosty.musicbot.utils.MusicUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GuildAudioManager {
	private final Logger logger = LoggerFactory.getLogger(GuildAudioManager.class);

	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> guildMusicManager;

	public GuildAudioManager() {
		this.logger.info("Loading AudioManager...");
		this.guildMusicManager = new HashMap<>();
		this.playerManager = new DefaultAudioPlayerManager();

		this.playerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
		this.playerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
		this.playerManager.getConfiguration().setFilterHotSwapEnabled(true);

		AudioSourceManagers.registerRemoteSources(this.playerManager);
		AudioSourceManagers.registerLocalSource(this.playerManager);

		this.playerManager.registerSourceManager(new BeamAudioSourceManager());
		this.playerManager.registerSourceManager(new HttpAudioSourceManager());
		this.playerManager.registerSourceManager(new VimeoAudioSourceManager());
		this.playerManager.registerSourceManager(new GetyarnAudioSourceManager());
		this.playerManager.registerSourceManager(new YoutubeAudioSourceManager());
		this.playerManager.registerSourceManager(new BandcampAudioSourceManager());
		this.playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
		this.playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());

		this.logger.info("Loaded AudioManager!");
	}

	/**
	 * Get's Guild Audio Manger.
	 *
	 * @param guild Guild to get AudioManager.
	 * @return GuildMusicManager.
	 */
	public synchronized GuildMusicManager getGuildAudio(final Guild guild) {
		return this.guildMusicManager.computeIfAbsent(guild.getIdLong(), (guildAudio) -> {
			final GuildMusicManager guildMusicManager = new GuildMusicManager(this.playerManager, this, guild);

			guild.getAudioManager().setSendingHandler(guildMusicManager.getAudioSendProvider());

			return guildMusicManager;
		});
	}

	/**
	 * Join's Voice Chanel and set's log channel.
	 *
	 * @param voiceChannel   Voice Channel.
	 * @param messageChannel Message Channel.
	 * @param guild          Guild.
	 */
	public void joinVoiceChannel(final VoiceChannel voiceChannel, final TextChannel messageChannel, final Guild guild) {
		final AudioManager audioManager = guild.getAudioManager();
		final GuildMusicManager guildMusicManager = getGuildAudio(guild);

		guildMusicManager.getTrackScheduler().setLogChannel(messageChannel);
		audioManager.openAudioConnection(voiceChannel);
	}

	/**
	 * Leave's Voice Channel.
	 *
	 * @param guild Guild
	 */
	public void leaveVoiceChannel(final Guild guild) {
		guild.getAudioManager().closeAudioConnection();
		this.getGuildAudio(guild).getTrackScheduler().clearQueue();
	}

	/**
	 * Loads and plays a track by it's URL.
	 *
	 * @param event       CommandEvent for getting details.
	 * @param trackURL    The URL of the track you want to play.
	 * @param sendMessage Sends message if true.
	 */
	public void loadAndPlay(final CommandEvent event, final String trackURL, final boolean sendMessage) {
		final GuildMusicManager musicManager = getGuildAudio(event.getGuild());
		final Member member = event.getMember();

		if (member == null || member.getVoiceState() == null || !member.getVoiceState().inVoiceChannel() || member.getVoiceState().getChannel() == null) {
			event.reply(new EmbedBuilder().setDescription("Please connect to a voice channel first!").build()).queue();
			return;
		}

		this.joinVoiceChannel(member.getVoiceState().getChannel(), event.getChannel(), event.getGuild());

		final TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		final String finalTrackURL = new SearchFactory(trackURL, event , trackScheduler, playerManager).search();

		if (finalTrackURL == null) {
			event.reply(new EmbedBuilder().setDescription("An error occurred!").build()).queue();
			return;
		}

		/* Spotify Playlist */
		if (finalTrackURL.startsWith("Spotify.PLAYLIST ")) {
			event.reply(new EmbedBuilder().setDescription("Loading playlist `" + finalTrackURL.replace("Spotify.PLAYLIST ","") + "`").build()).queue();
			return;
		}

		/* Spotify Album */
		if (finalTrackURL.startsWith("Spotify.ALBUM ")) {
			event.reply(new EmbedBuilder().setDescription("Loading album `" + finalTrackURL.replace("Spotify.ALBUM ","") + "`").build()).queue();
			return;
		}

		playerManager.loadItem(finalTrackURL, new AudioSoundLoadHandler(this.logger, member, event, sendMessage, trackScheduler, finalTrackURL));
	}

	/**
	 * Adds track and loads it.
	 *
	 * @param name Name of Song.
	 * @param guild Guild.
	 * @param queueSong If song need's to be queued.
	 */
	public void addTrack(final String name, final Guild guild, final boolean queueSong) {
		playerManager.loadItem(name, new AudioLoadResultHandler() {
			final TrackScheduler scheduler = getGuildAudio(guild).getTrackScheduler();

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
					scheduler.queueSong(track);
				}
			}

			@Override
			public void noMatches() {}

			@Override
			public void loadFailed(FriendlyException ignore) {}
		});
	}

	/**
	 * Get's Song and then make's a search link.
	 *
	 * @param song Song to search for.
	 * @param event Replies to messages.
	 * @return Song name in correct format: ytsearch: [Song Name]
	 */
	private String searchForSong(final String song, final CommandEvent event) {
		final RequestType requestType = Objects.requireNonNull(RequestType.getRequestType(song));
		try {

			/* Request Type: Youtube URL */
			if (requestType == RequestType.YOUTUBE) {
				final YoutubeType youtubeType = YoutubeType.getYouTubeType(song);

				if (youtubeType == YoutubeType.TRACK) {
					return song;
				}

				if (youtubeType == YoutubeType.PLAYLIST) {
					// TODO: Playlist loading.

				}

				return song;
			}

			/* Request Type: Search */
			if (requestType == RequestType.SEARCH) {
				return "ytsearch: " + song;
			}

			if (requestType == RequestType.SPOTIFY) {
				final SpotifyType spotifyType = Objects.requireNonNull(SpotifyType.getSpotifyType(song));
				final SpotifySource spotifySource = new SpotifySource();

				if (spotifyType == SpotifyType.TRACK) {

					return "ytsearch: " + spotifySource.getTrackNameAndArtist(song);
				}

				if (spotifyType == SpotifyType.PLAYLIST) {

					String id = null;
					if (song.startsWith("spotify:playlist:")) {
						id = song.split(":")[2];
					}
					if (song.startsWith("open.spotify.com/playlist/")) {
						String temp = song.split("/")[2];
						id = temp.split("\\?")[0];
					}
					if (song.startsWith("https://open.spotify.com/playlist/")) {
						String temp = song.split("/")[4];
						id = temp.split("\\?")[0];
					}

					String playlistName = spotifySource.getPlayListName(id);

					logger.info("Loading Playlist: " + playlistName);

					for (String load : spotifySource.getPlaylistByID(id)) {
						loadAndPlay(event, load, false);
					}

					return "Spotify.PLAYLIST " + playlistName;
				}

				if (spotifyType == SpotifyType.ALBUM) {

					String id = null;
					if (song.startsWith("spotify:album:")) {
						id = song.split(":")[2];
					}
					if (song.startsWith("open.spotify.com/album/")) {
						String temp = song.split("/")[2];
						id = temp.split("\\?")[0];
					}
					if (song.startsWith("https://open.spotify.com/album/")) {
						String temp = song.split("/")[4];
						id = temp.split("\\?")[0];
					}

					String albumName = spotifySource.getAlbumName(id);

					logger.info("Loading Album: " + albumName);

					for (String load : spotifySource.getAlbum(id)) {
						loadAndPlay(event, load, false);
					}

					return "Spotify.ALBUM " + albumName;
				}
			}

			return "ytsearch: " + song;
		} catch (IOException | ParseException | SpotifyWebApiException e) {
			e.printStackTrace();
		}

		return null;
	}
}