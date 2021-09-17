package me.itxfrosty.musicbot.launcher;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import me.itxfrosty.musicbot.MusicBot;
import org.apache.hc.core5.http.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Launcher {

	public static void main(String[] args) throws LoginException, InterruptedException, IOException, ParseException, SpotifyWebApiException {
		new MusicBot();
	}
}
