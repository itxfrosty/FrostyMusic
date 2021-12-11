package me.itxfrosty.frostymusic.commands.cmd;

import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.MusicManager;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class VolumeCommand extends SlashCommand {
	private final MusicManager musicManager;

	public VolumeCommand(final FrostyMusic musicBot) {
		super("volume", "Set's the Volume of the Audio Player.", "/volume <volume>", false);
		this.musicManager = musicBot.getGuildAudioManager();

		this.addOption(new OptionData(OptionType.STRING,"value","The value to set the volume to.", true));
	}

	@Override
	public void execute(CommandEvent event, String args) {
		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}

		String volume = event.getEvent().getCommandString().replace("/volume value: ", "");

		if (Integer.parseInt(volume) >= 401 || Integer.parseInt(volume) <= -1) {
			event.reply(new EmbedBuilder().setDescription("The volume is out of range! [0-400]").build()).queue();
			return;
		}

		try {
			musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().setVolume(Integer.parseInt(volume));
		} catch (IndexOutOfBoundsException e) {
			event.reply(new EmbedBuilder().setDescription("Please specify a volume!").build()).queue();
		} catch (NumberFormatException e) {
			event.reply(new EmbedBuilder().setDescription("That is not a number!").build()).queue();
		}

		event.reply(new EmbedBuilder().setDescription("🔈 Set volume to " + volume + "!").build()).queue();
	}
}
