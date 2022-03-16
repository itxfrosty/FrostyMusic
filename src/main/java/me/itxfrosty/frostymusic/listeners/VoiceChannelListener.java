package me.itxfrosty.frostymusic.listeners;

import me.itxfrosty.frostymusic.FrostyMusic;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class VoiceChannelListener extends ListenerAdapter {

	private final FrostyMusic frostyMusic;

	public VoiceChannelListener(final FrostyMusic frostyMusic) {
		this.frostyMusic = frostyMusic;
	}

	@Override
	public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
		if (this.botCheck(event)) {
			for (int i = 0; i < 5400; i++) {
				try {
					Thread.sleep(5000);
				} catch (Exception ignore) {}
			}

			if (event.getChannelLeft().getMembers().isEmpty()) {
				frostyMusic.getGuildAudioManager().leaveVoiceChannel(event.getGuild());
			}
		}
	}

	private boolean botCheck(final GuildVoiceLeaveEvent event) {
		AtomicBoolean check = new AtomicBoolean(false);
		event.getChannelLeft().getMembers().forEach((member) -> {
			if (member.getIdLong() == 887534020840288277L) {
				check.set(true);
			}
		});

		return check.get();
	}

}
