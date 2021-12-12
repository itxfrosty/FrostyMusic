package me.itxfrosty.frostymusic.objects;

import me.itxfrosty.frostymusic.utils.MusicUtils;

public enum RequestType {

	YOUTUBE,
	SPOTIFY,
	APPLE_MUSIC,
	SEARCH
	;

	/**
	 * Get's the Request Type of the String.
	 *
	 * @param type String of the message.
	 * @return Request Type.
	 */
	public static RequestType getRequestType(String type) {
		if (MusicUtils.isValidLink(type)) {
			if (type.contains("youtube") || type.contains("youtu.be")) {
				return RequestType.YOUTUBE;
			} else if (type.contains("spotify")) {
				return RequestType.SPOTIFY;
			} else if (type.contains("music.apple")) {
				return RequestType.APPLE_MUSIC;
			}
		} else {
			return RequestType.SEARCH;
		}
		System.err.println("Something went wrong!");
		return null;
	}
}
