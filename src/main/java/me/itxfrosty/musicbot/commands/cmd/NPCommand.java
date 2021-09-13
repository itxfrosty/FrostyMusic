package me.itxfrosty.musicbot.commands.cmd;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.SlashCommand;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class NPCommand extends SlashCommand {
	private final MusicManager musicManager;

	public NPCommand(final MusicBot musicBot) {
		super("np", "Displays the currently playing song and its duration/position.","/np",false);
		musicManager = musicBot.getMusicManager();
	}

	@Override
	public void execute(CommandEvent event) {
		if (musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()) == null || musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().getQueueCopy().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}
		AudioTrack currentPlaying = musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().getQueueCopy().get(0);
		String[] posString = new String[]{"⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯", "⎯",};
		try {
			posString[(int) Math.floor((float) currentPlaying.getPosition() / (float) currentPlaying.getDuration() * 30F)] = "~~◉~~";
		} catch (Exception e) {
			e.printStackTrace();
		}

		long msPos = currentPlaying.getPosition();
		long minPos = msPos / 60000;
		msPos = msPos % 60000;
		int secPos = (int) Math.floor((float) msPos / 1000f);

		long msDur = currentPlaying.getDuration();
		long minDur = msDur / 60000;
		msDur = msDur % 60000;
		int secDur = (int) Math.floor((float) msDur / 1000f);

		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Now Playing :musical_note:", currentPlaying.getInfo().uri)
				.setDescription(currentPlaying.getInfo().title)
				.addField("Position", String.join("", posString), false)
				.addField("Progress", minPos + ":" + ((secPos < 10) ? "0" + secPos : secPos) + " / " + minDur + ":" + ((secDur < 10) ? "0" + secDur : secDur), false);
		event.reply(builder.build()).queue();
	}
}
