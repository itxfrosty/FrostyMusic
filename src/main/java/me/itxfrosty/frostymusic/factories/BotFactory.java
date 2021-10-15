package me.itxfrosty.frostymusic.factories;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bot Factory
 * @author itxfrosty
 */
public class BotFactory {
	private final Logger logger = LoggerFactory.getLogger(BotFactory.class);

	/* Shard Manager Interface for the Discord Bot */
	private ShardManager shardManager;
	/* Shard Manager for the Discord Bot. */
	private final DefaultShardManagerBuilder defaultShardManager;

	/* Discord Account Token. */
	private String token;
	/* Discord Account Activity. */
	private Activity activity;
	/* Discord Account Status. */
	private OnlineStatus onlineStatus;
	/* Discord Account Cache Policy. */
	private MemberCachePolicy memberCachePolicy;

	/* Built if the bot has started. */
	private boolean built;

	/* Cache Flag Enable/Disable List */
	private final List<CacheFlag> enableCacheFlagList;
	private final List<CacheFlag> disableCacheFlagList;

	/* Gateway Intent List */
	private final List<GatewayIntent> gatewayIntentList;

	/* Listeners List */
	private final List<ListenerAdapter> eventListenersList;

	/**
	 * BotFactory Constructor
	 *
	 * @param token Token of Discord Bot account.
	 */
	public BotFactory(String token) {
		this.token = token;

		this.gatewayIntentList = new ArrayList<>();
		this.eventListenersList = new ArrayList<>();
		this.enableCacheFlagList = new ArrayList<>();
		this.disableCacheFlagList = new ArrayList<>();

		this.built = false;

		this.defaultShardManager = DefaultShardManagerBuilder.createDefault(token);
	}

	/**
	 * BotFactory Constructor.
	 */
	public BotFactory() {
		this.gatewayIntentList = new ArrayList<>();
		this.eventListenersList = new ArrayList<>();
		this.enableCacheFlagList = new ArrayList<>();
		this.disableCacheFlagList = new ArrayList<>();

		this.built = false;

		this.defaultShardManager = DefaultShardManagerBuilder.createDefault(null);
	}

	/**
	 * Set's Bot Token.
	 *
	 * @param token Bot Token.
	 * @return this.
	 */
	public BotFactory setToken(String token) {
		this.token = token;

		return this;
	}

	/**
	 * Set's the activity of the bot.
	 *
	 * @param activity Activity.
	 * @return this.
	 */
	public BotFactory setActivity(Activity activity) {
		this.activity = activity;

		return this;
	}

	/**
	 * Set's Online status of the bot.
	 *
	 * @param onlineStatus Online Status.
	 * @return this.
	 */
	public BotFactory setOnlineStatus(OnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;

		return this;
	}

	/**
	 * Set's Member Cache Policy for bot.
	 *
	 * @param memberCachePolicy Member Cache Policy.
	 * @return this.
	 */
	public BotFactory setMemberCachePolicy(MemberCachePolicy memberCachePolicy) {
		this.memberCachePolicy = memberCachePolicy;

		return this;
	}

	/**
	 * Registers Listener.
	 *
	 * @param listenerAdapter Listeners.
	 * @return this.
	 */
	public BotFactory registerListener(ListenerAdapter listenerAdapter) {
		this.eventListenersList.add(listenerAdapter);

		return this;
	}

	/**
	 * Registers multiple listeners.
	 *
	 * @param listeners Listeners.
	 * @return this.
	 */
	public BotFactory registerListeners(ListenerAdapter... listeners) {
		this.eventListenersList.addAll(Arrays.asList(listeners));

		return this;
	}

	/**
	 * Registers Intent.
	 *
	 * @param gatewayIntent Gateway Intent to register.
	 * @return this.
	 */
	public BotFactory registerIntent(GatewayIntent gatewayIntent) {
		this.gatewayIntentList.add(gatewayIntent);

		return this;
	}

	/**
	 * Registers Intents.
	 *
	 * @param gatewayIntent Gateway Intents to register.
	 * @return this.
	 */
	public BotFactory registerIntents(GatewayIntent... gatewayIntent) {
		this.gatewayIntentList.addAll(Arrays.asList(gatewayIntent));

		return this;
	}

