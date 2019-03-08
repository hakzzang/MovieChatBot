package hbs.com.freetoeicapp.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectUtils {
    private Retrofit retrofitObject = null;
    public Retrofit makeRetrofitObject(String baseUrl) {
        retrofitObject = new Retrofit.Builder().
                baseUrl(baseUrl).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        return retrofitObject;
    }
}
