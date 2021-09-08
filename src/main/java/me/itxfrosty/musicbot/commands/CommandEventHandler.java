package me.itxfrosty.musicbot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

/**
 * Record for handling the {@link me.itxfrosty.musicbot.commands.CommandEvent CommandEvent}.
 *
 * @author itxfrosty
 */
public record CommandEventHandler(SlashCommandEvent event) implements CommandEvent {

	@Override
	public SlashCommandEvent getEvent() {
		return event;
	}
}