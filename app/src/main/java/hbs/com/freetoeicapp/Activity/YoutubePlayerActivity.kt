package hbs.com.freetoeicapp.Activity

import android.os.Bundle
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import hbs.com.freetoeicapp.R
import kotlinx.android.synthetic.main.activity_youtube_player.*

class YoutubePlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youtubePlayer: YouTubePlayer?, isReady: Boolean) {
        if (!isReady) {
            val playKey = intent.getStringExtra("playKey")
            youtubePlayer!!.cueVideo(playKey);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)
        youtube_view.initialize(getString(R.string.youtube_key), this@YoutubePlayerActivity)
    }
}