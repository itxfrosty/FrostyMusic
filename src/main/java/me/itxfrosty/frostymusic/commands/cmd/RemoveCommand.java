package me.itxfrosty.frostymusic.commands.cmd;

import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.MusicManager;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class RemoveCommand extends SlashCommand {
	private final MusicManager musicManager;

	public RemoveCommand(final FrostyMusic frostyMusic) {
		super("remove", "Removes the song from the queue","/remove <number>", false);

		this.musicManager = frostyMusic.getGuildAudioManager();

		this.addOption(new OptionData(OptionType.STRING, "input", "Song from the queue to remove.", true));
	}

	@Override
	public void execute(CommandEvent event, String args) {
		if (musicManager.getGuildAudio(event.getGuild()) == null || musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing!").build()).queue();
			return;
		}

		String message = event.getEvent().getCommandString().replace("/remove input: ","");

		try {
			if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() < Integer.parseInt(message)) {
				event.reply(new EmbedBuilder().setDescription("There is no song at this input.").build()).queue();
				return;
			}

			if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().removeSong(Integer.parseInt(message))) {
				event.reply(new EmbedBuilder().setDescription("Removed Song!").build()).queue();
			} else {
				event.reply(new EmbedBuilder().setDescription("There is no song at this input.").build()).queue();
			}

		} catch (Exception e) {
			event.reply(new EmbedBuilder().setDescription("Input is not a number. Please specify a number you want to remove.").build()).queue();
		}


	}
}