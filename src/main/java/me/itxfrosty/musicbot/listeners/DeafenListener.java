package me.itxfrosty.musicbot.listeners;

import me.itxfrosty.musicbot.MusicBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DeafenListener extends ListenerAdapter {
	private final MusicBot musicBot;

	public DeafenListener(MusicBot musicBot) {
		this.musicBot = musicBot;
	}

	@Override
	public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
		final Member bot = event.getGuild().getMember(event.getJDA().getSelfUser());

		if (event.getMember().equals(bot)) {
			bot.deafen(true).queue();
		}
	}

	@Override
	public void onGuildVoiceGuildDeafen(@NotNull GuildVoiceGuildDeafenEvent event) {
		final Member bot = event.getGuild().getMember(event.getJDA().getSelfUser());

		if (event.getMember().equals(bot)) {
			if (event.isGuildDeafened()) {
				return;
			}

			musicBot.getGuildAudioManager().getGuildAudio(event.getGuild()).getTrackScheduler().getLogChannel().sendMessageEmbeds(new EmbedBuilder().setDescription(("Please do not undeafen the bot!")).build()).queue();

			bot.deafen(true).queue();
		}
	}
}
