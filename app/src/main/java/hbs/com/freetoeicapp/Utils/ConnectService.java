package hbs.com.freetoeicapp.Utils;

import hbs.com.freetoeicapp.Model.BoxOfficeItem;
import hbs.com.freetoeicapp.Model.ChatItem;
import hbs.com.freetoeicapp.Model.MovieDBItem;
import hbs.com.freetoeicapp.Model.MovieSearchItem;
import hbs.com.freetoeicapp.Model.YoutubeSearchItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ConnectService {

    @Headers({"Accept: application/json"})
    @GET("{api_type}")
    Call<BoxOfficeItem> boxOfficeContributors(
            @Path("api_type") String api_type,
            @Query("key") String serviceKey,
            @Query("targetDt") String targetDt
    );


    //search
    @Headers({"Accept: application/json"})
    @GET("{version}/{api_type}/{api_sub_type}")
    Call<MovieSearchItem> movieDBItemContributors(
            @Path("version") String version,
            @Path("api_type") String api_type,
            @Path("api_sub_type") String api_sub_type,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("include_adult") boolean adultBool,
            @Query("query") String query
    );

    //추천 search
    @Headers({"Accept: application/json"})
    @GET("{version}/{api_type}/{api_sub_type}")
    Call<MovieDBItem> movieDBItemContributors(
            @Path("version") String version,
            @Path("api_type") String api_type,
            @Path("api_sub_type") String api_sub_type,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("query") String query,
            @Query("year") String year
    );


    @Headers({"Accept: application/json"})
    @GET("{chat_path}")
    //GET : chatbot
    Call<ChatItem> chatItemContributors(
            @Path("chat_path") String botPath,
            @Query("content") String conversation,
            @Query("user_key") String userId
    );

    @Headers({"Accept: application/json"})
    @GET("{get_api_type}")
    Call<YoutubeSearchItem> searchYoutubeContributors(
            @Path("get_api_type") String apiType,
            @Query("part") String part,
            @Query("order") String order,
            @Query("maxResults") String maxResults,
            @Query("q") String query,
            @Query("key") String key
    );

}
