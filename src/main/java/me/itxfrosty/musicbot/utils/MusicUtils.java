package me.itxfrosty.musicbot.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicUtils {

	public static final int EMBED_COLOR = 0x7289da;

	/**
	 * Get's Time left in audio track.
	 *
	 * @param audioTrack Audio Track to get duration.
	 * @return Duration.
	 */
	public static String getDuration(AudioTrack audioTrack) {
		long msPos = audioTrack.getInfo().length;
		long minPos = msPos / 60000;
		msPos = msPos % 60000;
		int secPos = (int) Math.floor((float) msPos / 1000f);

		return minPos + ":" + ((secPos < 10) ? "0" + secPos : secPos);
	}

	/**
	 * Get's Thumbnail.
	 *
	 * @param audioTrack Audio Track.
	 * @return String of URL
	 */
	public static String getThumbnail(AudioTrack audioTrack) {
		return String.format("https://img.youtube.com/vi/%s/0.jpg", audioTrack.getInfo().uri.substring(32));
	}

}