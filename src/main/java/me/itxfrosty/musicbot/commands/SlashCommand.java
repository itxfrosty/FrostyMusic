package me.itxfrosty.musicbot.commands;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SlashCommand {

	public final String name;
	public final String description;
	public final String usage;
	public final boolean moderatorOnly;

	public List<OptionData> optionData;

	public SlashCommand(String name, String description, String usage, boolean moderatorOnly) {
		this.name = name;
		this.description = description;
		this.usage = usage;
		this.moderatorOnly = moderatorOnly;

		this.optionData = new ArrayList<>();
	}

	public abstract void execute(CommandEvent event);

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getUsage() {
		return usage;
	}

	public boolean isModeratorOnly() {
		return moderatorOnly;
	}

	public List<OptionData> getOptionData() {
		return optionData;
	}

	public void addOption(OptionData optionData) {
		this.getOptionData().add(optionData);
	}

	public void addOptions(OptionData... optionData) {
		this.getOptionData().addAll(Arrays.asList(optionData));
	}
}