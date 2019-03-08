package hbs.com.freetoeicapp.Utils;

public enum CONNECT_TYPE {
    MOVIE_CLASS_TYPE(1000) , BOX_OFFICE_CLASS_TYPE(1001), CHAT_CLASS_TYPE(1002), ADMOB_CLASS_TYPE(1003);

    private int statusNum;

    CONNECT_TYPE(int statusNum) {
        this.statusNum = statusNum;
    }

    public int getStatusNum(){
        return statusNum;
    }
}
