package hbs.com.freetoeicapp.Model;

import com.google.gson.annotations.SerializedName;

public class MovieSearchItem {
    @SerializedName("page")
    int page;

    @SerializedName("results")
    MovieSearch[] movieSearches;

    @SerializedName("total_results")
    int total_results;

    @SerializedName("total_pages")
    int total_pages;

    public MovieSearchItem(int page, MovieSearch[] movieSearches, int total_results, int total_pages) {
        this.page = page;
        this.movieSearches = movieSearches;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public MovieSearch[] getMovieSearches() {
        return movieSearches;
    }

    public void setMovieSearches(MovieSearch[] movieSearches) {
        this.movieSearches = movieSearches;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
