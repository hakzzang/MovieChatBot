package hbs.com.freetoeicapp.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import hbs.com.freetoeicapp.Model.BoxOffice
import hbs.com.freetoeicapp.Model.ChatItem
import hbs.com.freetoeicapp.Model.MovieSearch
import hbs.com.freetoeicapp.R
import hbs.com.freetoeicapp.Utils.CONNECT_TYPE
import hbs.com.freetoeicapp.ViewModel.BoxOfficeViewModel
import hbs.com.freetoeicapp.ViewModel.ChatViewModel
import hbs.com.freetoeicapp.ViewModel.MovieViewModel
import hbs.com.freetoeicapp.databinding.ChatRecyclerItemBinding
import hbs.com.freetoeicapp.databinding.MovieRecyclerItemBinding
import hbs.com.freetoeicapp.databinding.OfficeRecyclerItemBinding
import kotlinx.android.synthetic.main.admob_recycler_item.view.*

class SearchResultAdapter(mContext: Context, arrayList: ArrayList<*>, requestManager: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context: Context? = mContext
    private var searchItemList: ArrayList<*> = arrayList
    private var mRequestManager: RequestManager = requestManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == CONNECT_TYPE.MOVIE_CLASS_TYPE.statusNum) {
            val itemBinding: MovieRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.movie_recycler_item, parent, false)
            return MovieBindingHolder(itemBinding)
        } else if (viewType == CONNECT_TYPE.BOX_OFFICE_CLASS_TYPE.statusNum) {
            val itemBinding: OfficeRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.office_recycler_item, parent, false)
            return OfficeBindingHolder(itemBinding)
        } else if (viewType == CONNECT_TYPE.CHAT_CLASS_TYPE.statusNum) {
            val chatBinding: ChatRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_recycler_item, parent, false)
            return ChatBindingHolder(chatBinding)
        } else if (viewType == CONNECT_TYPE.ADMOB_CLASS_TYPE.statusNum) {
            val adViewInflater = LayoutInflater.from(context).inflate(R.layout.admob_recycler_item, parent, false)
            return AdMobBindingHolder(adViewInflater)
        }

        val itemBinding: OfficeRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.office_recycler_item, parent, false)
        return OfficeBindingHolder(itemBinding)

    }

    override fun getItemCount(): Int {
        return searchItemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == CONNECT_TYPE.MOVIE_CLASS_TYPE.statusNum) {
            val movieHolder = holder as MovieBindingHolder
            val movieItem = searchItemList[position] as MovieSearch
            movieHolder.binding.movieViewModel = MovieViewModel(movieItem = movieItem, movieRecyclerItemBinding = movieHolder.binding, context = context!!)
            movieHolder.binding.movieViewModel!!.onCreate(position, mRequestManager)
        }
        if (holder.itemViewType == CONNECT_TYPE.BOX_OFFICE_CLASS_TYPE.statusNum) {
            val boxOfficeHolder = holder as OfficeBindingHolder
            val officeItem = searchItemList[position] as BoxOffice
            boxOfficeHolder.binding.officeViewModel = BoxOfficeViewModel(officeItem, boxOfficeHolder.binding, position)
            boxOfficeHolder.binding.officeViewModel!!.onCreate(context!!, mRequestManager)
        }
        if (holder.itemViewType == CONNECT_TYPE.CHAT_CLASS_TYPE.statusNum) {
            val chatBindingHolder = holder as ChatBindingHolder
            val chatItem = searchItemList[position] as ChatItem
            chatBindingHolder.binding.chatViewModel = ChatViewModel(chatItem, chatBindingHolder.binding)
            chatBindingHolder.binding.chatViewModel!!.onCreate(mRequestManager)
        }
        if (holder.itemViewType == CONNECT_TYPE.ADMOB_CLASS_TYPE.statusNum) {
            val adHolder = holder as AdMobBindingHolder
            MobileAds.initialize(context, context!!.getString(R.string.admob_key))
            val adRequest = AdRequest.Builder().build()
            adHolder.adView.adItemView.loadAd(adRequest)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (searchItemList[position] is MovieSearch) {
            val movieSearchItem= searchItemList[position] as MovieSearch
            if(movieSearchItem.connect_type == CONNECT_TYPE.ADMOB_CLASS_TYPE){
                return CONNECT_TYPE.ADMOB_CLASS_TYPE.statusNum
            }
            return CONNECT_TYPE.MOVIE_CLASS_TYPE.statusNum
        }else if (searchItemList[position] is BoxOffice) {
            val boxOfficeItem = searchItemList[position] as BoxOffice
            if (boxOfficeItem.viewType == CONNECT_TYPE.ADMOB_CLASS_TYPE) {
                return CONNECT_TYPE.ADMOB_CLASS_TYPE.statusNum
            }
            return CONNECT_TYPE.BOX_OFFICE_CLASS_TYPE.statusNum
        } else if (searchItemList[position] is ChatItem) {
            return CONNECT_TYPE.CHAT_CLASS_TYPE.statusNum
        }
        return 0
    }

    class MovieBindingHolder(val binding: MovieRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)
    class OfficeBindingHolder(val binding: OfficeRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)
    class ChatBindingHolder(val binding: ChatRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)
    class AdMobBindingHolder(val adView: View) : RecyclerView.ViewHolder(adView)
}