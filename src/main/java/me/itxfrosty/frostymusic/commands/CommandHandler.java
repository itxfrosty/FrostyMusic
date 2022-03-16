package me.itxfrosty.frostymusic.commands;

import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.commands.cmd.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandHandler extends ListenerAdapter {
	private final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

	public final List<SlashCommand> commandList = new ArrayList<>();

	public CommandHandler(FrostyMusic musicBot) {
		this.logger.info("Registering Commands...");
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
				new BassBoostCommand(musicBot),
				new LoopCommand(musicBot),
				new RemoveCommand(musicBot),
				new RestartCommand(musicBot),
				new MoveCommand(musicBot),
				new BackCommand(musicBot));
		this.logger.info("Registered Commands!");
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

				if (command.getOptionData().isEmpty()) {
					command.execute(new CommandEventHandler(event), null);
				} else {
					command.execute(new CommandEventHandler(event), event.getCommandString().replace("/" + command.name + " " + command.optionData.get(0).getName() + ": ", ""));
				}

				break;
			}
		}
	}
}