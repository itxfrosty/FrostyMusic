package me.itxfrosty.musicbot;

import me.itxfrosty.musicbot.audio.guild.GuildAudioManager;
import me.itxfrosty.musicbot.commands.SlashCommandManager;
import me.itxfrosty.musicbot.commands.cmd.*;
import me.itxfrosty.musicbot.data.Config;
import me.itxfrosty.musicbot.factories.BotFactory;
import me.itxfrosty.musicbot.listeners.CommandHandlerListener;
import me.itxfrosty.musicbot.listeners.ReadyListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class MusicBot {
	private final Logger logger = LoggerFactory.getLogger(MusicBot.class);

	private final BotFactory botFactory;
	private final GuildAudioManager guildAudioManager;
	private final SlashCommandManager slashCommandManager;

	public MusicBot() throws LoginException, InterruptedException {
		Thread.currentThread().setName("FrostyMusic");
		logger.info("Starting FrostyMusic V{}.", Config.VERSION);

		botFactory = new BotFactory()
				.setToken(Config.TOKEN)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.registerIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_PRESENCES)
				.disableCaches(CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
				.registerListeners(new CommandHandlerListener(this), new ReadyListener(this));
		botFactory.build();
		logger.info("Connected to Discord!");

		guildAudioManager = new GuildAudioManager();
		slashCommandManager = new SlashCommandManager(botFactory);

		slashCommandManager.registerCommands(
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
				new SkipToCommand(this),
				new ClearCommand(this),
				new BassBoostCommand(this));
	}

	public static void main(String[] args) {
		try {
			new MusicBot();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public BotFactory getBotFactory() {
		return botFactory;
	}

	public GuildAudioManager getGuildAudioManager() {
		return guildAudioManager;
	}

	public SlashCommandManager getSlashCommandManager() {
		return slashCommandManager;
	}

	public Logger getLogger() {
		return logger;
	}
}