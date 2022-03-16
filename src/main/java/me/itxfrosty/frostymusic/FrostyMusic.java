package me.itxfrosty.frostymusic;

import lombok.Getter;
import me.itxfrosty.frostymusic.audio.MusicManager;
import me.itxfrosty.frostymusic.audio.sources.applemusic.AppleMusicAPI;
import me.itxfrosty.frostymusic.commands.CommandHandler;
import me.itxfrosty.frostymusic.data.MusicConfig;
import me.itxfrosty.frostymusic.factories.BotFactory;
import me.itxfrosty.frostymusic.listeners.DeafenListener;
import me.itxfrosty.frostymusic.listeners.ReadyListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class FrostyMusic {
	@Getter private final Logger logger = LoggerFactory.getLogger(FrostyMusic.class);

	@Getter private final BotFactory botFactory;
	@Getter private final CommandHandler commandHandler;
	@Getter private final MusicManager guildAudioManager;

	@Getter private final AppleMusicAPI appleMusicAPI;

	public FrostyMusic() throws LoginException, InterruptedException {
		Thread.currentThread().setName("FrostyMusic");
		this.logger.info("Starting FrostyMusic V{}.", MusicConfig.VERSION);

		this.guildAudioManager = new MusicManager(this);
		this.commandHandler = new CommandHandler(this);

		this.appleMusicAPI = new AppleMusicAPI("MusicConfig.APPLE_TOKEN");

		this.botFactory = new BotFactory()
				.setToken(MusicConfig.TOKEN)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.registerIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_PRESENCES)
				.disableCaches(CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
				.registerListeners(this.commandHandler, new ReadyListener(this), new DeafenListener(this));
		this.botFactory.build();
		this.logger.info("Connected to Discord!");

		try	{
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();;
		}

	}
}