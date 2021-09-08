package me.itxfrosty.musicbot.managers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import me.itxfrosty.musicbot.MusicBot;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeManager {

	private final String key;
	private final YouTube.Search.List search;

	public YoutubeManager(final MusicBot bot) {
		try {
			key = "AIzaSyBNCghdooasfVs2syNtPiyM_f7n3zLII_U";

			YouTube youtube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), new JacksonFactory(),
					request -> {
					}).setApplicationName("mb-discord-bot").build();

			List<String> properties = new ArrayList<>();

			properties.add("id");
			properties.add("snippet");

			search = youtube.search().list(properties);
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
}