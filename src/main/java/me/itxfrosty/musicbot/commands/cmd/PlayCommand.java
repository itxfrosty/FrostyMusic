package me.itxfrosty.musicbot.commands.cmd;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import lombok.var;
import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.audio.sources.SpotifySource;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.commands.SlashCommand;
import me.itxfrosty.musicbot.objects.enums.RequestType;
import me.itxfrosty.musicbot.objects.enums.SpotifyType;
import me.itxfrosty.musicbot.objects.enums.YoutubeType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class PlayCommand extends SlashCommand {
	private final MusicBot bot;

	public PlayCommand(final MusicBot bot) {
		super("play", "Play's Song from either link or keywords.", "/play",false);
		this.bot = bot;

		getOptionData().add(new OptionData(OptionType.STRING,"input","A search term or link.", true));
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

		@NotNull String message = event.getEvent().getCommandString().replace("/play input: ", "");

		bot.getMusicManager().playSong(message, event);
	}
}
