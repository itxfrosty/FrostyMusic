package me.itxfrosty.musicbot.listeners;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.SlashCommand;
import me.itxfrosty.musicbot.commands.SlashCommandManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReadyListener extends ListenerAdapter {
	private final MusicBot musicBot;

	public ReadyListener(MusicBot musicBot) {
		this.musicBot = musicBot;
	}

	@Override
	public void onGuildReady(@NotNull GuildReadyEvent event) {
		this.registerSlashCommands(event.getGuild());
	}

	@Override
	public void onGuildJoin(@NotNull GuildJoinEvent event) {
		this.registerSlashCommands(event.getGuild());
	}

	private void registerSlashCommands(Guild guild) {
		final CommandListUpdateAction action = guild.updateCommands();
		final List<CommandData> commandDataList = new ArrayList<>();

		for (SlashCommand command : musicBot.getSlashCommandManager().getCommands()) {
			CommandData commandData = new CommandData(command.getName(), command.getDescription());

			if (!command.getOptionData().isEmpty()) {
				command.getOptionData().forEach(optionData -> commandData.addOption(optionData.getType(), optionData.getName(), optionData.getDescription(), optionData.isRequired()));
			}

			commandDataList.add(commandData);
		}

		action.addCommands(commandDataList).queue();
	}

}
