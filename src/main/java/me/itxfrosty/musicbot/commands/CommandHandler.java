package me.itxfrosty.musicbot.commands;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.commands.cmd.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandHandler extends ListenerAdapter {

	public final List<SlashCommand> commandList = new ArrayList<>();

	public CommandHandler(MusicBot musicBot) {
		this.registerCommands(
				new PlayCommand(musicBot),
				new SkipCommand(musicBot),
				new QueueCommand(musicBot),
				new LeaveCommand(musicBot),
				new VolumeCommand(musicBot),
				new JoinCommand(musicBot),
				new ShuffleCommand(musicBot),
				new PauseCommand(musicBot),
				new NPCommand(musicBot),
				new ResumeCommand(musicBot),
				new SeekCommand(musicBot),
				new SkipToCommand(musicBot),
				new ClearCommand(musicBot),
				new BassBoostCommand(musicBot));
	}

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

	/**
	 * Record for handling the {@link CommandEvent CommandEvent}.
	 *
	 * @author itxfrosty
	 */
	public static class CommandEventHandler implements CommandEvent {
		public SlashCommandEvent event;

		public CommandEventHandler(SlashCommandEvent event) {
			this.event = event;
		}

		@Override
		public SlashCommandEvent getEvent() {
			return event;
		}
	}

	/**
	 * Listen's for slash command's and executes them.
	 */
	@Override
	public void onSlashCommand(@NotNull SlashCommandEvent event) {
		final User user = event.getUser();
		final Member member = event.getMember();

		if (user.isBot()) return;

		for (SlashCommand command : this.commandList) {
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

				event.deferReply().queue();
				command.execute(new CommandEventHandler(event));

				break;
			}
		}
	}
}