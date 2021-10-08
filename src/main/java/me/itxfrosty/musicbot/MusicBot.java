package me.itxfrosty.musicbot;

import lombok.Getter;
import me.itxfrosty.musicbot.audio.guild.GuildAudioManager;
import me.itxfrosty.musicbot.data.Config;
import me.itxfrosty.musicbot.factories.BotFactory;
import me.itxfrosty.musicbot.commands.CommandHandler;
import me.itxfrosty.musicbot.listeners.ReadyListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class MusicBot {
	@Getter private final Logger logger = LoggerFactory.getLogger(MusicBot.class);

	@Getter private final CommandHandler commandHandler;
	@Getter private final GuildAudioManager guildAudioManager;

	public MusicBot() throws LoginException, InterruptedException {
		Thread.currentThread().setName("FrostyMusic");
		this.logger.info("Starting FrostyMusic V{}.", Config.VERSION);

		BotFactory botFactory = new BotFactory()
				.setToken(Config.TOKEN)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.registerIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_PRESENCES)
				.disableCaches(CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
				.registerListeners(new CommandHandler(this), new ReadyListener(this));
		botFactory.build();
		this.logger.info("Connected to Discord!");

		this.guildAudioManager = new GuildAudioManager();
		this.commandHandler = new CommandHandler(this);
	}

	public static void main(String[] args) {
		try {
			new MusicBot();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}