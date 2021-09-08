package me.itxfrosty.musicbot;

import lombok.Getter;
import me.itxfrosty.musicbot.commands.cmd.*;
import me.itxfrosty.musicbot.listeners.CommandListener;
import me.itxfrosty.musicbot.managers.BotManager;
import me.itxfrosty.musicbot.managers.YoutubeManager;
import me.itxfrosty.musicbot.managers.audio.MusicManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class MusicBot {

	@Getter private final JDA jda;

	@Getter private final BotManager botManager;
	@Getter private final MusicManager musicManager;
	@Getter private final YoutubeManager youtubeManager;

	@Getter private static MusicBot instance;

	/**
	 * Public Constructor for MusicBot.
	 *
	 * @throws LoginException Bad Token.
	 * @throws InterruptedException Bad Connection.
	 */
	public MusicBot() throws LoginException, InterruptedException {
		instance = this;
		botManager = new BotManager();

		JDABuilder builder = JDABuilder.createDefault("ODI3Mzg0NDg5MzE0NjE1MzM4.YGaP2g.qMRX_wUnVQjHfFOkAxKDl8X41xE");

		builder.setStatus(OnlineStatus.ONLINE);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS);

		jda = builder.build().awaitReady();

		/*
		CommandListUpdateAction commands = jda.updateCommands();

		commands.addCommands(
				new CommandData("play","Play's Song from either link or keywords.").addOption(OptionType.STRING,"input","A search term or link.",true),
				new CommandData("volume","Set's the Volume of the Audio Player.").addOption(OptionType.STRING,"value","The value to set the volume to.",true),
				new CommandData("skip","Skip's Song and play's next one in queue."),
				new CommandData("queue","Display's a queue of songs."),
				new CommandData("join","Makes the bot join the voice channel."),
				new CommandData("leave","Leaves the voice channel.")
		).queue();

		 */

		musicManager = new MusicManager();
		youtubeManager = new YoutubeManager(this);

	}

	/**
	 * Initializes bot.
	 *
	 * @param args Ignored.
	 */
	public static void main(String[] args) {
		MusicBot musicBot = null;
		
		try {
			musicBot = new MusicBot();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}

		assert musicBot != null;
		musicBot.getBotManager().registerEventListener(new CommandListener());

		musicBot.getBotManager().registerCommands(
				musicBot.getJda(),
				new PlayCommand(musicBot),
				new SkipCommand(musicBot),
				new QueueCommand(musicBot),
				new LeaveCommand(musicBot),
				new VolumeCommand(musicBot),
				new JoinCommand(musicBot),
				new ShuffleCommand(musicBot));

		musicBot.getBotManager().addListeners(musicBot.getJda());
	}
}