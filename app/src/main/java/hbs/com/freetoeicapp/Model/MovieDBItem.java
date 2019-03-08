package hbs.com.freetoeicapp.Model;

import com.google.gson.annotations.SerializedName;

public class MovieDBItem {
    @SerializedName("page")
    int page;

    @SerializedName("results")
    MovieDB[] movieDBs;

    @SerializedName("total_results")
    int total_results;
    
    @SerializedName("total_pages")
    int total_pages;

    public MovieDBItem(int page, MovieDB[] movieDBs, int total_results, int total_pages) {
        this.page = page;
        this.movieDBs = movieDBs;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public int getPage() {
        return page;
    }

    public MovieDB[] getMovieDBs() {
        return movieDBs;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }
}
