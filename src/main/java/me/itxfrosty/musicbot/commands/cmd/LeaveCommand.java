package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.Command;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class LeaveCommand extends Command {
	private final MusicManager musicManager;

	public LeaveCommand(final MusicBot musicBot) {
		super("leave","Leaves the voice channel.","/leave",false);
		musicManager = musicBot.getMusicManager();

		setCommandData(new CommandData("leave","Leaves the voice channel."));
	}

	@Override
	public void execute(CommandEvent event) {
		if ((event.getMember() == null) ||
				(event.getMember().getVoiceState() == null) ||
				!event.getMember().getVoiceState().inVoiceChannel() ||
				(event.getMember().getVoiceState().getChannel() == null)) {
			event.reply(new EmbedBuilder().setDescription("You are not in a voice channel!").build()).queue();
			return;
		}

		musicManager.leaveVoiceChannel(event.getGuild());
		event.reply(new EmbedBuilder().setDescription("Left voice channel!").build()).queue();

	}
}
