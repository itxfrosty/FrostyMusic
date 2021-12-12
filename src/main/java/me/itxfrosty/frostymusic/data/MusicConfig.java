package me.itxfrosty.frostymusic.data;

import me.itxfrosty.frostymusic.FrostyMusic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class MusicConfig {
	private static final Logger logger = LoggerFactory.getLogger(FrostyMusic.class);

	public static final Properties PROPERTIES = loadProperties();

	/* Discord Bot Version */
	public static final String VERSION = PROPERTIES.getProperty("version");

	/* Discord Account Token */
	public static final String TOKEN = PROPERTIES.getProperty("token");

	/* Spotify Login */
	public static final String SPOTIFY_CLIENT_ID = PROPERTIES.getProperty("spotify_client_ID");
	public static final String SPOTIFY_CLIENT_SECRET = PROPERTIES.getProperty("spotify_client_SECRET");

	public static final String APPLE_TOKEN = PROPERTIES.getProperty("apple_token");


	/**
	 * Load's the Properties File.
	 *
	 * @return Properties.
	 */
	private static Properties loadProperties() {
		final Properties properties = new Properties();
		final Path path = Paths.get("bot.properties");

		try (final BufferedReader is = Files.newBufferedReader(path)) {
			properties.load(is);
		} catch (IOException e) {
			saveProperties(properties);
		}

		return properties;
	}

	private static void saveProperties(Properties properties) {
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
