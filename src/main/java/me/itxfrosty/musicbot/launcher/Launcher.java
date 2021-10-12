package me.itxfrosty.musicbot.launcher;

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import me.itxfrosty.musicbot.MusicBot;
import me.itxfrosty.musicbot.data.MusicConfig;
import net.dv8tion.jda.api.JDAInfo;

import javax.security.auth.login.LoginException;

public class Launcher {

	public static void main(String[] args) {
		System.out.println(getVersionInfo());

		try {
			new MusicBot();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static String getVersionInfo() {
		return  " ______             _           __  __           _      \n" +
				"|  ____|           | |         |  \\/  |         (_)     \n" +
				"| |__ _ __ ___  ___| |_ _   _  | \\  / |_   _ ___ _  ___ \n" +
				"|  __| '__/ _ \\/ __| __| | | | | |\\/| | | | / __| |/ __|\n" +
				"| |  | | | (_) \\__ \\ |_| |_| | | |  | | |_| \\__ \\ | (__ \n" +
				"|_|  |_|  \\___/|___/\\__|\\__, | |_|  |_|\\__,_|___/_|\\___|\n" +
				"                         __/ |                          \n" +
				"                        |___/                           " +
				"\n"
				+ "\nLoading Frosty Music..."
				+ "\n"
				+ "\nBot Version:      " + MusicConfig.VERSION
				+ "\nJVM:              " + System.getProperty("java.version")
				+ "\nJDA:              " + JDAInfo.VERSION
				+ "\nLavaplayer:       " + PlayerLibrary.VERSION
				+ "\nCreated By:       " + "itxfrosty"
				+ "\n";
	}
}