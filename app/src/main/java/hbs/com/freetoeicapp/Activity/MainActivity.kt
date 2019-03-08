package hbs.com.freetoeicapp.Activity

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.transition.TransitionManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.android.gms.ads.MobileAds
import com.google.firebase.messaging.FirebaseMessaging
import hbs.com.freetoeicapp.Adapter.SearchResultAdapter
import hbs.com.freetoeicapp.Model.*
import hbs.com.freetoeicapp.R
import hbs.com.freetoeicapp.Utils.*
import hbs.com.freetoeicapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    /*
    * Retrofit으로 Naver Search API 사용
    * MVVM 패턴으로 RecyclerView 갱신
    * */

    companion object {
        @SuppressLint("StaticFieldLeak")
        var tempItemLayout: ConstraintLayout? = null
        var loadingHandler : Handler? = null
        val applyConstraintSet = ConstraintSet()
        var youtubeSearchArrayList:ArrayList<YoutubeSearchItem.YoutubeSearch> = ArrayList()
    }
    enum class ANIMATION_STATUS(ordinal: Int) {
        ANIM_START(0), ANIM_END(1);
    }

    private var requestManager: RequestManager? = null
    private var connectUtils: ConnectUtils? = null
    private var searchResultAdapter: SearchResultAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private val chatItemArrayList: ArrayList<ChatItem> = ArrayList()
    private val movieSearchArrayList: ArrayList<MovieSearch>? = ArrayList()
    private val boxOfficeArrayList: ArrayList<BoxOffice>? = ArrayList()
    private var sendText: String = ""

    val SUCCESS_STATUS = 200

    private var activityMainBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this, this.getString(R.string.admob_key))
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.my_lang))

        activityMainBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        TransitionManager.beginDelayedTransition(movieSearchRV)
        requestManager = Glide.with(this)

        connectUtils = ConnectUtils()
        initView()

        loadingHandler =
                @SuppressLint("HandlerLeak")
                object : Handler(){
                    override fun handleMessage(msg: Message?) {
                        super.handleMessage(msg)
                        val animStatus = msg!!.obj as ANIMATION_STATUS
                        loadingAnimation(animStatus)
                    }
                }

        getChattingLists()
        conversationChatBot("안녕")
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        supportActionBar!!.setDisplayShowTitleEnabled(true)

        supportActionBar!!.title = "ChatBot"

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        sendChatBtn.setOnClickListener { _ ->
            if (!sendChatET.text.isNullOrEmpty()) {
                sendText = sendChatET.text.toString()
                conversationWithChatBot(sendText)

                sendChatET.setText("")
                Log.d("sendText", sendText)
            }
        }
    }

    private fun updateViewUi(moviesResult: MovieSearchItem) {
        if (movieSearchArrayList!!.size > 0) {
            Log.d("itemIndex:", (movieSearchArrayList.size - 1).toString())
            searchResultAdapter!!.notifyItemRangeRemoved(0, movieSearchArrayList.size)
        }

        movieSearchArrayList.clear()

        for (movieItem in moviesResult.movieSearches) {
            movieSearchArrayList.add(movieItem)
            searchResultAdapter!!.notifyItemInserted(movieSearchArrayList.size - 1)
        }
        movieSearchArrayList.add(MovieSearch(CONNECT_TYPE.ADMOB_CLASS_TYPE))
    }

    private fun updateBoxOfficeViewUi(boxOfficeResults: Array<BoxOffice>) {
        for (boxOfficeItem in boxOfficeResults) {
            boxOfficeArrayList!!.add(boxOfficeItem)
            searchResultAdapter!!.notifyItemInserted(boxOfficeArrayList.size - 1)
            if(boxOfficeArrayList.size ==5){
                boxOfficeArrayList.add(BoxOffice(CONNECT_TYPE.ADMOB_CLASS_TYPE))
            }
        }
    }

    private fun updateChatViewUi(chatItem: ChatItem) {
        chatItemArrayList.add(chatItem)
        searchResultAdapter!!.notifyItemInserted(chatItemArrayList.size - 1)

        movieSearchRV.scrollToPosition(searchResultAdapter!!.itemCount-1)
    }

    private fun getChattingLists() {
        statusTV.text = ""
        layoutManager = LinearLayoutManager(this@MainActivity)
        layoutManager!!.orientation = LinearLayoutManager.VERTICAL
        layoutManager!!.stackFromEnd = true
        movieSearchRV.layoutManager = layoutManager

        searchResultAdapter = SearchResultAdapter(this@MainActivity, chatItemArrayList, requestManager!!)
        movieSearchRV.adapter = searchResultAdapter
        searchResultAdapter!!.notifyDataSetChanged()

        val message = Message()
        message.obj=ANIMATION_STATUS.ANIM_END
        loadingHandler!!.sendMessage(message)


    }

    private fun conversationWithChatBot(conversation: String) {
        //내 텍스트
        val myChatContent = ChatItem().ChatContent(conversation, "")
        val myChatItem = ChatItem(myChatContent, true)

        updateChatViewUi(myChatItem)

        //챗봇의 텍스트
        val chattingConnector = connectUtils!!.makeRetrofitObject(API_URL.CHAT_BOT_URL.apiUrl).create(ConnectService::class.java)
        val callChatting = chattingConnector.chatItemContributors("chatbot", conversation, "0")
        callChatting.enqueue(object : Callback<ChatItem> {
            override fun onResponse(call: Call<ChatItem>?, response: Response<ChatItem>?) {
                val mChatItem = response!!.body()
                if (mChatItem != null) {
                    updateChatViewUi(mChatItem)
                }
            }

            override fun onFailure(call: Call<ChatItem>?, t: Throwable?) {

            }
        })
    }

    private fun conversationChatBot(conversation: String) {
        //챗봇의 텍스트
        val chattingConnector = connectUtils!!.makeRetrofitObject(API_URL.CHAT_BOT_URL.apiUrl).create(ConnectService::class.java)
        val callChatting = chattingConnector.chatItemContributors("chatbot", conversation, "0")
        callChatting.enqueue(object : Callback<ChatItem> {
            override fun onResponse(call: Call<ChatItem>?, response: Response<ChatItem>?) {
                val mChatItem = response!!.body()
                if (mChatItem != null) {
                    updateChatViewUi(mChatItem)
                }
            }

            override fun onFailure(call: Call<ChatItem>?, t: Throwable?) {

            }
        })
    }

    private fun getBoxOfficeLists() {
        val message = Message()
        message.obj=ANIMATION_STATUS.ANIM_START
        loadingHandler!!.sendMessage(message)

        layoutManager = LinearLayoutManager(this@MainActivity)
        boxOfficeArrayList!!.clear()
        movieSearchRV.layoutManager = layoutManager
        val boxOfficeConnector = connectUtils!!.makeRetrofitObject(API_URL.BOX_OFFICE_URL.apiUrl).create(ConnectService::class.java)
        Log.d("DateUtils.getYesterday", DateUtils.getYesterday())
        val callBoxOffice = boxOfficeConnector.boxOfficeContributors("searchDailyBoxOfficeList.json", getString(R.string.box_office_service_key), DateUtils.getYesterday())
        callBoxOffice.enqueue(object : Callback<BoxOfficeItem> {
            //호출 실패
            override fun onFailure(call: Call<BoxOfficeItem>?, t: Throwable?) {
                Log.d("movieConnector", t!!.message)
            }

            //호출 성공
            override fun onResponse(call: Call<BoxOfficeItem>?, response: Response<BoxOfficeItem>?) {
                val boxOfficeItem = response!!.body()
                searchResultAdapter = SearchResultAdapter(this@MainActivity, boxOfficeArrayList, requestManager!!)
                movieSearchRV.adapter = searchResultAdapter
                when (response.code()) {
                    SUCCESS_STATUS -> {
                        Log.d("responseResult", response.toString())
                        statusTV.text = ""
                        updateBoxOfficeViewUi(boxOfficeItem!!.boxOfficeResult.dailyBoxOfficeList)
                    }
                    else -> {
                        Log.d("errorCode", response.toString())
                        statusTV.text = response.toString()
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val adMobDialog = AdMobDialog(this)
            adMobDialog.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.search_menu, menu)
        tempItemLayout = null//it is temp expenedLayout.

        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        movieSearchRV.setBackgroundColor(resources.getColor(R.color.grey_200));
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener, SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                layoutManager!!.stackFromEnd = false
                sendChatLL.visibility = View.GONE
                loadingAnimation(ANIMATION_STATUS.ANIM_START)

                val movieDBItemConnector= connectUtils!!.makeRetrofitObject(API_URL.MOVIE_DATA_URL.apiUrl).create(ConnectService::class.java)
                val movieDBItemCall = movieDBItemConnector.movieDBItemContributors("3","search","movie", getString(R.string.movie_db_key),"ko",false, query)
                movieDBItemCall.enqueue(object : Callback<MovieSearchItem> {
                    //호출 실패
                    override fun onFailure(call: Call<MovieSearchItem>?, t: Throwable?) {
                        Log.d("movieConnector", t!!.message)
                    }

                    //호출 성공
                    override fun onResponse(call: Call<MovieSearchItem>?, response: Response<MovieSearchItem>?) {
                        searchResultAdapter = SearchResultAdapter(this@MainActivity, movieSearchArrayList!!, requestManager!!)
                        layoutManager!!.orientation = LinearLayoutManager.VERTICAL
                        movieSearchRV.layoutManager = layoutManager
                        movieSearchRV.adapter = searchResultAdapter


                        val movies = response!!.body()
                        loadingAnimation(ANIMATION_STATUS.ANIM_END)
                        when (response.code()) {
                            SUCCESS_STATUS -> {
                                statusTV.text = ""
                                updateViewUi(movies!!)
                            }
                            else -> statusTV.text = response.toString()
                        }
                    }
                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this@MainActivity).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this@MainActivity).trimMemory(level)
    }

    //여기에 라우터작업을 해야함.
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        movieSearchRV.setBackgroundColor(resources.getColor(R.color.real_white));
        layoutManager!!.stackFromEnd = false
        movieSearchRV.layoutManager = layoutManager
        when (item.itemId) {
            R.id.navigation_home -> {
                movieSearchRV.setBackgroundColor(resources.getColor(R.color.grey_200));
                supportActionBar!!.title = "ChatBot"
                sendChatLL.visibility = View.VISIBLE
                getChattingLists()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportActionBar!!.title = "BoxOffice"
                sendChatLL.visibility = View.GONE
                getBoxOfficeLists()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportActionBar!!.title = "Opinion"
                sendChatLL.visibility = View.GONE
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

/*
    override fun onStart() {
        super.onStart()
        loadingHandler =
                @SuppressLint("HandlerLeak")
                object : Handler(){
                    override fun handleMessage(msg: Message?) {
                        super.handleMessage(msg)
                        val animStatus = msg!!.obj as ANIMATION_STATUS
                        loadingAnimation(animStatus)
                    }
                }

        getChattingLists()
        conversationChatBot("안녕")
    }*/

    fun loadingAnimation(view_status:ANIMATION_STATUS){
        when(view_status){
            ANIMATION_STATUS.ANIM_START -> {
                movieSearchRV.visibility = View.INVISIBLE
                loadingIV.visibility = View.VISIBLE
                (loadingIV.background as AnimationDrawable).start()
            }

            ANIMATION_STATUS.ANIM_END -> {
                movieSearchRV.visibility = View.VISIBLE
                loadingIV.visibility = View.GONE
                (loadingIV.background as AnimationDrawable).stop()
            }
        }
    }



}
