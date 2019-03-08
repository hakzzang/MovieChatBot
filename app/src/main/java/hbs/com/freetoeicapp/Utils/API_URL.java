package hbs.com.freetoeicapp.Utils;

public enum API_URL {
    NAVER_URL("https://openapi.naver.com") , BOX_OFFICE_URL("http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/"),
    MOVIE_DATA_URL("https://api.themoviedb.org"), MOVIE_POSTER_PATH("https://image.tmdb.org/t/p/w500/"),
    CHAT_BOT_URL("https://today-movie-chatbot.mybluemix.net/"),
    YOUTUBE_SEARCH_PATH("https://www.googleapis.com/youtube/v3/");

    private String apiUrl;

    API_URL(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiUrl(){
        return apiUrl;
    }
}
