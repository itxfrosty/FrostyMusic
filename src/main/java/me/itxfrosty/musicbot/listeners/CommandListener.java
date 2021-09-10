package me.itxfrosty.musicbot.listeners;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.Command;
import me.itxfrosty.musicbot.commands.CommandEventHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CommandListener extends ListenerAdapter {

	@Override
	public void onSlashCommand(@NotNull SlashCommandEvent event) {
		final User user = event.getUser();
		final Member member = event.getMember();

		if (user.isBot()) return;

		for (Command command : MusicBot.getInstance().getCommandManager().getCommands()) {
			if (event.getName().equals(command.name)) {
				if (command.isModeratorOnly()) {
					assert member != null;
					if (!member.hasPermission(Permission.MANAGE_SERVER)) {
						event.deferReply().setEphemeral(true).setContent("You must be a moderator to use this command!").queue(hook -> {
							hook.deleteOriginal().queueAfter(5, TimeUnit.SECONDS);
							event.getHook().deleteOriginal().queueAfter(5, TimeUnit.SECONDS);
						});
						return;
					}
				}

				command.execute(new CommandEventHandler(event));
				break;
			}
		}
	}
}