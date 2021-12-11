package me.itxfrosty.frostymusic.commands.cmd;

import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.MusicManager;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class LeaveCommand extends SlashCommand {
	private final MusicManager musicManager;

	public LeaveCommand(final FrostyMusic musicBot) {
		super("leave","Leaves the voice channel.","/leave",false);
		musicManager = musicBot.getGuildAudioManager();
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

		musicManager.leaveVoiceChannel(event.getGuild());
		event.reply(new EmbedBuilder().setDescription("Left voice channel!").build()).queue();
	}
}
