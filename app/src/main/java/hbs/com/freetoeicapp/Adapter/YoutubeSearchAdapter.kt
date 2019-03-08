package hbs.com.freetoeicapp.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import hbs.com.freetoeicapp.Model.YoutubeSearchItem
import hbs.com.freetoeicapp.R
import hbs.com.freetoeicapp.ViewModel.SearchYoutubeViewModel
import hbs.com.freetoeicapp.databinding.YoutubeSearchItemBinding

class YoutubeSearchAdapter(mContext: Context, arrayList:ArrayList<YoutubeSearchItem.YoutubeSearch>, private val mRequestManager:RequestManager, private val parentPosition:Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val context = mContext
    private val youtubeSearchArrayList = arrayList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val youtubeSearchView:YoutubeSearchItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.youtube_search_item, parent, false)
        return YoutubeSearchHolder(youtubeSearchView)
    }

    override fun getItemCount(): Int {
        return youtubeSearchArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val youtubeHolder = holder as YoutubeSearchHolder
        youtubeHolder.binding.searchYoutubeVM = SearchYoutubeViewModel(youtubeSearchArrayList[position], youtubeHolder.binding, mRequestManager, context)
        youtubeHolder.binding.searchYoutubeVM!!.onCreate()
    }

    class YoutubeSearchHolder(val binding: YoutubeSearchItemBinding) : RecyclerView.ViewHolder(binding.root)
}