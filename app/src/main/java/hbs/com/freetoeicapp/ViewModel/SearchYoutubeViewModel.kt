package hbs.com.freetoeicapp.ViewModel

import android.content.Context
import android.content.Intent
import android.databinding.BaseObservable
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.Explode
import android.transition.Fade
import android.util.Log
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hbs.com.freetoeicapp.Activity.YoutubePlayerActivity
import hbs.com.freetoeicapp.Model.YoutubeSearchItem
import hbs.com.freetoeicapp.databinding.YoutubeSearchItemBinding


class SearchYoutubeViewModel(private var searchYoutube: YoutubeSearchItem.YoutubeSearch, youtubeSearchItemBinding: YoutubeSearchItemBinding, private val requestManager: RequestManager, private val context: Context) : BaseObservable() {
    private val youtubeItemBinding = youtubeSearchItemBinding
    fun getSearchYoutube(): YoutubeSearchItem.YoutubeSearch? {
        return searchYoutube
    }

    fun onCreate() {
        Log.d("thumburl",searchYoutube.snippet.thumbnails.mediumThumbnail.url)
        if (searchYoutube.snippet.thumbnails.mediumThumbnail.url != null) {
            requestManager.load(searchYoutube.snippet.thumbnails.mediumThumbnail.url).diskCacheStrategy(DiskCacheStrategy.ALL).into(youtubeItemBinding.youtubeThumbIV)
        } else {
            requestManager.load(searchYoutube.snippet.thumbnails.defaultThumbnail.url).diskCacheStrategy(DiskCacheStrategy.ALL).into(youtubeItemBinding.youtubeThumbIV)
        }
        youtubeItemBinding.youtubeItemLL.setOnClickListener {
            it-> clickItem()
        }
    }

    fun clickItem(){
        val appCompatConetext=(context as AppCompatActivity)
        appCompatConetext.window.sharedElementEnterTransition = ChangeBounds()
        appCompatConetext.window.sharedElementReturnTransition = ChangeBounds()
        appCompatConetext.window.enterTransition = Fade()
        appCompatConetext.window.exitTransition = Explode()
        val youtubeIntent = Intent(context, YoutubePlayerActivity::class.java)
        youtubeIntent.putExtra("playKey", searchYoutube.videoIds.videoId)
        context.startActivity(youtubeIntent)
    }

    /*@SuppressLint("SimpleDateFormat")
    fun compareDate() {
        //날짜
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        var youtubePublishAt: Date? = null
        val tempCalendar = Calendar.getInstance()
        val currentCalendar = Calendar.getInstance()
        try {
            youtubePublishAt = dateFormat.parse(searchYoutube.snippet.publishedAt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        tempCalendar.time = youtubePublishAt
        currentCalendar.time = Date()
        Log.d("tempCalendar.time ",tempCalendar.time.toString())
        Log.d("tempCalendar.time ",currentCalendar.time.toString())
        Log.d("currentCalendar.time",currentCalendar.timeInMillis.toString())
        Log.d("tempCalendar.time",tempCalendar.timeInMillis.toString())
        val diffSec = (currentCalendar.timeInMillis - tempCalendar.timeInMillis) / 1000 //초
        val diffDays = (diffSec / (24 * 60 * 60)).toInt() //시간 분 초
        Log.d("diffDays",diffDays.toString())
        var diffString = ""
        when(diffDays){
            0 -> diffString = "오늘"
            1 -> diffString = "어제"
            in 2..7 -> diffString = "이번 주"
            in 7..14 -> diffString = "저번 주"
            in 14..30 -> diffString ="이번 달"
            in 30..365 -> diffString = "${diffDays/30}달 전"
            in 365..99999-> diffString = "${diffDays/365}년 전"
        }
        Log.d("diffString",diffString)
        youtubeSearchBinding.youtubeDateTV.text = diffString
    }*/
}