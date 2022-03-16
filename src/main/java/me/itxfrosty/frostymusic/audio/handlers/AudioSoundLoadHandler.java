package me.itxfrosty.frostymusic.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.itxfrosty.frostymusic.audio.TrackScheduler;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.utils.MusicUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;

public record AudioSoundLoadHandler(Logger logger, Member member,
									CommandEvent event, boolean sendMessage,
									TrackScheduler trackScheduler,
									String finalTrackURL) implements AudioLoadResultHandler {

	@Override
	public void trackLoaded(AudioTrack audioTrack) {
		logger.info("Loaded Song: " + audioTrack.getInfo().title + " By: " + audioTrack.getInfo().author + " | [User Info] Played By: " + member.getUser().getAsTag() + " ID: " + member.getUser().getId() + " | [Guild Info] Name: " + member.getGuild().getName() + " ID: " + member.getGuild().getId());
		if (!trackScheduler.getTrackQueue().isEmpty()) {
			EmbedBuilder embed = new EmbedBuilder()
					.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
					.addField("Song Duration", MusicUtils.getDuration(audioTrack), true)
					.addField("Position in Queue", String.valueOf(trackScheduler.getTrackQueue().size()), true)
					.setFooter("Added by " + member.getUser().getAsTag(), member.getUser().getEffectiveAvatarUrl())
					.setThumbnail(MusicUtils.getThumbnail(audioTrack));

			if (sendMessage) {
				event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription(":white_check_mark: **" + audioTrack.getInfo().title + "** successfully added to the queue!").build()).queue();
				event.reply(embed.build()).queue();
			}

		} else {

			if (sendMessage) {
				event.reply(new EmbedBuilder().setDescription("Song added to queue.").build()).queue();
			}

		}

		if (trackScheduler.getTrackQueue().contains(audioTrack)) {
			logger.info("Clone Song Found: " + audioTrack.getInfo().title);
			trackScheduler.queueSong(audioTrack.makeClone());
			return;
		}

		trackScheduler.queueSong(audioTrack);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		if (finalTrackURL.contains("ytsearch: ")) {
			final AudioTrack audioTrack = playlist.getTracks().get(0);

			logger.info("Loaded Song: " + audioTrack.getInfo().title + " By: " + audioTrack.getInfo().author + " | [User Info] Played By: " + member.getUser().getAsTag() + " ID: " + member.getUser().getId() + " | [Guild Info] Name: " + member.getGuild().getName() + " ID: " + member.getGuild().getId());

			if (sendMessage) {
				if (!trackScheduler.getTrackQueue().isEmpty()) {
					EmbedBuilder embed = new EmbedBuilder()
							.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
							.addField("Song Duration", MusicUtils.getDuration(playlist.getTracks().get(0)), true)
							.addField("Position in Queue", String.valueOf(trackScheduler.getTrackQueue().size()), true)
							.setFooter("Added by " + member.getUser().getAsTag(), member.getUser().getEffectiveAvatarUrl())
							.setThumbnail(MusicUtils.getThumbnail(playlist.getTracks().get(0)));

					event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription(":white_check_mark: **" + audioTrack.getInfo().title + "** successfully added to the queue!").build()).queue();
					event.reply(embed.build()).queue();
				} else {
					event.reply(new EmbedBuilder().setDescription("Song added to queue.").build()).queue();
				}
			}

			trackScheduler.queueSong(playlist.getTracks().get(0));


		} else {
			event.reply(new EmbedBuilder().setDescription("Loading playlist `" + playlist.getName() + "`").build()).queue();

			logger.info("Loading Playlist: " + playlist.getName() + " | [User Info] Played By: " + member.getUser().getAsTag() + " ID: " + member.getUser().getId() + " | [Guild Info] Name: " + member.getGuild().getName() + " ID: " + member.getGuild().getId());

			for (AudioTrack audioTrack : playlist.getTracks()) {
				logger.info("Loaded Song: " + audioTrack.getInfo().title + " By: " + audioTrack.getInfo().author);
				trackScheduler.queueSong(audioTrack);
			}
		}
	}

	@Override
	public void noMatches() {
		logger.info("Could not find song!");
		event.reply(new EmbedBuilder().setDescription("Could not find song!").build()).queue();
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		logger.error("An error occurred loading the song!", exception);

		event.reply(new EmbedBuilder().setDescription("An error occurred!").build()).queue();
	}

}
