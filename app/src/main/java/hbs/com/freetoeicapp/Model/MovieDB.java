package hbs.com.freetoeicapp.Model;

import com.google.gson.annotations.SerializedName;

public class MovieDB {
    @SerializedName("poster_path")
    String poster_path;

    @SerializedName("adult")
    boolean adult;

    @SerializedName("overview")
    String overview;

    @SerializedName("release_date")
    String release_date;

    @SerializedName("id")
    int movieId;

    @SerializedName("original_title")
    String original_title;

    @SerializedName("original_language")
    String original_language;

    @SerializedName("title")
    String title;

    @SerializedName("backdrop_path")
    String backdrop_path;

    @SerializedName("popularity")
    float popularity;

    @SerializedName("vote_count")
    int vote_count;

    @SerializedName("vote_average")
    float vote_average;

    public MovieDB(String poster_path, boolean adult, String overview, String release_date, int movieId, String original_title, String original_language, String title, String backdrop_path, float popularity, int vote_count, float vote_average) {
        this.poster_path = poster_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
        this.movieId = movieId;
        this.original_title = original_title;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public float getPopularity() {
        return popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public float getVote_average() {
        return vote_average;
    }
}
