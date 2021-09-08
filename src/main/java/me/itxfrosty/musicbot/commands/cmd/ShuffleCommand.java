package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.Command;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class ShuffleCommand extends Command {
	private final MusicManager musicManager;

	public ShuffleCommand(final MusicBot musicBot) {
		super("shuffle","Shuffles the queue", "/shuffle",false);
		this.musicManager = musicBot.getMusicManager();

		setCommandData(new CommandData("shuffle","Shuffles the queue."));
	}

	@Override
	public void execute(CommandEvent event) {
		if (musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()) == null || musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().getQueueCopy().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}
		musicManager.getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().shuffle();
		event.reply(new EmbedBuilder().setDescription("🔀 Shuffled Queue!").build()).queue();
	}
}
