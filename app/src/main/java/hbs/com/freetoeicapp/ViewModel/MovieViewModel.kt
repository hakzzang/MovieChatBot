package hbs.com.freetoeicapp.ViewModel

import android.content.Context
import android.databinding.BaseObservable
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.RecyclerView
import android.transition.TransitionManager
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hbs.com.freetoeicapp.Activity.MainActivity
import hbs.com.freetoeicapp.Model.MovieSearch
import hbs.com.freetoeicapp.R
import hbs.com.freetoeicapp.Utils.API_URL
import hbs.com.freetoeicapp.databinding.MovieRecyclerItemBinding


class MovieViewModel(private var movieItem: MovieSearch?, movieRecyclerItemBinding: MovieRecyclerItemBinding, private val context: Context) : BaseObservable() {
    val movieItemBinding = movieRecyclerItemBinding
    var applyConstraintSet = ConstraintSet()
    fun getMovieData(): MovieSearch? {
        return movieItem
    }

    fun onCreate(itemPosition: Int, mRequestManager: RequestManager) {
        mRequestManager.load(API_URL.MOVIE_POSTER_PATH.apiUrl + movieItem!!.poster_path).diskCacheStrategy(DiskCacheStrategy.ALL).into(movieItemBinding.movieFrontIV)

        movieItemBinding.searchLL.setOnClickListener {
            _ ->
            Log.d("click","click")
            TransitionManager.beginDelayedTransition(movieItemBinding.itemCL)
            val constraintLayout = ConstraintLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT)
            movieItemBinding.itemCL.layoutParams = constraintLayout
            applyConstraintSet.clone(movieItemBinding.itemCL)


            applyConstraintSet.clear(movieItemBinding.itemCardImage.id)
            applyConstraintSet.connect(movieItemBinding.itemCardImage.id, ConstraintSet.LEFT, movieItemBinding.itemCL.id, ConstraintSet.LEFT,0)
            applyConstraintSet.connect(movieItemBinding.itemCardImage.id, ConstraintSet.BOTTOM, movieItemBinding.itemCL.id, ConstraintSet.BOTTOM,0)
            applyConstraintSet.connect(movieItemBinding.itemCardImage.id, ConstraintSet.RIGHT, movieItemBinding.itemCL.id, ConstraintSet.RIGHT,0)
            applyConstraintSet.connect(movieItemBinding.itemCardImage.id, ConstraintSet.TOP, movieItemBinding.itemCL.id, ConstraintSet.TOP,0)/*

            applyConstraintSet.connect(movieItemBinding.itemCL.id, ConstraintSet.BOTTOM, movieSearchRV, ConstraintSet.BOTTOM)*/
            applyConstraintSet.applyTo(movieItemBinding.itemCL)

            val mRecyclerView = (context as MainActivity).findViewById<RecyclerView>(R.id.movieSearchRV)
            mRecyclerView.scrollToPosition(itemPosition)
        }
    }


}