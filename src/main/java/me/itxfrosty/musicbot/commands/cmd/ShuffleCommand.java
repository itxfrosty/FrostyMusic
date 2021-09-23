package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.audio.guild.GuildAudioManager;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class ShuffleCommand extends SlashCommand {
	private final GuildAudioManager musicManager;

	public ShuffleCommand(final MusicBot musicBot) {
		super("shuffle","Shuffles the queue", "/shuffle",false);
		this.musicManager = musicBot.getGuildAudioManager();
	}

	@Override
	public void execute(CommandEvent event) {
		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}
		musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().shuffle();
		event.reply(new EmbedBuilder().setDescription("🔀 Shuffled Queue!").build()).queue();
	}
}
