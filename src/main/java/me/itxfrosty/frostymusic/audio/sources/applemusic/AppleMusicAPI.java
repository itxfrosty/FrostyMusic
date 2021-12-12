package me.itxfrosty.frostymusic.audio.sources.applemusic;

import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.activity.Activities;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.activity.Activity;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.album.Album;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.album.Albums;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.applecurators.AppleCurator;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.applecurators.AppleCurators;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.artist.Artist;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.artist.Artists;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.chart.Charts;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.curator.Curator;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.curator.Curators;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.genre.Genre;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.genre.Genres;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.musicvideo.MusicVideo;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.musicvideo.MusicVideos;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.playlist.PlayList;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.playlist.PlayLists;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.search.Hint;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.search.Searches;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.song.Song;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.song.Songs;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.station.Station;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.station.Stations;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.storefront.StoreFront;
import me.itxfrosty.frostymusic.audio.sources.applemusic.entites.storefront.StoreFronts;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;

public class AppleMusicAPI {

	private final String token;

	public AppleMusicAPI(@NotNull String token) {
		this.token = token;
	}

	public void getActivity(String storefront, String id, Callback<Activity> callback) {
		Call<Activity> call = AppleMusicAdapter.getAPI().getActivity(token, storefront, id);
		call.enqueue(callback);
	}

	public void getActivity(String storefront, String id, String language, Callback<Activity> callback) {
		Call<Activity> call = AppleMusicAdapter.getAPI().getActivity(token, storefront, id, language);
		call.enqueue(callback);
	}

	public void getMultipleActivity(String storefront, List<String> ids, Callback<Activities> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Activities> call = AppleMusicAdapter.getAPI().getMultipleActivity(token, storefront, formatIds);
		call.enqueue(callback);
	}

	public void getMultipleActivity(String storefront, List<String> ids, String language, Callback<Activities> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Activities> call = AppleMusicAdapter.getAPI().getMultipleActivity(token, storefront, formatIds, language);
		call.enqueue(callback);
	}

	public void getAlbum(String storefront, String id, Callback<Album> callback) {
		Call<Album> call = AppleMusicAdapter.getAPI().getAlbum(token, storefront, id);
		call.enqueue(callback);
	}

	public void getAlbum(String storefront, String id, String language, Callback<Album> callback) {
		Call<Album> call = AppleMusicAdapter.getAPI().getAlbum(token, storefront, id, language);
		call.enqueue(callback);
	}

	public void getMultipleAlbum(String storefront, List<String> ids, Callback<Albums> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Albums> call = AppleMusicAdapter.getAPI().getMultipleAlbum(token, storefront, formatIds);
		call.enqueue(callback);
	}

	public void getMultipleAlbum(String storefront, List<String> ids, String language, Callback<Albums> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Albums> call = AppleMusicAdapter.getAPI().getMultipleAlbum(token, storefront, formatIds, language);
		call.enqueue(callback);
	}

	public void getAppleCurator(String storefront, String id, Callback<AppleCurator> callback) {
		Call<AppleCurator> call = AppleMusicAdapter.getAPI().getAppleCurator(token, storefront, id);
		call.enqueue(callback);
	}

	public void getAppleCurator(String storefront, String id, String language, Callback<AppleCurator> callback) {
		Call<AppleCurator> call = AppleMusicAdapter.getAPI().getAppleCurator(token, storefront, id, language);
		call.enqueue(callback);
	}

	public void getMultipleAppleCurator(String storefront, List<String> ids, Callback<AppleCurators> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<AppleCurators> call = AppleMusicAdapter.getAPI().getMultipleAppleCurator(token, storefront, formatIds);
		call.enqueue(callback);
	}

	public void getMultipleAppleCurator(String storefront, List<String> ids, String language, Callback<AppleCurators> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<AppleCurators> call = AppleMusicAdapter.getAPI().getMultipleAppleCurator(token, storefront, formatIds, language);
		call.enqueue(callback);
	}

	public void getArtist(String storefront, String id, Callback<Artist> callback) {
		Call<Artist> call = AppleMusicAdapter.getAPI().getArtist(token, storefront, id);
		call.enqueue(callback);
	}

	public void getArtist(String storefront, String id, String language, Callback<Artist> callback) {
		Call<Artist> call = AppleMusicAdapter.getAPI().getArtist(token, storefront, id, language);
		call.enqueue(callback);
	}

	public void getMultipleArtist(String storefront, List<String> ids, Callback<Artists> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Artists> call = AppleMusicAdapter.getAPI().getMultipleArtist(token, storefront, formatIds);
		call.enqueue(callback);
	}

