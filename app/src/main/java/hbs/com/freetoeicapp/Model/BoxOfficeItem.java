package hbs.com.freetoeicapp.Model;

import com.google.gson.annotations.SerializedName;

public class BoxOfficeItem {
    @SerializedName("boxOfficeResult")
    BoxOfficeResult boxOfficeResult;

    public BoxOfficeItem(BoxOfficeResult boxOfficeResult) {
        this.boxOfficeResult = boxOfficeResult;
    }

    public BoxOfficeResult getBoxOfficeResult() {
        return boxOfficeResult;
    }

    public void setBoxOfficeResult(BoxOfficeResult boxOfficeResult) {
        this.boxOfficeResult = boxOfficeResult;
    }

    public class BoxOfficeResult {
        @SerializedName("boxofficeType")
        String boxOfficeType;

        @SerializedName("showRange")
        String showRange;

        @SerializedName("dailyBoxOfficeList")
        BoxOffice[] dailyBoxOfficeList;

        public BoxOfficeResult(String boxOfficeType, String showRange, BoxOffice[] dailyBoxOfficeList) {
            this.boxOfficeType = boxOfficeType;
            this.showRange = showRange;
            this.dailyBoxOfficeList = dailyBoxOfficeList;
        }

        public String getBoxOfficeType() {
            return boxOfficeType;
        }

        public void setBoxOfficeType(String boxOfficeType) {
            this.boxOfficeType = boxOfficeType;
        }

        public String getShowRange() {
            return showRange;
        }

        public void setShowRange(String showRange) {
            this.showRange = showRange;
        }

        public BoxOffice[] getDailyBoxOfficeList() {
            return dailyBoxOfficeList;
        }

        public void setDailyBoxOfficeList(BoxOffice[] dailyBoxOfficeList) {
            this.dailyBoxOfficeList = dailyBoxOfficeList;
        }
    }


}
