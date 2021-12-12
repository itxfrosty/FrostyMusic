package me.itxfrosty.frostymusic.objects;

public enum AppleMusicType {

	ALBUM,
	PLAYLIST
	;

	/**
	 * Get's the AppleMusic Type of the String.
	 *
	 * @param link String of the message.
	 * @return Type of AppleMusic Song.
	 */
	public static AppleMusicType getAppleMusicType(String link) {
		if (link.contains("music.apple.com/library/playlist/")) {
			return AppleMusicType.PLAYLIST;
		} else if (link.contains("music.apple.com/us/album/")) {
			return AppleMusicType.ALBUM;
		}

		System.err.println("Something went wrong!");
		return null;
	}

}
