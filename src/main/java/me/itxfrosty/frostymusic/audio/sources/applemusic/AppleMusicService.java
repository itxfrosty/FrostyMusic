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
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppleMusicService {

	String CONTENT_TYPE = "application/json";

	@GET("/v1/storefronts/{id}")
	Call<StoreFront> getStoreFront(@Header("Authorization") String developerToken,
								   @Path("id") String id);

	@GET("/v1/storefronts/{id}")
	Call<StoreFront> getStoreFront(@Header("Authorization") String developerToken,
								   @Path("id") String id,
								   @Query("l") String language);

	@GET("/v1/storefronts")
	Call<StoreFronts> getMultipleStoreFront(@Header("Authorization") String developerToken,
											@Query("ids") String ids);

	@GET("/v1/storefronts")
	Call<StoreFronts> getMultipleStoreFront(@Header("Authorization") String developerToken,
											@Query("ids") String ids,
											@Query("l") String language);

	@GET("/v1/storefronts")
	Call<StoreFronts> getAllStoreFronts(@Header("Authorization") String developerToken);

	@GET("/v1/storefronts")
	Call<StoreFronts> getAllStoreFronts(@Header("Authorization") String developerToken,
										@Query("l") String language);

	@GET("/v1/catalog/{storefront}/albums/{id}")
	Call<Album> getAlbum(@Header("Authorization") String developerToken,
						 @Path("storefront") String storefront,
						 @Path("id") String id);

	@GET("/v1/catalog/{storefront}/albums/{id}")
	Call<Album> getAlbum(@Header("Authorization") String developerToken,
						 @Path("storefront") String storefront,
						 @Path("id") String id,
						 @Query("l") String language);

	@GET("/v1/catalog/{storefront}/albums")
	Call<Albums> getMultipleAlbum(@Header("Authorization") String developerToken,
								  @Path("storefront") String storefront,
								  @Query("ids") String ids);

	@GET("/v1/catalog/{storefront}/albums")
	Call<Albums> getMultipleAlbum(@Header("Authorization") String developerToken,
								  @Path("storefront") String storefront,
								  @Query("ids") String ids,
								  @Query("l") String language);

	@GET("/v1/catalog/{storefront}/songs/{id}")
	Call<Song> getSong(@Header("Authorization") String developerToken,
					   @Path("storefront") String storefront,
					   @Path("id") String id);

	@GET("/v1/catalog/{storefront}/songs/{id}")
	Call<Song> getSong(@Header("Authorization") String developerToken,
					   @Path("storefront") String storefront,
					   @Path("id") String id,
					   @Query("l") String language);

	@GET("/v1/catalog/{storefront}/songs")
	Call<Songs> getMultipleSong(@Header("Authorization") String developerToken,
								@Path("storefront") String storefront,
								@Query("ids") String ids);

	@GET("/v1/catalog/{storefront}/songs")
	Call<Songs> getMultipleSong(@Header("Authorization") String developerToken,
								@Path("storefront") String storefront,
								@Query("ids") String ids,
								@Query("l") String language);

	@GET("/v1/catalog/{storefront}/artists/{id}")
	Call<Artist> getArtist(@Header("Authorization") String developerToken,
						   @Path("storefront") String storefront,
						   @Path("id") String id);

	@GET("/v1/catalog/{storefront}/artists/{id}")
	Call<Artist> getArtist(@Header("Authorization") String developerToken,
						   @Path("storefront") String storefront,
						   @Path("id") String id,
						   @Query("l") String language);

	@GET("/v1/catalog/{storefront}/artists")
	Call<Artists> getMultipleArtist(@Header("Authorization") String developerToken,
									@Path("storefront") String storefront,
									@Query("ids") String ids);

	@GET("/v1/catalog/{storefront}/artists")
	Call<Artists> getMultipleArtist(@Header("Authorization") String developerToken,
									@Path("storefront") String storefront,
									@Query("ids") String ids,
									@Query("l") String language);

	@GET("/v1/catalog/{storefront}/music-videos/{id}")
	Call<MusicVideo> getMusicVideo(@Header("Authorization") String developerToken,
								   @Path("storefront") String storefront,
								   @Path("id") String id);

	@GET("/v1/catalog/{storefront}/music-videos/{id}")
	Call<MusicVideo> getMusicVideo(@Header("Authorization") String developerToken,
								   @Path("storefront") String storefront,
								   @Path("id") String id,
								   @Query("l") String language);

	@GET("/v1/catalog/{storefront}/music-videos")
	Call<MusicVideos> getMultipleMusicVideo(@Header("Authorization") String developerToken,
											@Path("storefront") String storefront,
											@Query("ids") String ids);

	@GET("/v1/catalog/{storefront}/music-videos")
	Call<MusicVideos> getMultipleMusicVideo(@Header("Authorization") String developerToken,
											@Path("storefront") String storefront,
											@Query("ids") String ids,
											@Query("l") String language);

	@GET("/v1/catalog/{storefront}/playlists/{id}")
	Call<PlayList> getPlayList(@Header("Authorization") String developerToken,
							   @Path("storefront") String storefront,
							   @Path("id") String id);

	@GET("/v1/catalog/{storefront}/playlists/{id}")
	Call<PlayList> getPlayList(@Header("Authorization") String developerToken,
							   @Path("storefront") String storefront,
							   @Path("id") String id,
							   @Query("l") String language);

	@GET("/v1/catalog/{storefront}/playlists")
	Call<PlayLists> getMultiplePlayList(@Header("Authorization") String developerToken,
										@Path("storefront") String storefront,
										@Query("ids") String ids);

	@GET("/v1/catalog/{storefront}/playlists")
	Call<PlayLists> getMultiplePlayList(@Header("Authorization") String developerToken,
										@Path("storefront") String storefront,
										@Query("ids") String ids,
										@Query("l") String language);

	@GET("/v1/catalog/{storefront}/stations/{id}")
	Call<Station> getStation(@Header("Authorization") String developerToken,
							 @Path("storefront") String storefront,
							 @Path("id") String id);

	@GET("/v1/catalog/{storefront}/stations/{id}")
	Call<Station> getStation(@Header("Authorization") String developerToken,
							 @Path("storefront") String storefront,
							 @Path("id") String id,
							 @Query("l") String language);

	@GET("/v1/catalog/{storefront}/stations")
	Call<Stations> getMultipleStation(@Header("Authorization") String developerToken,
									  @Path("storefront") String storefront,
									  @Query("ids") String ids);

	@GET("/v1/catalog/{storefront}/stations")
	Call<Stations> getMultipleStation(@Header("Authorization") String developerToken,
									  @Path("storefront") String storefront,
									  @Query("ids") String ids,
									  @Query("l") String language);

	@GET("/v1/catalog/{storefront}/curators/{id}")
	Call<Curator> getCurator(@Header("Authorization") String developerToken,
							 @Path("storefront") String storefront,
							 @Path("id") String id);

	@GET("/v1/catalog/{storefront}/curators/{id}")
	Call<Curator> getCurator(@Header("Authorization") String developerToken,
							 @Path("storefront") String storefront,
							 @Path("id") String id,
							 @Query("l") String language);

	@GET("/v1/catalog/{storefront}/curators")
	Call<Curators> getMultipleCurator(@Header("Authorization") String developerToken,
									  @Path("storefront") String storefront,
									  @Query("ids") String ids);

	@GET("/v1/catalog/{storefront}/curators")
	Call<Curators> getMultipleCurator(@Header("Authorization") String developerToken,
									  @Path("storefront") String storefront,
									  @Query("ids") String ids,
									  @Query("l") String language);

	@GET("/v1/catalog/{storefront}/activities/{id}")
	Call<Activity> getActivity(@Header("Authorization") String developerToken,
							   @Path("storefront") String storefront,
							   @Path("id") String id);

	@GET("/v1/catalog/{storefront}/activities/{id}")
	Call<Activity> getActivity(@Header("Authorization") String developerToken,
							   @Path("storefront") String storefront,
							   @Path("id") String id,
							   @Query("l") String language);

	@GET("/v1/catalog/{storefront}/activities")
	Call<Activities> getMultipleActivity(@Header("Authorization") String developerToken,
										 @Path("storefront") String storefront,
										 @Query("ids") String ids);

	@GET("/v1/catalog/{storefront}/activities")
	Call<Activities> getMultipleActivity(@Header("Authorization") String developerToken,
										 @Path("storefront") String storefront,
										 @Query("ids") String ids,
										 @Query("l") String language);

	@GET("/v1/catalog/{storefront}/apple-curators/{id}")
	Call<AppleCurator> getAppleCurator(@Header("Authorization") String developerToken,
									   @Path("storefront") String storefront,
									   @Path("id") String id);

	@GET("/v1/catalog/{storefront}/apple-curators/{id}")
	Call<AppleCurator> getAppleCurator(@Header("Authorization") String developerToken,
									   @Path("storefront") String storefront,
									   @Path("id") String id,
									   @Query("l") String language);

	@GET("/v1/catalog/{storefront}/apple-curators")
	Call<AppleCurators> getMultipleAppleCurator(@Header("Authorization") String developerToken,
												@Path("storefront") String storefront,
												@Query("ids") String ids);

	@GET("/v1/catalog/{storefront}/apple-curators")
	Call<AppleCurators> getMultipleAppleCurator(@Header("Authorization") String developerToken,
												@Path("storefront") String storefront,
												@Query("ids") String ids,
												@Query("l") String language);

	@GET("/v1/catalog/{storefront}/charts")
	Call<Charts> getCharts(@Header("Authorization") String developerToken,
						   @Path("storefront") String storefront,
						   @Query("types") String types,
						   @Query("l") String language,
						   @Query("genre") int genre,
						   @Query("chart") String chart,
						   @Query("limit") Integer limit,
						   @Query("offset") String offset);

	@GET("/v1/catalog/{storefront}/genres")
	Call<Genres> getTopChartsGenres(@Header("Authorization") String developerToken,
									@Path("storefront") String storefront,
									@Query("l") String language,
									@Query("limit") Integer limit,
									@Query("offset") String offset);

	@GET("/v1/catalog/{storefront}/genres/{id}")
	Call<Genre> getGenre(@Header("Authorization") String developerToken,
						 @Path("storefront") String storefront,
						 @Path("id") String id,
						 @Query("l") String language);

	@GET("/v1/catalog/{storefront}/genres")
	Call<Genres> getMultipleGenre(@Header("Authorization") String developerToken,
								  @Path("storefront") String storefront,
								  @Query("ids") String ids,
								  @Query("l") String language);

	@GET("/v1/catalog/{storefront}/search")
	Call<Searches> search(@Header("Authorization") String developerToken,
						  @Path("storefront") String storefront,
						  @Query("term") String term,
						  @Query("l") String language,
						  @Query("limit") Integer limit,
						  @Query("offset") String offset,
						  @Query("types") String types);

	@GET("/v1/catalog/{storefront}/search/hints")
	Call<Hint> searchHint(@Header("Authorization") String developerToken,
						  @Path("storefront") String storefront,
						  @Query("term") String term,
						  @Query("l") String language,
						  @Query("limit") Integer limit,
						  @Query("offset") String offset,
						  @Query("types") String types);
}