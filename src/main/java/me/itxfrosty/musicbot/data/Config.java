package me.itxfrosty.musicbot.data;

import me.itxfrosty.musicbot.MusicBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
		final File file = new File("bot.properties");

		try {
			final FileInputStream fileInputStream = new FileInputStream(file);

			properties.load(fileInputStream);
		} catch (IOException e) {
			saveProperties(properties, file);
		}


		return properties;
	}

	private static void saveProperties(Properties properties, File file) {
		try (final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("bot.properties")) {
			properties.load(inputStream);

			if (inputStream == null) {
				throw new RuntimeException("Configuration file not found. Exiting.");
			}

			FileOutputStream fr = new FileOutputStream("bot.properties");
			properties.store(fr, null);
			fr.close();
		} catch (IOException err) {
			logger.error("An error occurred while loading configuration file. Exiting.", err);
			throw new RuntimeException(err);
		}

	}

}
