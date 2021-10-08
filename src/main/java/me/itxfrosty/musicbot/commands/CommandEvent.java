package me.itxfrosty.musicbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;

import javax.annotation.Nonnull;

/**
 * Interface for the SlashCommandEvent.
 *
 * @author itxfrosty
 */
public interface CommandEvent {

	/**
	 * Returns the {@link net.dv8tion.jda.api.events.interaction.SlashCommandEvent SlashCommand Event}
	 * that was received for this instance.
	 *
	 * @return Returns the {@link net.dv8tion.jda.api.events.interaction.SlashCommandEvent SlashCommand Event}.
	 */
	SlashCommandEvent getEvent();

	/**
	 * Returns the {@link net.dv8tion.jda.api.JDA JDA} current instance of the JDA class.
	 *
	 * @return Returns the {@link net.dv8tion.jda.api.JDA JDA}
	 */
	default JDA getJDA() {
		return getEvent().getJDA();
	}

	/**
	 * Returns the {@link net.dv8tion.jda.api.entities.Guild Guild} current guild.
	 *
	 * @return Returns the {@link net.dv8tion.jda.api.entities.Guild Guild}.
	 */
	default Guild getGuild() {
		return getEvent().getGuild();
	}

	/**
	 * Returns the {@link net.dv8tion.jda.api.entities.TextChannel Text Channel} current text channel the
	 * command was issued in.
	 *
	 * @return Returns the {@link net.dv8tion.jda.api.entities.TextChannel Text Channel}.
	 */
	default TextChannel getChannel() {
		return (TextChannel) getEvent().getChannel();
	}

	/**
	 * Returns the {@link net.dv8tion.jda.api.entities.User User} current User.
	 *
	 * @return Returns the {@link net.dv8tion.jda.api.entities.User User}.
	 */
	default User getUser() {
		return getEvent().getUser();
	}

	/**
	 * Returns the {@link net.dv8tion.jda.api.entities.Member Member} current Member.
	 *
	 * @return Returns the {@link net.dv8tion.jda.api.entities.Member Member}.
	 */
	default Member getMember() {
		return getEvent().getMember();
	}

	/**
	 * Returns the {@link net.dv8tion.jda.api.entities.User User} user class for this bot account.
	 *
	 * @return Returns the {@link net.dv8tion.jda.api.entities.User User}.
	 */
	default User getSelfUser() {
		return getJDA().getSelfUser();
	}

	/**
	 * Returns the {@link net.dv8tion.jda.api.entities.Member Member} member class for this bot account.
	 *
	 * @return Returns the {@link net.dv8tion.jda.api.entities.Member Member}.
	 */
	default Member getSelfMember() {
		return getGuild().getSelfMember();
	}

	/**
	 * Sends a message as a String.
	 *
	 * @param message String Message to send to User/Member.
	 * @return ReplyAction to queue.
	 */
	default WebhookMessageAction<Message> reply(@Nonnull String message) {
		return getEvent().getHook().sendMessage(message);
	}

	/**
	 * Sends a embed.
	 *
	 * @param messageEmbed Embed Message to send to User/Member.
	 * @return ReplyAction to queue.
	 */
	default WebhookMessageAction<Message> reply(@Nonnull MessageEmbed messageEmbed) {
		return getEvent().getHook().sendMessageEmbeds(messageEmbed);
	}
}