package me.itxfrosty.frostymusic.audio.sources.applemusic;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppleMusicAdapter {

	private static AppleMusicService appleMusicService;

	/**
	 * Get's AppleMusic Service Via HTTP.
	 *
	 * @return AppleMusicService.
	 */
	public static AppleMusicService getAPI() {
		final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

		if (appleMusicService == null) {
			final Retrofit retrofit = new Retrofit.Builder()
					.baseUrl("https://api.music.apple.com")
					.addConverterFactory(GsonConverterFactory.create())
					.client(httpClient.build())
					.build();
			appleMusicService = retrofit.create(AppleMusicService.class);
		}

		return appleMusicService;
	}
}