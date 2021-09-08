package me.itxfrosty.musicbot.managers;

import me.itxfrosty.musicbot.commands.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BotManager {

	private final List<Command> commandList = new ArrayList<>();
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

	/**
	 * Register's all command's Listed.
	 *
	 * @param commands Commands.
	 */
	public void registerCommands(JDA jda, Command... commands) {
		this.commandList.addAll(Arrays.asList(commands));

		CommandListUpdateAction action = jda.updateCommands();
		commandList.forEach((command -> action.addCommands(command.getCommandData()).queue()));

	}

	/**
	 * Get's all Commands.
	 *
	 * @return List of Commands.
	 */
	public List<Command> getCommands() {
		return new ArrayList<>(commandList);
	}
}