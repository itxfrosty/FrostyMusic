package me.itxfrosty.musicbot;

import lombok.Getter;
import me.itxfrosty.musicbot.commands.CommandManager;
import me.itxfrosty.musicbot.commands.cmd.*;
import me.itxfrosty.musicbot.listeners.CommandListener;
import me.itxfrosty.musicbot.managers.BotManager;
import me.itxfrosty.musicbot.managers.YoutubeManager;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import me.itxfrosty.musicbot.utils.FileUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;

public class MusicBot {

	@Getter private static MusicBot instance;

	@Getter private final JDA jda;

	@Getter private final BotManager botManager;
	@Getter private final MusicManager musicManager;
	@Getter private final CommandManager commandManager;
	@Getter private final YoutubeManager youtubeManager;

	@Getter private static final YamlFile yamlFile = new YamlFile("configs/bot.yml");

	/**
	 * Public Constructor for MusicBot.
	 *
	 * @throws LoginException Bad Token.
	 * @throws InterruptedException Bad Connection.
	 */
	public MusicBot() throws LoginException, InterruptedException {
		instance = this;

		this.loadBotConfig();
		this.botManager = new BotManager();

		JDABuilder builder = JDABuilder.createDefault(yamlFile.getString("token"));

		builder.setStatus(OnlineStatus.ONLINE);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS);

		this.jda = builder.build().awaitReady();

		musicManager = new MusicManager();
		commandManager = new CommandManager();
		youtubeManager = new YoutubeManager(this);
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
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}

		assert musicBot != null;
		musicBot.getBotManager().registerEventListener(new CommandListener());

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

		if (yamlFile.getString("token") == null) {
			System.out.println("Bot token for Discord bot not provided! Proceeding to disable bot!");
			System.exit(0);
		}
	}
}