	/**
	 * Register's Cache Flag.
	 *
	 * @param cacheFlag Cache Flag to register.
	 * @return this.
	 */
	public BotFactory registerCache(CacheFlag cacheFlag) {
		this.enableCacheFlagList.add(cacheFlag);

		return this;
	}

	/**
	 * Register's Cache Flags.
	 *
	 * @param cacheFlag Cache Flags to register.
	 * @return this.
	 */
	public BotFactory registerCaches(CacheFlag... cacheFlag) {
		this.enableCacheFlagList.addAll(Arrays.asList(cacheFlag));

		return this;
	}

	/**
	 * Disables Cache Flag.
	 *
	 * @param cacheFlag Cache Flag to register.
	 * @return this.
	 */
	public BotFactory disableCache(CacheFlag cacheFlag) {
		this.disableCacheFlagList.add(cacheFlag);

		return this;
	}

	/**
	 * Disables Cache Flags.
	 *
	 * @param cacheFlag Cache Flags to register.
	 * @return this.
	 */
	public BotFactory disableCaches(CacheFlag... cacheFlag) {
		this.disableCacheFlagList.addAll(Arrays.asList(cacheFlag));

		return this;
	}

	/**
	 * Build's Bot and start's Login Process.
	 *
	 * @throws LoginException Discord Bot Login Exception.
	 */
	public void build() throws LoginException {
		if (built) {
			this.logger.error("Bot already built! Please use a new instance, if you would like to start new bot.");
			return;
		}

		if (this.token == null ) {
			throw new LoginException("Bot token is null. Shutting down...");
		}

		this.logger.debug("Starting login process...");

		this.defaultShardManager.setToken(token);

		if (this.activity != null) this.defaultShardManager.setActivity(this.activity);
		if (this.onlineStatus != null) this.defaultShardManager.setStatus(this.onlineStatus);
		if (this.memberCachePolicy != null) this.defaultShardManager.setMemberCachePolicy(this.memberCachePolicy);

		if (!this.gatewayIntentList.isEmpty()) this.defaultShardManager.enableIntents(this.gatewayIntentList);
		if (!this.eventListenersList.isEmpty()) this.eventListenersList.forEach(this.defaultShardManager::addEventListeners);
		if (!this.enableCacheFlagList.isEmpty()) this.defaultShardManager.enableCache(this.enableCacheFlagList);
		if (!this.disableCacheFlagList.isEmpty()) this.defaultShardManager.disableCache(this.disableCacheFlagList);

		this.defaultShardManager.setShardsTotal(-1);

		if (this.isNas()) {
			this.logger.info("Enabling NAS...");
			this.defaultShardManager.setAudioSendFactory(new NativeAudioSendFactory());
		}


		this.shardManager = this.defaultShardManager.build();
		this.built = true;
	}

	/**
	 * If Nas-Compatible.
	 *
	 * @return If Nas-Compatible.
	 */
	private boolean isNas() {
		final String os = System.getProperty("os.name").toLowerCase();
		final String arch = System.getProperty("os.arch");

		return (os.contains("windows") || os.contains("linux")) && !arch.equalsIgnoreCase("arm") && !arch.equalsIgnoreCase("arm-linux");
	}

	public boolean isBuilt() {
		return built;
	}

	public @NotNull String getToken() {
		return this.token;
	}

	public @NotNull Activity getActivity() {
		return this.activity;
	}

	public @NotNull ShardManager getShardManager() {
		return this.shardManager;
	}

	public @NotNull OnlineStatus getOnlineStatus() {
		return this.onlineStatus;
	}

	public @NotNull MemberCachePolicy getMemberCachePolicy() {
		return this.memberCachePolicy;
	}

	public @NotNull List<CacheFlag> getEnableCacheFlagList() {
		return enableCacheFlagList;
	}

	public @NotNull List<CacheFlag> getDisableCacheFlagList() {
		return disableCacheFlagList;
	}

	public @NotNull List<GatewayIntent> getGatewayIntentList() {
		return gatewayIntentList;
	}

	public @NotNull List<ListenerAdapter> getEventListenersList() {
		return eventListenersList;
	}
}