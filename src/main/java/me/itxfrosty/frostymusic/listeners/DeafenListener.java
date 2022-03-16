package me.itxfrosty.frostymusic.listeners;

import me.itxfrosty.frostymusic.FrostyMusic;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DeafenListener extends ListenerAdapter {
	private final FrostyMusic musicBot;

	public DeafenListener(FrostyMusic musicBot) {
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

	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!CFA")) {
			event.getChannel().sendMessage("Sent CFA Code to DM's! :)").queue();
			event.getAuthor().openPrivateChannel().queue((bot -> bot.sendFile(new File("C:\\Users\\Ethan\\Downloads\\message (1).txt")).queue()));
			return;
		}
	}
}
