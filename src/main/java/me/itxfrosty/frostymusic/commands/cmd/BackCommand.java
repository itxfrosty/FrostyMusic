package me.itxfrosty.frostymusic.commands.cmd;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.MusicManager;
import me.itxfrosty.frostymusic.audio.TrackScheduler;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackCommand extends SlashCommand {
	private final MusicManager musicManager;

	public BackCommand(final FrostyMusic musicBot) {
		super("back","Play's last song", "/back", false);
		this.musicManager = musicBot.getGuildAudioManager();
	}

	@Override
	public void execute(CommandEvent event, String args) {
		if (event.getMember() == null || event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel() || event.getMember().getVoiceState().getChannel() == null) {
			event.reply(new EmbedBuilder().setDescription("You are not in a voice channel!").build()).queue();
			return;
		}

		final TrackScheduler trackScheduler = musicManager.getGuildAudio(event.getGuild()).getTrackScheduler();

		final AudioTrack track = trackScheduler.getAudioPlayer().getPlayingTrack();
		final AudioTrack lastSong = trackScheduler.getLastSong();

		if (lastSong == null) {
			if (trackScheduler.getTrackQueue().isEmpty()) {
				event.reply(new EmbedBuilder().setDescription("There are no old or new song to be played.").build()).queue();
			} else {
				event.reply(new EmbedBuilder().setDescription("There is no song to go back to!").build()).queue();
			}
		} else {
			trackScheduler.getAudioPlayer().stopTrack();
			trackScheduler.getTrackQueue().add(0, track);
			trackScheduler.getAudioPlayer().playTrack(lastSong.makeClone());
			trackScheduler.setLastSong(null);
			trackScheduler.setLastBoolean(false);

			event.reply(new EmbedBuilder().setDescription("Now playing last song.").build()).queue();
		}
	}
}
