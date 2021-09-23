package me.itxfrosty.musicbot.commands.cmd;

import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.audio.guild.GuildAudioManager;
import me.itxfrosty.musicbot.commands.CommandEvent;
import me.itxfrosty.musicbot.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

public class ClearCommand extends SlashCommand {
	private final GuildAudioManager musicManager;
	public ClearCommand(final MusicBot musicBot) {
		super("clear","Clear's queue of queued songs.","/clear",false);

		musicManager = musicBot.getGuildAudioManager();
	}

	@Override
	public void execute(CommandEvent event) {
		final Member member = event.getMember();
		if (member == null || member.getVoiceState() == null || !member.getVoiceState().inVoiceChannel() || member.getVoiceState().getChannel() == null) {
			event.getEvent().getHook().sendMessageEmbeds(new EmbedBuilder().setDescription("Please connect to a voice channel first!").build()).queue();
			return;
		}

		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs to clear.").build()).queue();
			return;
		}

		musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().clearQueue();

		event.reply(new EmbedBuilder().setDescription("You have cleared the queue!").build()).queue();
	}
}
