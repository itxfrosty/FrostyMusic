package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.audio.guild.GuildAudioManager;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class VolumeCommand extends SlashCommand {
	private final GuildAudioManager musicManager;

	public VolumeCommand(final MusicBot musicBot) {
		super("volume", "Set's the Volume of the Audio Player.", "/volume <volume>", false);
		this.musicManager = musicBot.getGuildAudioManager();

		getOptionData().add(new OptionData(OptionType.STRING,"value","The value to set the volume to.", true));
	}

	@Override
	public void execute(CommandEvent event) {
		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}

		String volume = event.getEvent().getCommandString().replace("/volume value: ", "");
		System.out.println(volume);

		if (Integer.parseInt(volume) >= 400 || Integer.parseInt(volume) <= 50) {
			event.reply(new EmbedBuilder().setDescription("The volume is out of range! [50-400]").build()).queue();
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
