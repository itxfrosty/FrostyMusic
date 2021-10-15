package me.itxfrosty.frostymusic.commands.cmd;

import me.itxfrosty.frostymusic.FrostyMusic;
import me.itxfrosty.frostymusic.audio.guild.GuildAudioManager;
import me.itxfrosty.frostymusic.commands.CommandEvent;
import me.itxfrosty.frostymusic.commands.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class SkipCommand extends SlashCommand {
	private final GuildAudioManager musicManager;

	public SkipCommand(@NotNull FrostyMusic musicBot) {
		super("skip","Skip's Song and play's next one in queue.","/skip", false);
		this.musicManager = musicBot.getGuildAudioManager();
	}

	@Override
	public void execute(CommandEvent event) {
		if (event.getMember() == null || event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel() || event.getMember().getVoiceState().getChannel() == null) {
			event.reply(new EmbedBuilder().setDescription("Skipped to the next song!").build()).queue();
			return;
		}

		Member member = event.getMember();

		if (member == null || member.getVoiceState() == null || !member.getVoiceState().inVoiceChannel() || member.getVoiceState().getChannel() == null) {
			event.reply(new EmbedBuilder().setDescription("Please use `/join` first!").build()).queue();
			return;
		}

		if (musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().getTrackQueue().size() == 0) {
			event.reply(new EmbedBuilder().setDescription("There are no songs playing.").build()).queue();
			return;
		}

		event.reply(new EmbedBuilder().setDescription("Skipped to the next song.").build()).queue();

		musicManager.getGuildAudio(event.getGuild()).getTrackScheduler().skip();
	}
}
