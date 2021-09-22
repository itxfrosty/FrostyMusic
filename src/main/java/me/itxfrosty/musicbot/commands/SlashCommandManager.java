package me.itxfrosty.musicbot.commands;

import me.itxfrosty.musicbot.factories.BotFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlashCommandManager {
	public BotFactory botFactory;

	public SlashCommandManager(BotFactory botFactory) {
		this.botFactory = botFactory;
	}

	public final List<SlashCommand> commandList = new ArrayList<>();

	/**
	 * Register's all command's Listed.
	 *
	 * @param commands Commands.
	 */
	public void registerCommands(SlashCommand... commands) {
		this.commandList.addAll(Arrays.asList(commands));
	}

	/**
	 * Get's all Commands.
	 *
	 * @return List of Commands.
	 */
	public List<SlashCommand> getCommands() {
		return new ArrayList<>(commandList);
	}

}