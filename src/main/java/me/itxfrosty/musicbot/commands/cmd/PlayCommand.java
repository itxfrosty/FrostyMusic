package me.itxfrosty.musicbot.commands.cmd;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.commands.SlashCommand;
import me.itxfrosty.musicbot.enums.RequestType;
import me.itxfrosty.musicbot.enums.SpotifyType;
import me.itxfrosty.musicbot.enums.YoutubeType;
import me.itxfrosty.musicbot.managers.audio.sources.SpotifyManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class PlayCommand extends SlashCommand {
	private final MusicBot bot;

	public PlayCommand(final MusicBot bot) {
		super("play", "Play's Song from either link or keywords.", "/play",false);
		this.bot = bot;

		getOptionData().add(new OptionData(OptionType.STRING,"input","A search term or link.", true));
	}

	@Override
	public void execute(CommandEvent event) {
		if (event.getMember() == null || event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel() || event.getMember().getVoiceState().getChannel() == null) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.RED);
			embed.setDescription(":x: Please connect to a voice channel first!");
			event.reply(embed.build()).queue();
			return;
		}

		bot.getMusicManager().joinVoiceChannel(event.getMember().getVoiceState().getChannel(), event.getChannel(), Objects.requireNonNull(event.getGuild()));

		@NotNull String message = event.getEvent().getCommandString().replace("/play input: ","");

	 	final RequestType requestType = Objects.requireNonNull(RequestType.getRequestType(message));

		switch (requestType) {
			case YOUTUBE -> {
				YoutubeType youtubeType = Objects.requireNonNull(YoutubeType.getYouTubeType(message));

				switch (youtubeType) {
					case TRACK -> {
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

					case PLAYLIST -> {
						try {
							List<String> playlist = bot.getYoutubeManager().getPlaylistItemsByLink(message);

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

			case SPOTIFY -> {
				SpotifyType spotifyType = Objects.requireNonNull(SpotifyType.getSpotifyType(message));

				switch (spotifyType) {
					case TRACK -> {
						// Track

						if (message.contains("open.spotify.com/track/")) {
							message = message.replace("https://", "");
							message = message.split("/")[2];
							message = message.split("\\?")[0];
						} else if (message.startsWith("spotify:track:")) {
							message = message.split(":")[2];
						}

						try {
							SpotifyManager spotify = bot.getSpotifyManager();

							String search = spotify.getTrackArtists(message) + " " + spotify.getTrackName(message);
							searchForSong(search,true, event);

						} catch (IOException | ParseException | SpotifyWebApiException e) {
							e.printStackTrace();
						}
					}

					case PLAYLIST -> {

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

							List<String> playlist = bot.getSpotifyManager().getPlaylist(id);
							for (String link : playlist) {
								searchForSong(link,false, event);
							}

							event.reply(new EmbedBuilder().setDescription("Loading playlist `").build());

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					case ALBUM -> {
						// Album

						System.out.println("Album: Not Today");
					}

					case ARTIST -> {
						// Artist

						System.out.println("Artist: Not Today");
					}
				}
			}

			case SEARCH -> {
				searchForSong(message,true, event);
				return;
			}
		}
	}

	private void searchForSong(String message, boolean mess, CommandEvent event) {
		String search_song = bot.getYoutubeManager().search(message);

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
