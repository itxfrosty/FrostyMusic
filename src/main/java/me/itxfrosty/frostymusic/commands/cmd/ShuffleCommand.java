package me.itxfrosty.frostymusic.commands.cmd;

import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.MusicManager;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class ShuffleCommand extends SlashCommand {
	private final MusicManager musicManager;

	public ShuffleCommand(final FrostyMusic musicBot) {
		super("shuffle","Shuffles the queue", "/shuffle",false);
		this.musicManager = musicBot.getGuildAudioManager();
	}

	@Override
	public void execute(CommandEvent event, String args) {
		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}
		musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().shuffle();
		event.reply(new EmbedBuilder().setDescription("🔀 Shuffled Queue!").build()).queue();
	}
}
