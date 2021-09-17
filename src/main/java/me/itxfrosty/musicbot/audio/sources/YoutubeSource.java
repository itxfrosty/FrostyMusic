package me.itxfrosty.musicbot.audio.sources;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import me.itxfrosty.musicbot.MusicBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YoutubeSource {

	private final String key;
	private YouTube youTube;
	private final YouTube.Search.List search;

	private final List<String> properties = Arrays.asList("id", "snippet");
	private final List<String> videoProperties = Arrays.asList("video");
	private final List<String> IDProperties = Arrays.asList("snippet", "localizations", "contentDetails");

	public YoutubeSource(MusicBot musicBot) {
		key = musicBot.getYamlFile().getString("youtubeToken");

		try {
			youTube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), new JacksonFactory(),
					request -> {
					}).setApplicationName("frostymusic").build();

			search = youTube.search().list(properties);
		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String search(String keywords) {
		try {

			SearchListResponse searchResponse = search
					.setMaxResults(3L)
					.setKey(key)
					.setQ(keywords)
					.execute();

			if (searchResponse.getItems().size() == 0) {
				return null;
			}

			for (SearchResult result : searchResponse.getItems()) {
				ResourceId rId = result.getId();
				if (rId.getKind().equals("youtube#video")) {
					return "https://www.youtube.com/watch?v=" + rId.getVideoId();
				}
			}

		} catch (IOException ignored) {}
		return null;
	}

	public List<String> getPlaylistItemsByLink(String link) throws IOException {
		String[] strings = link.split("list=");
		String id = strings[1];
		String query = "https://youtube.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults=50&playlistId=" + id + "&key=" + key;
		JSONObject jsonObject = readJsonFromUrl(query);
		JSONArray items = jsonObject.getJSONArray("items");
		List<String> videoIds = new ArrayList<>();
		for (int i = 0; i < items.length(); i++) {
			videoIds.add(items.getJSONObject(i).getJSONObject("contentDetails").getString("videoId"));
		}
		return videoIds;
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException {
		try (InputStream is = new URL(url).openStream()) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
			}
			String jsonText = sb.toString();
			return new JSONObject(jsonText);
		}
	}

	/**
	 * Retrieves the next video for the autoplay function
	 *
	 * @param videoId the ID of the prevoius video
	 * @return The new videos SearchResult
	 * @throws IOException          when an IO error occurred
	 * @throws NullPointerException When no video where found
	 */
	public SearchResult retrieveRelatedVideos(String videoId) throws IOException, NullPointerException {
		YouTube.Search.List search = youTube.search().list(properties)
				.setRelatedToVideoId(videoId)
				.setType(videoProperties)
				.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
				.setMaxResults(1L);
		SearchListResponse searchResults = search.execute();
		if (searchResults.getItems().isEmpty())
			throw new NullPointerException("No videos were found");
		return searchResults.getItems().get(0);
	}

	public String getVideoId(String query) throws IOException {
		YouTube.Search.List search = youTube.search().list(properties)
				.setType(videoProperties)
				.setFields("items(id/videoId)")
				.setQ(query);
		SearchListResponse response = search.execute();
		if (response.getItems().isEmpty())
			return "";
		return response.getItems().get(0).getId().getVideoId();
	}

	/**
	 * Search for youtube Videos by it's ide
	 *
	 * @param videoId The id of the video
	 * @return an VideoListResponse {@link com.google.api.services.youtube.model.VideoListResponse}
	 * @throws IOException When YoutubeRequest returns an error
	 */
	public VideoListResponse getVideoById(String videoId) throws IOException {
		return youTube.videos().list(IDProperties).setId(Arrays.asList(videoId)).execute();
	}

	/**
	 * Gets the first video from an VideoListResponse
	 *
	 * @param videoId The yotube video id.
	 * @return The first Video {@link com.google.api.services.youtube.model.Video} of the {@link com.google.api.services.youtube.model.VideoListResponse}
	 * @throws IOException When YoutubeRequest returns an error
	 */
	public Video getFirstVideoById(String videoId) throws IOException {
		return getVideoById(videoId).getItems().get(0);
	}
}