	public void getMultipleArtist(String storefront, List<String> ids, String language, Callback<Artists> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Artists> call = AppleMusicAdapter.getAPI().getMultipleArtist(token, storefront, formatIds, language);
		call.enqueue(callback);
	}

	public void getCharts(String storefront, List<String> types, String language,
						  Integer genre, String chart, Integer limit, String offset, Callback<Charts> callback) {
		String formatTypes = "";
		for (String type : types) {
			formatTypes = formatTypes + type + ",";
		}
		formatTypes = formatTypes.substring(0, formatTypes.length()-1);
		Call<Charts> call =AppleMusicAdapter.getAPI().getCharts(token, storefront, formatTypes,
				language, genre, chart, limit, offset);
		call.enqueue(callback);
	}

	public void getCurator(String storefront, String id, Callback<Curator> callback) {
		Call<Curator> call = AppleMusicAdapter.getAPI().getCurator(token, storefront, id);
		call.enqueue(callback);
	}

	public void getCurator(String storefront, String id, String language, Callback<Curator> callback) {
		Call<Curator> call = AppleMusicAdapter.getAPI().getCurator(token, storefront, id, language);
		call.enqueue(callback);
	}

	public void getMultipleCurator(String storefront, List<String> ids, Callback<Curators> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Curators> call = AppleMusicAdapter.getAPI().getMultipleCurator(token, storefront, formatIds);
		call.enqueue(callback);
	}

	public void getMultipleCurator(String storefront, List<String> ids, String language, Callback<Curators> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Curators> call = AppleMusicAdapter.getAPI().getMultipleCurator(token, storefront, formatIds, language);
		call.enqueue(callback);
	}

	public void getTopChartsGenres(String storefront, String language, Integer limit, String offset, Callback<Genres> callback) {
		Call<Genres> call = AppleMusicAdapter.getAPI().getTopChartsGenres(token, storefront, language, limit, offset);
		call.enqueue(callback);
	}

	public void getGenre(String storefront, String id, String language, Callback<Genre> callback) {
		Call<Genre> call = AppleMusicAdapter.getAPI().getGenre(token, storefront, id, language);
		call.enqueue(callback);
	}

	public void getMultipleGenre(String storefront, List<String> ids, String language, Callback<Genres> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Genres> call = AppleMusicAdapter.getAPI().getMultipleGenre(token, storefront, formatIds, language);
		call.enqueue(callback);
	}

	public void getMusicVideo(String storefront, String id, Callback<MusicVideo> callback) {
		Call<MusicVideo> call = AppleMusicAdapter.getAPI().getMusicVideo(token, storefront, id);
		call.enqueue(callback);
	}

	public void getMusicVideo(String storefront, String id, String language, Callback<MusicVideo> callback) {
		Call<MusicVideo> call = AppleMusicAdapter.getAPI().getMusicVideo(token, storefront, id, language);
		call.enqueue(callback);
	}

	public void getMultipleMusicVideo(String storefront, List<String> ids, Callback<MusicVideos> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<MusicVideos> call = AppleMusicAdapter.getAPI().getMultipleMusicVideo(token, storefront, formatIds);
		call.enqueue(callback);
	}

	public void getMultipleMusicVideo(String storefront, List<String> ids, String language, Callback<MusicVideos> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<MusicVideos> call = AppleMusicAdapter.getAPI().getMultipleMusicVideo(token, storefront, formatIds, language);
		call.enqueue(callback);
	}

	public void getPlayList(String storefront, String id, Callback<PlayList> callback) {
		Call<PlayList> call = AppleMusicAdapter.getAPI().getPlayList(token, storefront, id);
		call.enqueue(callback);
	}

	public void getPlayList(String storefront, String id, String language, Callback<PlayList> callback) {
		Call<PlayList> call = AppleMusicAdapter.getAPI().getPlayList(token, storefront, id, language);
		call.enqueue(callback);
	}

	public void getMultiplePlayList(String storefront, List<String> ids, Callback<PlayLists> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<PlayLists> call = AppleMusicAdapter.getAPI().getMultiplePlayList(token, storefront, formatIds);
		call.enqueue(callback);
	}

	public void getMultiplePlayList(String storefront, List<String> ids, String language, Callback<PlayLists> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<PlayLists> call = AppleMusicAdapter.getAPI().getMultiplePlayList(token, storefront, formatIds, language);
		call.enqueue(callback);
	}

