package me.itxfrosty.musicbot.utils;

public class ConsoleMessage {

	/**
	 * Sends a message to console
	 *
	 * @param message message to be sent to console
	 */
	public static void log(String message) {
		System.out.println("[Music Bot] " + message);
	}

	/**
	 * Sends a message to console with plugin name at the start
	 *
	 * @param plugin name of the plugin
	 * @param message message to be sent to console
	 */
	public static void log(String plugin, String message) {
		System.out.println(ConsoleMessage.YELLOW + "[" + plugin + "] " + message + RESET);
	}

	public static final String RESET = "\033[0m";			// RESET
	public static final String BLACK = "\033[0;30m";		// BLACK
	public static final String RED = "\033[0;31m";			// RED
	public static final String GREEN = "\033[0;32m";		// GREEN
	public static final String YELLOW = "\033[0;33m";		// YELLOW
	public static final String BLUE = "\033[0;34m";			// BLUE
	public static final String PURPLE = "\033[0;35m";		// PURPLE
	public static final String CYAN = "\033[0;36m";			// CYAN
	public static final String WHITE = "\033[0;37m";		// WHITE

}