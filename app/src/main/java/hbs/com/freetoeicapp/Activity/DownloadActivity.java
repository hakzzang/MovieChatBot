package hbs.com.freetoeicapp.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import hbs.com.freetoeicapp.R;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class DownloadActivity extends AppCompatActivity {
    public static final String[] APP_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private int READ_EXTERNAL_STORAGE_NUM = 1001;
    private int WRITE_EXTERNAL_STORAGE_NUM = 1002;


    private ArrayList<Long> list = new ArrayList<>();
    private Button downBtn1, downBtn2, downBtn3;
    private WebView webview;
    private String[] downloadUris = {"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"};

    private String baseUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/";
    private String partUrl[] = {"BigBuckBunny.mp4", "ElephantsDream.mp4", "ForBiggerBlazes.mp4"};
    private int downloadManagerChannel = 1118;

    private String TAG = "hello";
    private HttpUtil httpUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);


        downBtn1 = (Button) findViewById(R.id.downBtn1);
        downBtn2 = (Button) findViewById(R.id.downBtn2);
        downBtn3 = (Button) findViewById(R.id.downBtn3);
        webview = (WebView)findViewById(R.id.testWebView);

        //임의 포지션 지정
        downBtn1.setTag(0);
        downBtn2.setTag(1);
        downBtn3.setTag(2);

        downBtn1.setOnClickListener(onClickListener);
        downBtn2.setOnClickListener(onClickListener);
        downBtn3.setOnClickListener(onClickListener);

        checkPermission(READ_EXTERNAL_STORAGE_NUM);
        checkPermission(WRITE_EXTERNAL_STORAGE_NUM);

        webview.loadUrl("https://www.googleapis.com/youtube/v3/search?part=snippet&order=rating&maxResults=1&q=%EB%B2%A0%EB%86%88%20%EB%A6%AC%EB%B7%B0&key=AIzaSyA4NFzPfRWGiSN8jqSBlc9c5f6fzqGH7Pg");
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(int permissionNumber) {
        if (
            // 인자에 해당하는 권한이 있는지를 리턴
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            // 권한 팝업에 한번이라도 거부를 했을 경우 true
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Write에 대한 설명이 필요한 경우
                Toast.makeText(this, "Please Check Write Permission", Toast.LENGTH_SHORT).show();
            }
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // READ에 대한 설명이 필요한 경우
                Toast.makeText(this, "Please Check READ Permission", Toast.LENGTH_SHORT).show();
            }
            // 권한 팝업을 요철하는 메소드
            requestPermissions(APP_PERMISSIONS, permissionNumber);
        } else {
            requestPermissions(APP_PERMISSIONS, permissionNumber);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = Integer.valueOf(view.getTag().toString());


            SharedPreferences pref = getSharedPreferences("lastmodifed", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            if (((Button) view).getText().equals("download")) {
                ((Button) view).setText("downloading...");

                httpUtil = new HttpUtil();//취소한 객체를 다시 사용할 수 없으므로 사용할 때마다 재생성
                httpUtil.execute(downloadUris[position], String.valueOf(position));
            } else {
                ((Button) view).setText("download");
                httpUtil.cancel(true);
            }
        }


    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001: {//WRITE_EXTERNAL_STORAGE_NUM
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the,
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(DownloadActivity.this, "please Check Permission", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case 1002: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(DownloadActivity.this, "please Check Permission", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public class NotificationUtils {
        private NotificationUtils notificationUtils;
        private PendingIntent notificationIntent;
        private NotificationManager notificationManager;
        private NotificationCompat.Builder notificationBuilder;
        //NotificationUtils.getInstance()를 호출한다.
        //사용법 makeNotification을 호출한다.
        //파라매터0: context , 파라매터1: notiTitle, 파라매터2: notiContent

        public NotificationCompat.Builder makeNotification(Context context, String notiTitle, String notiContent, boolean fix_true) {

            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                String channelId = "download";
                NotificationChannel mChannel = notificationManager.getNotificationChannel(channelId);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(channelId, context.getString(R.string.app_name), importance);
                    mChannel.setDescription(context.getString(R.string.app_name));
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationBuilder = new NotificationCompat.Builder(context, channelId);


                notificationBuilder.setContentTitle(notiTitle)  // required
                        .setSmallIcon(R.drawable.bar_image) // required
                        .setContentText(notiContent)  // required
                        .setProgress(100, 0, false)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(notificationIntent);
            } else {
                notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.bar_image)
                        .setContentTitle(notiTitle)
                        .setContentText(notiContent)
                        .setContentIntent(notificationIntent);
            }

            Notification fixNotification = notificationBuilder.build();
            if (fix_true)
                fixNotification.flags |= Notification.FLAG_NO_CLEAR;
            else
                fixNotification.flags = Notification.DEFAULT_VIBRATE;

            notificationManager.notify(downloadManagerChannel, fixNotification);

            return notificationBuilder;
        }

        public void removeNotification(Context context) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
            nMgr.cancel(downloadManagerChannel);
        }

        private void setNotiUpdate(NotificationCompat.Builder notiUpdate){
            notificationManager.notify(downloadManagerChannel,notiUpdate.build());
        }

    }

    class HttpUtil extends AsyncTask<String, String, String> {

        NotificationCompat.Builder notiBuilder;
        NotificationUtils notificationUtils = new NotificationUtils();
        int frameCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notiBuilder = notificationUtils.makeNotification(DownloadActivity.this, "Download", "progess..", true);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            notiBuilder.setProgress(100, Integer.valueOf(values[0]),false);
            notificationUtils.setNotiUpdate(notiBuilder);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("download", "completed");
            notificationUtils.removeNotification(DownloadActivity.this);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                File filename = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + partUrl[Integer.parseInt(f_url[1])]);
                System.out.println("Downloading");
                RandomAccessFile rOutput = new RandomAccessFile(filename.getAbsolutePath(), "rw");
                long fileSize = rOutput.length();
                Log.d("startFileSize", String.valueOf(fileSize));
                rOutput.seek(fileSize);


                URL url = new URL(f_url[0]);

                URLConnection conn = url.openConnection();
                conn.setRequestProperty("Range", "bytes=" + String.valueOf(fileSize) + '-');
                conn.connect();
                int remains = conn.getContentLength();
                long lenghtOfFile = remains + fileSize;


                if (remains == fileSize) {
                    return null;
                }

                InputStream input = conn.getInputStream();

                byte data[] = new byte[1024];
                long total = 0;


                while ((count = input.read(data)) != -1) {
                    total += count;
                    // writing data to file
                    rOutput.write(data, 0, count);
                    if(frameCount++==50){
                        frameCount = 0;
                        Log.d("total/lenghtOfFile*100",String.valueOf((total*100)/(long)remains));
                        publishProgress(String.valueOf(total/lenghtOfFile*100));
                    }
                    if(isCancelled()){
                        break;
                    }
                }
                // closing streams
                rOutput.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }
    }

//Environment.DIRECTORY_DOWNLOADS+paths[paths.length-1]
}
