package me.itxfrosty.frostymusic.factories;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.TrackScheduler;
import me.itxfrosty.frostymusic.audio.handlers.AudioSoundLoadHandler;
import me.itxfrosty.frostymusic.audio.sources.SpotifySource;
import me.itxfrosty.frostymusic.audio.sources.applemusic.AppleMusicAPI;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.Track;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.album.Album;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.objects.AppleMusicType;
import me.itxfrosty.frostymusic.objects.RequestType;
import me.itxfrosty.frostymusic.objects.SpotifyType;
import me.itxfrosty.frostymusic.objects.YoutubeType;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

public class SearchFactory {
	private final Logger logger = LoggerFactory.getLogger(SearchFactory.class);

	private final String search;
	private final CommandEvent event;
	private final TrackScheduler trackScheduler;
	private final AudioPlayerManager audioPlayerManager;

	private final FrostyMusic frostyMusic;

	/**
	 * SearchFactory Constructor.
	 *
	 * @param search Song/URL to search for.
	 */
	public SearchFactory(String search, CommandEvent event, TrackScheduler trackScheduler, AudioPlayerManager audioPlayerManager, FrostyMusic frostyMusic) {
		this.search = search;
		this.event = event;
		this.trackScheduler = trackScheduler;
		this.audioPlayerManager = audioPlayerManager;

		this.frostyMusic = frostyMusic;
	}

	public String search() {
		final RequestType requestType = Objects.requireNonNull(RequestType.getRequestType(search));

		try {
			switch (requestType) {
				case YOUTUBE: {
					final YoutubeType youtubeType = YoutubeType.getYouTubeType(search);

					/* Searches for song using URL */
					if (youtubeType == YoutubeType.TRACK) {
						return search;
					}

					return search;
				}

				case SPOTIFY: {
					final SpotifyType spotifyType = Objects.requireNonNull(SpotifyType.getSpotifyType(search));
					final SpotifySource spotifySource = new SpotifySource();

					/* Search for song using youtube by using name. */
					if (spotifyType == SpotifyType.TRACK) {
						return "ytsearch: " + spotifySource.getTrackNameAndArtist(search);
					}

					/* Searches for playlist and then add all of the song's to the queue. */
					if (spotifyType == SpotifyType.PLAYLIST) {
						String id = null;
						if (search.startsWith("spotify:playlist:")) {
							id = search.split(":")[2];
						}
						if (search.startsWith("open.spotify.com/playlist/")) {
							String temp = search.split("/")[2];
							id = temp.split("\\?")[0];
						}
						if (search.startsWith("https://open.spotify.com/playlist/")) {
							String temp = search.split("/")[4];
							id = temp.split("\\?")[0];
						}

						String playlistName = spotifySource.getPlayListName(id);

						this.logger.info("Loading Playlist: " + playlistName);

						for (String song : spotifySource.getPlaylistByID(id)) {
							audioPlayerManager.loadItem("ytsearch: " + song, new AudioSoundLoadHandler(logger, event.getMember(), event, false, trackScheduler,"ytsearch: " + song));
						}

						return "Spotify.PLAYLIST " + playlistName;
					}

					/* Searches for album and then add all of the song's to the queue. */
					if (spotifyType == SpotifyType.ALBUM) {

						String id = null;
						if (search.startsWith("spotify:album:")) {
							id = search.split(":")[2];
						}
						if (search.startsWith("open.spotify.com/album/")) {
							String temp = search.split("/")[2];
							id = temp.split("\\?")[0];
						}
						if (search.startsWith("https://open.spotify.com/album/")) {
							String temp = search.split("/")[4];
							id = temp.split("\\?")[0];
						}

						String albumName = spotifySource.getAlbumName(id);

						this.logger.info("Loading Album: " + albumName);

						for (String song : spotifySource.getAlbum(id)) {
							audioPlayerManager.loadItem("ytsearch: " + song, new AudioSoundLoadHandler(logger, event.getMember(), event, false, trackScheduler,"ytsearch: " + song));
						}

						return "Spotify.ALBUM " + albumName;
					}
				}

				case APPLE_MUSIC: {
					final AppleMusicType appleMusicType = AppleMusicType.getAppleMusicType(search);
					final AppleMusicAPI api = this.frostyMusic.getAppleMusicAPI();

					if (appleMusicType == AppleMusicType.ALBUM)  {

						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append(search);
						stringBuilder.substring(stringBuilder.lastIndexOf("/"), stringBuilder.lastIndexOf(".")).replaceAll("/", "");

						final String[] albumName = new String[1];
						albumName[0] = null;

						api.getAlbum("", stringBuilder.toString(), new Callback<>() {
							@Override
							public void onResponse(@NotNull Call<Album> call, @NotNull Response<Album> response) {
								assert response.body() != null;
								albumName[0] = response.body().getAlbum().getAttributes().getName();
								logger.info("Loading Album: " +  albumName[0]);

								for (Track track : response.body().getAlbum().getRelationships().getTracks().getData()) {
									final String song = track.getAttributes().getName() + " " + track.getAttributes().getArtistName();
									audioPlayerManager.loadItem("ytsearch: " + song, new AudioSoundLoadHandler(logger, event.getMember(), event, false, trackScheduler,"ytsearch: " + song));
								}
							}

							@Override
							public void onFailure(@NotNull Call<Album> call, @NotNull Throwable throwable) {
								event.reply(new EmbedBuilder().setDescription("Could not find album!").build()).queue();
								call.cancel();
							}
						});

						return "AppleMusic.ALBUM" + albumName[0];
					}
				}

				case SEARCH: {
					return "ytsearch: " + search;
				}

			}
		} catch (IOException | ParseException | SpotifyWebApiException e) {
			e.printStackTrace();
		}

		return "";
	}

	public String getSearch() {
		return search;
	}

}