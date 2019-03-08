package hbs.com.freetoeicapp.Model;

import com.google.gson.annotations.SerializedName;

import hbs.com.freetoeicapp.Utils.CONNECT_TYPE;

public class BoxOffice {
    @SerializedName("rank")
    String rank;

    @SerializedName("rankOldAndNew")
    String rankOldAndNew;

    @SerializedName("movieNm")
    String movieNm;

    @SerializedName("openDt")
    String openDt;

    @SerializedName("audiChange")
    String audiChange;

    @SerializedName("audiAcc")
    String audiAcc;

    @SerializedName("movieCd")
    String movieCd;

    @SerializedName("rankInten")
    String rankInten;

    CONNECT_TYPE viewType;

    public BoxOffice(String rank, String rankOldAndNew, String movieNm, String openDt, String audiChange, String audiAcc, String movieCd, String rankInten) {
        this.rank = rank;
        this.rankOldAndNew = rankOldAndNew;
        this.movieNm = movieNm;
        this.openDt = openDt;
        this.audiChange = audiChange;
        this.audiAcc = audiAcc;
        this.movieCd = movieCd;
        this.rankInten = rankInten;
    }

    public BoxOffice(CONNECT_TYPE mViewType){
        this.viewType = mViewType;
    }

    public String getRank() {
        return rank;
    }

    public String getRankOldAndNew() {
        return rankOldAndNew;
    }

    public String getMovieNm() {
        return movieNm;
    }

    public String getOpenDt() {
        return openDt;
    }

    public String getAudiChange() {
        return audiChange;
    }

    public String getAudiAcc() {
        return audiAcc;
    }

    public String getMovieCd() {
        return movieCd;
    }

    public String getRankInten() {
        return rankInten;
    }

    public CONNECT_TYPE getViewType() {
        return viewType;
    }
}
