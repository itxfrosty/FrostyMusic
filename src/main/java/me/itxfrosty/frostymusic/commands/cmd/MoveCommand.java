package me.itxfrosty.frostymusic.commands.cmd;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.MusicManager;
import me.itxfrosty.frostymusic.audio.TrackScheduler;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class MoveCommand extends SlashCommand {
	private final MusicManager musicManager;

	public MoveCommand(final FrostyMusic frostyMusic) {
		super("move", "moves songs around", "/move", false);

		this.musicManager = frostyMusic.getGuildAudioManager();

		this.addOption(new OptionData(OptionType.NUMBER, "input", "The song to move to output's position.", true));
		this.addOption(new OptionData(OptionType.NUMBER, "output", "The song to replace input's position.", true));
	}

	@Override
	public void execute(CommandEvent event, String args) {
		if ((event.getMember() == null) ||
				(event.getMember().getVoiceState() == null) ||
				!event.getMember().getVoiceState().inVoiceChannel() ||
				(event.getMember().getVoiceState().getChannel() == null)) {
			event.reply(new EmbedBuilder().setDescription("You are not in a voice channel!").build()).queue();
			return;
		}

		final int input = (int) event.getEvent().getOption("input").getAsDouble();
		final int output = (int) event.getEvent().getOption("output").getAsDouble();

		final TrackScheduler trackScheduler = this.musicManager.getGuildAudio(event.getGuild()).getTrackScheduler();

		try {
			if (trackScheduler.getTrackQueue().get(input) == null) {
				event.reply(new EmbedBuilder().setDescription("Cannot find song!").build()).queue();
			} else if (trackScheduler.getTrackQueue().get(output) == null) {
				event.reply(new EmbedBuilder().setDescription("Cannot find song!").build()).queue();
			} else {
				AudioTrack audioInput = trackScheduler.getTrackQueue().get(input);
				AudioTrack audioOutput = trackScheduler.getTrackQueue().get(output);

				int indexIn = trackScheduler.getTrackQueue().indexOf(audioInput);
				int indexOut = trackScheduler.getTrackQueue().indexOf(audioOutput);

				trackScheduler.getTrackQueue().set(indexIn, audioOutput);
				trackScheduler.getTrackQueue().set(indexOut, audioInput);

				event.reply(new EmbedBuilder().setDescription("Song's Switched!").build()).queue();
			}
		} catch (Exception e) {
			event.reply(new EmbedBuilder().setDescription("Cannot find song!").build()).queue();
		}




	}
}
