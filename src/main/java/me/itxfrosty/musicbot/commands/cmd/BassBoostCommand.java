package me.itxfrosty.musicbot.commands.cmd;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.audio.guild.GuildAudioManager;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.commands.SlashCommand;
import me.itxfrosty.musicbot.utils.NumberUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class BassBoostCommand extends SlashCommand {
	private final GuildAudioManager musicManager;

	public BassBoostCommand(final MusicBot musicBot) {
		super("bassboost", "BassBoost the song to the amount you want.","/bassboost",false);

		this.addOption(new OptionData(OptionType.STRING, "input", "Amount to Bassboost.", true));

		musicManager = musicBot.getGuildAudioManager();
	}

	private static final float[] BASS_BOOST = {0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f};

	@Override
	public void execute(CommandEvent event) {
		final Member member = event.getMember();
		if (member == null || member.getVoiceState() == null || !member.getVoiceState().inVoiceChannel() || member.getVoiceState().getChannel() == null) {
			event.getEvent().getHook().sendMessageEmbeds(new EmbedBuilder().setDescription("Please connect to a voice channel first!").build()).queue();
			return;
		}

		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}

		final double input = Double.parseDouble(event.getEvent().getCommandString().replace("/bassboost input: ", ""));

		if (input == 50|| input == 0) {
			musicManager.getGuildAudio(event.getGuild()).getAudioPlayer().setFilterFactory(null);
			event.reply(new EmbedBuilder().setDescription("Bassboost has been disabled.").build()).queue();
			return;
		}

		if (input >= 0 || input <= 500) {
			event.reply(new EmbedBuilder().setDescription("The bassboost is out of range! [0-500]").build()).queue();
			return;
		}

		final EqualizerFactory equalizer = new EqualizerFactory();

		for (int i = 0; i < BASS_BOOST.length; i++) {
			equalizer.setGain(i, (float) (BASS_BOOST[i] + (input / 100)));
		}

		musicManager.getGuildAudio(event.getGuild()).getAudioPlayer().setFilterFactory(equalizer);

		event.reply(new EmbedBuilder().setDescription("Bassboost has been applied, at the level " + input + "%.").build()).queue();
	}
}