	public void search(String storefront, String term, String language, Integer limit,
					   String offset, List<String> types, Callback<Searches> callback) {
		String formatTerm = "";
		if (term != null) {
			formatTerm = term.replace(" ", "+").toLowerCase();
		} else {
			formatTerm = null;
		}
		String formatTypes = "";
		if (types != null) {

			for (String id : types) {
				formatTypes = formatTypes + id + ",";
			}
			formatTypes = formatTypes.substring(0, formatTypes.lastIndexOf(","));
		} else {
			formatTypes = null;
		}

		Call<Searches> call = AppleMusicAdapter.getAPI().search(token, storefront, formatTerm,
				language, limit, offset, formatTypes);
		call.enqueue(callback);
	}

	public void searchHint(String storefront, String term, String language, Integer limit, String offset,
						   List<String> types, Callback<Hint> callback) {
		String formatTerm = "";
		if (term != null) {
			formatTerm = term.replace(" ", "+").toLowerCase();
		} else {
			formatTerm = null;
		}
		String formatTypes = "";
		if (types != null) {

			for (String id : types) {
				formatTypes = formatTypes + id + ",";
			}
			formatTypes = formatTypes.substring(0, formatTypes.lastIndexOf(","));
		} else {
			formatTypes = null;
		}
		Call<Hint> call = AppleMusicAdapter.getAPI().searchHint(token, storefront, formatTerm,
				language, limit, offset, formatTypes);
		call.enqueue(callback);
	}

	public void getSong(String storefront, String id, Callback<Song> callback) {
		Call<Song> call = AppleMusicAdapter.getAPI().getSong(token, storefront, id);
		call.enqueue(callback);
	}

	public void getSong(String storefront, String id, String language, Callback<Song> callback) {
		Call<Song> call = AppleMusicAdapter.getAPI().getSong(token, storefront, id, language);
		call.enqueue(callback);
	}

	public void getMultipleSong(String storefront, List<String> ids, Callback<Songs> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Songs> call = AppleMusicAdapter.getAPI().getMultipleSong(token, storefront, formatIds);
		call.enqueue(callback);
	}

	public void getMultipleSong(String storefront, List<String> ids, String language, Callback<Songs> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Songs> call = AppleMusicAdapter.getAPI().getMultipleSong(token, storefront, formatIds, language);
		call.enqueue(callback);
	}

	public void getStation(String storefront, String id, Callback<Station> callback) {
		Call<Station> call = AppleMusicAdapter.getAPI().getStation(token, storefront, id);
		call.enqueue(callback);
	}

	public void getStation(String storefront, String id, String language, Callback<Station> callback) {
		Call<Station> call = AppleMusicAdapter.getAPI().getStation(token, storefront, id, language);
		call.enqueue(callback);
	}

	public void getMultipleStation(String storefront, List<String> ids, Callback<Stations> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Stations> call = AppleMusicAdapter.getAPI().getMultipleStation(token, storefront, formatIds);
		call.enqueue(callback);
	}

	public void getMultipleStation(String storefront, List<String> ids, String language, Callback<Stations> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<Stations> call =AppleMusicAdapter.getAPI().getMultipleStation(token, storefront, formatIds, language);
		call.enqueue(callback);
	}

	public void getStoreFront(String id, Callback<StoreFront> callback) {
		Call<StoreFront> call = AppleMusicAdapter.getAPI().getStoreFront(token, id);
		call.enqueue(callback);
	}

	public void getStoreFront(String id, String language, Callback<StoreFront> callback) {
		Call<StoreFront> call = AppleMusicAdapter.getAPI().getStoreFront(token, id, language);
		call.enqueue(callback);
	}

	public void getMultipleStoreFront(List<String> ids, Callback<StoreFronts> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<StoreFronts> call = AppleMusicAdapter.getAPI().getMultipleStoreFront(token, formatIds);
		call.enqueue(callback);
	}

	public void getMultipleStoreFront(List<String> ids, String language, Callback<StoreFronts> callback) {
		String formatIds = "";
		for (String id : ids) {
			formatIds = formatIds + id + ",";
		}
		formatIds = formatIds.substring(0, formatIds.lastIndexOf(","));
		Call<StoreFronts> call = AppleMusicAdapter.getAPI().getMultipleStoreFront(token, formatIds, language);
		call.enqueue(callback);
	}

	public void getAllStoreFronts(Callback<StoreFronts> callback) {
		Call<StoreFronts> call = AppleMusicAdapter.getAPI().getAllStoreFronts(token);
		call.enqueue(callback);
	}

	public void getAllStoreFronts(String language, Callback<StoreFronts> callback) {
		Call<StoreFronts> call = AppleMusicAdapter.getAPI().getAllStoreFronts(token, language);
		call.enqueue(callback);
	}
}