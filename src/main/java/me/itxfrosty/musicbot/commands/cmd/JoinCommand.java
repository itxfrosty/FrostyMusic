package me.itxfrosty.musicbot.commands.cmd;

import lombok.NonNull;
import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.Command;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class JoinCommand extends Command {
	private final MusicManager musicManager;

	public JoinCommand(final MusicBot musicBot) {
		super("join","Makes the bot join the voice channel.","/join",false);
		this.musicManager = musicBot.getMusicManager();

		setCommandData(new CommandData("join","Makes the bot join the voice channel."));
	}

	@Override
	public void execute(CommandEvent event) {
		if (event.getMember() == null || event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel() || event.getMember().getVoiceState().getChannel() == null) {
			event.reply(new EmbedBuilder().setDescription("You are not in a voice channel!").build()).queue();
			return;
		}
		musicManager.joinVoiceChannel(event.getEvent().getVoiceChannel(), event.getChannel(), event.getGuild());
		event.reply(new EmbedBuilder().setDescription("Joined `" + event.getMember().getVoiceState().getChannel().getName() + "`").build()).queue();

	}
}