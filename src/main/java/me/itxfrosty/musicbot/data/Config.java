package me.itxfrosty.musicbot.data;

import me.itxfrosty.musicbot.MusicBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static final Logger logger = LoggerFactory.getLogger(MusicBot.class);

	private static final Properties PROPERTIES = loadProperties();

	/* Discord Bot Version */
	public static final String VERSION = PROPERTIES.getProperty("version");

	/* Discord Account Token */
	public static final String TOKEN = PROPERTIES.getProperty("token");

	/* Spotify Login */
	public static final String SPOTIFY_CLIENT_ID = PROPERTIES.getProperty("spotify_client_ID");
	public static final String SPOTIFY_CLIENT_SECRET = PROPERTIES.getProperty("spotify_client_SECRET");


	/**
	 * Load's the Properties File.
	 *
	 * @return Properties.
	 */
	private static Properties loadProperties() {
		final Properties properties = new Properties();
		try (final InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("bot.properties")) {
			if (inputStream == null) {
				throw new RuntimeException("Configuration file not found. Exiting.");
			}
			properties.load(inputStream);
		} catch (final IOException err) {
			logger.error("An error occurred while loading configuration file. Exiting.", err);
			throw new RuntimeException(err);
		}
		return properties;
	}
}
