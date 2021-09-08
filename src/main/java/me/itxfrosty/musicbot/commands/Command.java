package me.itxfrosty.musicbot.commands;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

	@Getter public final String name;
	@Getter public final String description;
	@Getter public final String usage;
	@Getter public final boolean moderatorOnly;

	@Getter @Setter public CommandData commandData;

	public Command(String name, String description, String usage, boolean moderatorOnly) {
		this.name = name;
		this.description = description;
		this.usage = usage;
		this.moderatorOnly = moderatorOnly;
	}

	public abstract void execute(CommandEvent event);
}