package me.itxfrosty.musicbot.managers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BotManager {

	private final List<ListenerAdapter> eventListenersList = new ArrayList<>();

	/**
	 * Registers Listener.
	 *
	 * @param listener Listener.
	 */
	public void registerEventListener(@NotNull ListenerAdapter listener) {
		eventListenersList.add(listener);
	}

	/**
	 * Register's all Listeners Listed.
	 *
	 * @param listeners Listeners to List.
	 */
	public void registerEventListeners(@NotNull ListenerAdapter... listeners) {
		Arrays.asList(listeners).forEach(this::registerEventListener);
	}

	/**
	 * Adds the listeners to JDA.
	 *
	 * @param jda JDA.
	 */
	public void addListeners(@NotNull JDA jda) {
		for (ListenerAdapter listener : eventListenersList) {
			jda.addEventListener(listener);
		}
	}
}