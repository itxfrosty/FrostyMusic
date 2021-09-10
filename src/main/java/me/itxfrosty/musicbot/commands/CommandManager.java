package me.itxfrosty.musicbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

	private final List<Command> commandList = new ArrayList<>();

	/**
	 * Register's all command's Listed.
	 *
	 * @param commands Commands.
	 */
	public void registerCommands(JDA jda, Command... commands) {
		this.commandList.addAll(Arrays.asList(commands));

		final List<CommandData> commandDataList = new ArrayList<>();
		final CommandListUpdateAction action = jda.updateCommands();

		for (Command command : commands) {
			CommandData commandData = new CommandData(command.getName(), command.getDescription());

			if (!command.getOptionData().isEmpty()) {
				command.getOptionData().forEach(optionData -> commandData.addOption(optionData.getType(), optionData.getName(), optionData.getDescription(), optionData.isRequired()));
			}

			commandDataList.add(commandData);
		}

		action.addCommands(commandDataList).queue();
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