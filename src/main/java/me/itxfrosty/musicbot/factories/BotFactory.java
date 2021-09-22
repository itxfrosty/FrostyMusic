package me.itxfrosty.musicbot.factories;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManager;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bot Factory
 * @author itxfrosty
 */
public class BotFactory {

	private ShardManager shardManager;
	private final DefaultShardManagerBuilder defaultShardManager;

	private String token;
	private Activity activity;
	private OnlineStatus onlineStatus;
	private MemberCachePolicy memberCachePolicy;

	private final List<CacheFlag> enablecacheFlagList;
	private final List<CacheFlag> disablecacheFlagList;
	private final List<GatewayIntent> gatewayIntentList;
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
		this.enablecacheFlagList = new ArrayList<>();
		this.disablecacheFlagList = new ArrayList<>();

		this.defaultShardManager = DefaultShardManagerBuilder.createDefault(token);
	}

	/**
	 * BotFactory Constructor.
	 */
	public BotFactory() {
		this.gatewayIntentList = new ArrayList<>();
		this.eventListenersList = new ArrayList<>();
		this.enablecacheFlagList = new ArrayList<>();
		this.disablecacheFlagList = new ArrayList<>();

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
		this.enablecacheFlagList.add(cacheFlag);

		return this;
	}

	/**
	 * Register's Cache Flags.
	 *
	 * @param cacheFlag Cache Flags to register.
	 * @return this.
	 */
	public BotFactory registerCaches(CacheFlag... cacheFlag) {
		this.enablecacheFlagList.addAll(Arrays.asList(cacheFlag));

		return this;
	}

	/**
	 * Disables Cache Flag.
	 *
	 * @param cacheFlag Cache Flag to register.
	 * @return this.
	 */
	public BotFactory disableCache(CacheFlag cacheFlag) {
		this.enablecacheFlagList.add(cacheFlag);

		return this;
	}

	/**
	 * Disables Cache Flags.
	 *
	 * @param cacheFlag Cache Flags to register.
	 * @return this.
	 */
	public BotFactory disableCaches(CacheFlag... cacheFlag) {
		this.enablecacheFlagList.addAll(Arrays.asList(cacheFlag));

		return this;
	}


	/**
	 * Build's Bot.
	 *
	 * @throws LoginException Discord Bot Login Exception.
	 */
	public void build() throws LoginException {
		if (this.token == null ) {
			throw new LoginException("Bot token is null. Shutting down...");
		}

		this.defaultShardManager.setToken(token);

		if (this.activity != null) this.defaultShardManager.setActivity(this.activity);
		if (this.onlineStatus != null) this.defaultShardManager.setStatus(this.onlineStatus);
		if (this.memberCachePolicy != null) this.defaultShardManager.setMemberCachePolicy(this.memberCachePolicy);

		if (!this.gatewayIntentList.isEmpty()) this.defaultShardManager.enableIntents(this.gatewayIntentList);
		if (!this.eventListenersList.isEmpty()) this.eventListenersList.forEach(this.defaultShardManager::addEventListeners);
		if (!this.enablecacheFlagList.isEmpty()) this.defaultShardManager.enableCache(this.enablecacheFlagList);
		if (!this.disablecacheFlagList.isEmpty()) this.defaultShardManager.disableCache(this.disablecacheFlagList);

		this.defaultShardManager.setShardsTotal(-1);
		this.defaultShardManager.setAudioSendFactory(new NativeAudioSendFactory());

		this.shardManager = this.defaultShardManager.build();
	}

	public String getToken() {
		return this.token;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public ShardManager getShardManager() {
		return this.shardManager;
	}

	public OnlineStatus getOnlineStatus() {
		return this.onlineStatus;
	}

	public MemberCachePolicy getMemberCachePolicy() {
		return this.memberCachePolicy;
	}
}