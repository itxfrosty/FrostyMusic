package me.itxfrosty.musicbot.utils;

public class NumberUtil {

	public static boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
		} catch(NumberFormatException | NullPointerException e) {
			return false;
		}
		return true;
	}

	public static boolean isDouble(String string) {
		try {
			Double.parseDouble(string);
		} catch(NumberFormatException | NullPointerException e) {
			return false;
		}
		return true;
	}
}