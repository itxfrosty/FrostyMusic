package me.itxfrosty.musicbot;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import lombok.Getter;
import me.itxfrosty.musicbot.commands.SlashCommandManager;
import me.itxfrosty.musicbot.commands.cmd.*;
import me.itxfrosty.musicbot.commands.SlashCommandListener;
import me.itxfrosty.musicbot.managers.BotManager;
import me.itxfrosty.musicbot.managers.audio.sources.SpotifyManager;
import me.itxfrosty.musicbot.managers.audio.sources.YoutubeManager;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import me.itxfrosty.musicbot.utils.ConsoleMessage;
import me.itxfrosty.musicbot.utils.FileUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.apache.hc.core5.http.ParseException;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MusicBot {

	@Getter private final JDA jda;

	@Getter private final BotManager botManager;
	@Getter private final MusicManager musicManager;
	@Getter private final SlashCommandManager commandManager;

	@Getter private final SpotifyManager spotifyManager;
	@Getter private final YoutubeManager youtubeManager;

	@Getter private static final YamlFile yamlFile = new YamlFile("configs/bot.yml");

	@Getter private static MusicBot instance;

	/**
	 * Public Constructor for MusicBot.
	 *
	 * @throws LoginException Bad Token.
	 * @throws InterruptedException Bad Connection.
	 */
	public MusicBot() throws GeneralSecurityException, InterruptedException, IOException, ParseException, SpotifyWebApiException {
		ConsoleMessage.log("Starting Bot...");

		instance = this;
		this.loadBotConfig();

		JDABuilder builder = JDABuilder.createDefault(yamlFile.getString("token"));

		builder.setStatus(OnlineStatus.ONLINE);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS);

		this.jda = builder.build().awaitReady();

		this.botManager = new BotManager();
		this.musicManager = new MusicManager();
		this.commandManager = new SlashCommandManager();
		this.spotifyManager = new SpotifyManager();
		this.youtubeManager = new YoutubeManager(this);

		ConsoleMessage.log("Bot has fully started.");
	}

	/**
	 * Initializes bot.
	 *
	 * @param args Ignored.
	 */
	public static void main(String[] args) {
		MusicBot musicBot = null;

		try {
			musicBot = new MusicBot();
		} catch (InterruptedException | GeneralSecurityException | IOException | ParseException | SpotifyWebApiException e) {
			e.printStackTrace();
		}

		assert musicBot != null;
		musicBot.getBotManager().registerEventListener(new SlashCommandListener(musicBot));

		musicBot.getCommandManager().registerCommands(
				musicBot.getJda(),
				new PlayCommand(musicBot),
				new SkipCommand(musicBot),
				new QueueCommand(musicBot),
				new LeaveCommand(musicBot),
				new VolumeCommand(musicBot),
				new JoinCommand(musicBot),
				new ShuffleCommand(musicBot),
				new PauseCommand(musicBot),
				new NPCommand(musicBot),
				new ResumeCommand(musicBot),
				new SeekCommand(musicBot),
				new SkipToCommand(musicBot));

		musicBot.getBotManager().addListeners(musicBot.getJda());
	}

	private void loadBotConfig() {
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
}