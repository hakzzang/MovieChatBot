package hbs.com.freetoeicapp.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import hbs.com.freetoeicapp.R;


public class AdMobDialog extends AlertDialog {
    private TextView cancelTV, finishTV;
    public AdMobDialog(@NonNull Context context) {
        super(context);
    }
    private NativeExpressAdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_native_ad);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        mAdView = findViewById(R.id.mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        cancelTV = findViewById(R.id.cancelTV);
        finishTV = findViewById(R.id.finishTV);

        cancelTV.setOnClickListener(onClickListener);
        finishTV.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = v -> {
        if(v.getId()==R.id.finishTV){
            mAdView.destroy();
            android.os.Process.killProcess(android.os.Process.myPid());
        }else if(v.getId()==R.id.cancelTV){
            mAdView.destroy();
            dismiss();
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        mAdView.destroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAdView.destroy();
    }
}
