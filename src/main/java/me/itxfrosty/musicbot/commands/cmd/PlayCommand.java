package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.Command;
import me.itxfrosty.musicbot.commands.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class PlayCommand extends Command {
	private final MusicBot bot;

	public PlayCommand(final MusicBot bot) {
		super("play", "Play's Song.", "/play",false);
		this.bot = bot;

		setCommandData(new CommandData("play","Play's Song from either link or keywords.").addOption(OptionType.STRING,"input","A search term or link.", true));
	}

	@Override
	public void execute(CommandEvent event) {
		if (event.getMember() == null || event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel() || event.getMember().getVoiceState().getChannel() == null) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.RED);
			embed.setDescription(":x: Please connect to a voice channel first!");
			event.reply(embed.build()).queue();
			return;
		}

		bot.getMusicManager().joinVoiceChannel(event.getMember().getVoiceState().getChannel(), event.getChannel(), Objects.requireNonNull(event.getGuild()));

		String message = event.getEvent().getCommandString().replace("/play input: ","");

		try {
			String url;
			try {
				url = new URL(message).toString();
			} catch (MalformedURLException e) {
				url = bot.getYoutubeManager().search(message);
			}
			if (url != null) {
				bot.getMusicManager().addTrack(event.getUser(), url, event.getChannel(), event.getGuild(), event);
				bot.getMusicManager().getMusicGuildManager().get(event.getGuild().getIdLong()).getTrackScheduler().setPaused(false);
			} else {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.RED);
				embed.setDescription(":x: You have reached the maximum quota for today!");
				event.reply(embed.build()).queue();
			}
		} catch (IndexOutOfBoundsException e) {
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.RED);
			embed.setDescription(":x: Please specify a song a to play.");
			event.reply(embed.build()).queue();
		}
	}
}
