package me.itxfrosty.frostymusic.commands;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SlashCommand {

	@Getter public final String name;
	@Getter public final String description;
	@Getter public final String usage;
	@Getter public final boolean moderatorOnly;

	@Getter public List<OptionData> optionData;

	private String[] args;

	public SlashCommand(String name, String description, String usage, boolean moderatorOnly) {
		this.name = name;
		this.description = description;
		this.usage = usage;
		this.moderatorOnly = moderatorOnly;

		this.optionData = new ArrayList<>();
	}

	public abstract void execute(CommandEvent event, String args);

	public void addOption(OptionData optionData) {
		this.optionData.add(optionData);
	}

	public void addOptions(OptionData... optionData) {
		this.optionData.addAll(Arrays.asList(optionData));
	}
}