package me.itxfrosty.musicbot;


import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import me.itxfrosty.musicbot.audio.MusicManager;
import me.itxfrosty.musicbot.audio.sources.SpotifySource;
import me.itxfrosty.musicbot.audio.sources.YoutubeSource;
import me.itxfrosty.musicbot.commands.SlashCommandListener;
import me.itxfrosty.musicbot.commands.SlashCommandManager;
import me.itxfrosty.musicbot.commands.cmd.*;
import me.itxfrosty.musicbot.factories.BotFactory;
import me.itxfrosty.musicbot.utils.FileUtils;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.apache.hc.core5.http.ParseException;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;

public class MusicBot {

	private BotFactory botFactory;
	private final MusicManager musicManager;
	private final YoutubeSource youtubeSource;
	private final SpotifySource spotifySource;
	private final SlashCommandManager commandManager;

	private final YamlFile yamlFile = new YamlFile("configs/bot.yml");

	public MusicBot() throws LoginException, InterruptedException, IOException, ParseException, SpotifyWebApiException {
		this.log("Music Bot Loading...");

		this.loadConfig();

		this.youtubeSource = new YoutubeSource(this);
		this.spotifySource = new SpotifySource(this);

		this.musicManager = new MusicManager(this);
		this.commandManager = new SlashCommandManager(this);

		this.loadBot();

		this.log("Music bot has started successfully!");
	}

	private void loadBot() throws LoginException, InterruptedException {
		this.botFactory = new BotFactory();

		this.botFactory.setToken(yamlFile.getString("token"));

		this.botFactory.setOnlineStatus(OnlineStatus.ONLINE);
		this.botFactory.setMemberCachePolicy(MemberCachePolicy.ALL);
		this.botFactory.registerIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS);
		this.botFactory.registerListener(new SlashCommandListener(this));

		this.botFactory.build();

		commandManager.registerCommands(
				new PlayCommand(this),
				new SkipCommand(this),
				new QueueCommand(this),
				new LeaveCommand(this),
				new VolumeCommand(this),
				new JoinCommand(this),
				new ShuffleCommand(this),
				new PauseCommand(this),
				new NPCommand(this),
				new ResumeCommand(this),
				new SeekCommand(this),
				new SkipToCommand(this));
	}

	private void loadConfig() {
		try {
			if (!yamlFile.exists()) {
				yamlFile.load(FileUtils.getFileFromResource("configs/bot.yml"));
				yamlFile.save();
			}

			yamlFile.load();
			yamlFile.save();

		} catch (IOException | InvalidConfigurationException | URISyntaxException exception) {
			exception.printStackTrace();
		}

		if (yamlFile.getString("token") == null || yamlFile.getString("token").equals("")) {
			System.out.println("Bot token for Discord bot not provided! Proceeding to disable bot!");
			System.exit(0);
		}
	}

	public YamlFile getYamlFile() {
		return yamlFile;
	}

	/**
	 * Log an Object with log level INFO
	 * @param log The object to log
	 */
	public void log(Object log) {
		System.out.println(log);
	}

	/**
	 * Log an Object with log level ERROR
	 * @param log The object to log
	 */
	public void logError(Object log) {
		System.err.println(log);
	}

	public BotFactory getBotFactory() {
		return botFactory;
	}

	public YoutubeSource getYoutubeSource() {
		return youtubeSource;
	}

	public SpotifySource getSpotifySource() {
		return spotifySource;
	}

	public MusicManager getMusicManager() {
		return musicManager;
	}

	public SlashCommandManager getCommandManager() {
		return commandManager;
	}
}
