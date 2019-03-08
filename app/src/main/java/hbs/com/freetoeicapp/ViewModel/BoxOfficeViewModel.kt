package hbs.com.freetoeicapp.ViewModel

import android.content.Context
import android.databinding.BaseObservable
import android.os.Message
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import hbs.com.freetoeicapp.Activity.MainActivity
import hbs.com.freetoeicapp.Activity.MainActivity.Companion.tempItemLayout
import hbs.com.freetoeicapp.Activity.MainActivity.Companion.youtubeSearchArrayList
import hbs.com.freetoeicapp.Adapter.YoutubeSearchAdapter
import hbs.com.freetoeicapp.Model.BoxOffice
import hbs.com.freetoeicapp.Model.MovieDBItem
import hbs.com.freetoeicapp.Model.YoutubeSearchItem
import hbs.com.freetoeicapp.R
import hbs.com.freetoeicapp.Utils.API_URL
import hbs.com.freetoeicapp.Utils.ConnectService
import hbs.com.freetoeicapp.Utils.ConnectUtils
import hbs.com.freetoeicapp.databinding.OfficeRecyclerItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class BoxOfficeViewModel(private var officeItem: BoxOffice?, officeRecyclerItemBinding: OfficeRecyclerItemBinding, rvPosition: Int) : BaseObservable() {
    private val officeItemBinding = officeRecyclerItemBinding
    private val itemPosition = rvPosition
    private var mContext: Context? = null
    private var requestManager: RequestManager? = null
    private var applyConstraintSet: ConstraintSet = ConstraintSet()

    fun getOfficeData(): BoxOffice? {
        return officeItem
    }

    fun onCreate(context: Context, mRequestManager: RequestManager) {
        mContext = context
        requestManager = mRequestManager

        val connectUtils = ConnectUtils()

        //updateArrowIV()
        val movieDBItemConnector = connectUtils.makeRetrofitObject(API_URL.MOVIE_DATA_URL.apiUrl).create(ConnectService::class.java)
        val movieDBItemCall = movieDBItemConnector.movieDBItemContributors("3", "search", "movie", context.getString(R.string.movie_db_key)
                , "ko", officeItem!!.movieNm, officeItem!!.openDt.split("-")[0])
        movieDBItemCall.enqueue(object : Callback<MovieDBItem> {
            override fun onFailure(call: Call<MovieDBItem>?, t: Throwable?) {
                Log.d("movieConnector", t!!.message)
            }

            override fun onResponse(call: Call<MovieDBItem>?, response: Response<MovieDBItem>?) {
                val responseResult = response!!.body()
                if (responseResult!!.movieDBs.isNotEmpty()) {
                    if (responseResult.movieDBs[0].poster_path != null) {
                        Log.d("posterPath", responseResult.movieDBs[0].poster_path)
                        val posterPath = API_URL.MOVIE_POSTER_PATH.apiUrl + responseResult.movieDBs[0].poster_path
                        mRequestManager.load(posterPath).placeholder(R.drawable.nosearch_poster_img).diskCacheStrategy(DiskCacheStrategy.ALL).listener(object : RequestListener<String, GlideDrawable> {
                            override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                if (itemPosition == 4) {
                                    val message = Message()
                                    message.obj = MainActivity.ANIMATION_STATUS.ANIM_END
                                    MainActivity.loadingHandler!!.sendMessage(message)
                                }
                                return false
                            }

                        }).into(officeItemBinding.movieImageView)
                    }

                }

            }
        })
        setTextAudiAcc()
        makeExpendedDesign(officeItemBinding)
    }

    private fun makeExpendedDesign(officeItemBinding: OfficeRecyclerItemBinding) {
        officeItemBinding.officeParentLL.setOnClickListener {
            officeItemBinding.officeExpandedYoutubeLL.visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(officeItemBinding.officeExpandedYoutubeLL)
            applyConstraintSet.clone(officeItemBinding.officeExpandedYoutubeLL)

            applyConstraintSet.setVisibility(officeItemBinding.movieReviewRV.id, VISIBLE)
            if (tempItemLayout != null)
                tempItemLayout!!.visibility = View.GONE


            connectService(officeItem!!.movieNm)
            tempItemLayout = officeItemBinding.officeExpandedYoutubeLL

            applyConstraintSet.applyTo(officeItemBinding.officeExpandedYoutubeLL)

        }
    }

    private fun setTextAudiAcc() {
        val commaFormat = DecimalFormat("#,###")
        officeItemBinding.audiAccTV.text = commaFormat.format(officeItem!!.audiAcc.toInt())
    }

    private fun connectService(movieName: String) {
        youtubeSearchArrayList.clear()
        TransitionManager.beginDelayedTransition(officeItemBinding.movieReviewRV)
        officeItemBinding.movieReviewRV.adapter = YoutubeSearchAdapter(mContext!!, youtubeSearchArrayList, requestManager!!, itemPosition)
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        officeItemBinding.movieReviewRV.layoutManager = layoutManager

        val connectUtils = ConnectUtils()
        val youtubeSearchConnector = connectUtils.makeRetrofitObject(API_URL.YOUTUBE_SEARCH_PATH.apiUrl).create(ConnectService::class.java)
        val youtubeItemCall = youtubeSearchConnector.searchYoutubeContributors("search", "snippet", "viewcount", "10", "$movieName 리뷰", mContext!!.getString(R.string.youtube_key))
        youtubeItemCall.enqueue(object : Callback<YoutubeSearchItem> {
            override fun onFailure(call: Call<YoutubeSearchItem>, t: Throwable) {
                Log.d("error", t.message)
            }

            override fun onResponse(call: Call<YoutubeSearchItem>, response: Response<YoutubeSearchItem>) {
                for (youtubeItem in response.body()!!.youtubeSearch) {
                    Log.d("itemTitle", youtubeItem.snippet.title)
                    youtubeSearchArrayList.add(youtubeItem)
                    if (youtubeSearchArrayList.size == 2)
                        break
                }
                officeItemBinding.movieReviewRV.adapter.notifyItemRangeInserted(0, youtubeSearchArrayList.size)
            }
        })
    }
}