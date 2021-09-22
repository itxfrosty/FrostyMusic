package me.itxfrosty.musicbot.audio.guild;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import me.itxfrosty.musicbot.audio.TrackScheduler;
import me.itxfrosty.musicbot.audio.sources.SpotifySource;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.objects.RequestType;
import me.itxfrosty.musicbot.objects.SpotifyType;
import me.itxfrosty.musicbot.utils.MusicUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GuildAudioManager {

	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> guildMusicManager;

	public GuildAudioManager() {
		this.guildMusicManager = new HashMap<>();
		this.playerManager = new DefaultAudioPlayerManager();

		this.playerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
		this.playerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
		this.playerManager.getConfiguration().setFilterHotSwapEnabled(true);

		AudioSourceManagers.registerRemoteSources(this.playerManager);
		AudioSourceManagers.registerLocalSource(this.playerManager);

		playerManager.registerSourceManager(new YoutubeAudioSourceManager());
	}

	/**
	 * Get's Guild Audio Manger.
	 *
	 * @param guild Guild to get AudioManager.
	 * @return GuildMusicManager.
	 */
	public synchronized GuildMusicManager getGuildAudio(Guild guild) {
		return this.guildMusicManager.computeIfAbsent(guild.getIdLong(), (guildAudio) -> {
			final GuildMusicManager guildMusicManager = new GuildMusicManager(this.playerManager, guild);

			guild.getAudioManager().setSendingHandler(guildMusicManager.getAudioSendProvider());

			return guildMusicManager;
		});
	}

	/**
	 * Join's Voice Chanel and set's log channel.
	 *
	 * @param voiceChannel Voice Channel.
	 * @param messageChannel Message Channel.
	 * @param guild Guild.
	 */
	public void joinVoiceChannel(final VoiceChannel voiceChannel, final MessageChannel messageChannel, final Guild guild) {
		final AudioManager audioManager = guild.getAudioManager();
		final GuildMusicManager guildMusicManager = getGuildAudio(guild);

		audioManager.openAudioConnection(voiceChannel);
	}

	/**
	 * Leave's Voice Channel.
	 *
	 * @param guild Guild
	 */
	public void leaveVoiceChannel(Guild guild) {
		guild.getAudioManager().closeAudioConnection();
		getGuildAudio(guild).getTrackScheduler().clearQueue();
	}

	/**
	 * Loads and plays a track by it's URL.
	 *
	 * @param event CommandEvent for getting details.
	 * @param trackURL The URL of the track you want to play.
	 * @param sendMessage Sends message if true.
	 */
	public void loadAndPlay(final CommandEvent event, String trackURL, final boolean sendMessage) {
		final GuildMusicManager musicManager = getGuildAudio(event.getGuild());
		final Member member = event.getMember();

		event.getEvent().deferReply().queue();

		if (member == null || member.getVoiceState() == null || !member.getVoiceState().inVoiceChannel() || member.getVoiceState().getChannel() == null) {
			event.getEvent().getHook().sendMessageEmbeds(new EmbedBuilder().setDescription("Please connect to a voice channel first!").build()).queue();
			return;
		}

		this.joinVoiceChannel(member.getVoiceState().getChannel(), event.getChannel(), event.getGuild());
		final TrackScheduler trackScheduler = musicManager.getTrackScheduler();

		if (!MusicUtils.ifURL(trackURL)) {
			String temp = trackURL;
			trackURL = "ytsearch: " + temp;
		}

		String finalTrackURL = trackURL;
		playerManager.loadItem(finalTrackURL, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				if (!trackScheduler.getTrackQueue().isEmpty()) {
					EmbedBuilder embed = new EmbedBuilder()
							.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
							.addField("Song Duration", MusicUtils.getDuration(audioTrack), true)
							.addField("Position in Queue", String.valueOf(trackScheduler.getTrackQueue().size()), true)
							.setFooter("Added by " + member.getUser().getAsTag(), member.getUser().getEffectiveAvatarUrl())
							.setThumbnail(MusicUtils.getThumbnail(audioTrack));

					if (sendMessage) {
						event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription(":white_check_mark: **" + audioTrack.getInfo().title + "** successfully added to the queue!").build()).queue();
						event.getEvent().getHook().sendMessageEmbeds(embed.build()).queue();
					}

				} else {

					if (sendMessage) {
						event.getEvent().getHook().sendMessageEmbeds(new EmbedBuilder().setDescription("Song added to queue.").build()).queue();
					}

				}

				trackScheduler.queueSong(audioTrack);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				if (finalTrackURL.contains("ytsearch: ")) {
					trackScheduler.queueSong(playlist.getTracks().get(0));

					if (sendMessage) {
						event.getEvent().getHook().sendMessageEmbeds(new EmbedBuilder().setDescription("Song added to queue.").build()).queue();
					}
				}
			}

			@Override
			public void noMatches() {
				event.getEvent().getHook().sendMessageEmbeds(new EmbedBuilder().setDescription("Could not find song!").build()).queue();
				System.out.println("No Matches");
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				event.getEvent().getHook().sendMessageEmbeds(new EmbedBuilder().setDescription("An error occurred!").build()).queue();
			}
		});
	}
}