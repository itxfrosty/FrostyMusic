package me.itxfrosty.frostymusic.commands.cmd;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.guild.GuildAudioManager;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

public class QueueCommand extends SlashCommand {
	private final GuildAudioManager musicManager;

	public QueueCommand(final FrostyMusic musicBot) {
		super("queue","Display's a queue of songs.","/queue",false);
		this.musicManager = musicBot.getGuildAudioManager();
	}

	@Override
	public void execute(CommandEvent event) {
		EmbedBuilder embed = new EmbedBuilder();

		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			embed.setDescription("There's no song in the queue for me to play. **/play** a song first.");
			event.reply(embed.build()).queue();
			return;
		}
		List<AudioTrack> tracks = musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue();
		if (tracks.size() == 0 || tracks.get(0) == null) {
			embed.setDescription("There's no song in the queue for me to play. **/play** a song first.");
			event.reply(embed.build()).queue();
			return;
		}

		int totalLength = 0;
		String description = "";
		int finish = tracks.size();
		if (finish > 11) {
			finish = 11;
		}
		for (int i = 0; i < finish; i++) {
			AudioTrack track = tracks.get(i);
			long msPos = track.getInfo().length;
			long minPos = msPos / 60000;
			msPos = msPos % 60000;
			int secPos = (int) Math.floor((float) msPos / 1000f);
			String length = minPos + ":" + ((secPos < 10) ? "0" + secPos : secPos);
			String song = "[" + track.getInfo().title + "](" + track.getInfo().uri + ")";
			if (i == 0) {
				description += "__Now Playing:__";
			} else if (i == 1) {
				description += "\n__Up Next:__";
			}

			if (i == 0) {
				description += String.format("\n%s | `%s`\n", song, length);
			} else {
				description += String.format("\n`%d.` %s | `%s`\n", i, song, length);
			}
			totalLength += track.getInfo().length;
		}
		int minTime = totalLength / 60000;
		totalLength %= 60000;
		int secTime = totalLength / 1000;

		if (tracks.size() > 1) {
			description += "\n**" + (tracks.size() - 1) + " Songs in Queue | " + minTime + ":" + ((secTime < 10) ? "0" + secTime : secTime) + " Total Length**";
		}

		embed.setTitle("Music Queue");
		embed.setDescription(description);

		event.reply(embed.build()).queue();
		return;
	}
}
