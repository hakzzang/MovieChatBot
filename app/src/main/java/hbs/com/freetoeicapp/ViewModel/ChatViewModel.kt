package hbs.com.freetoeicapp.ViewModel

import android.databinding.BaseObservable
import android.view.View
import com.bumptech.glide.RequestManager
import hbs.com.freetoeicapp.Model.ChatItem
import hbs.com.freetoeicapp.R
import hbs.com.freetoeicapp.databinding.ChatRecyclerItemBinding

class ChatViewModel(private var chatItem: ChatItem, chatRecyclerItemBinding : ChatRecyclerItemBinding) : BaseObservable(){
    val chatItemBinding = chatRecyclerItemBinding
    fun getChatItem(): ChatItem {
        return chatItem
    }

    fun onCreate(mRequestManager: RequestManager){
        //내 챗일 경우
        if(chatItem.isMyChatBool){
            chatItemBinding.chatBotLL.visibility = View.GONE
            chatItemBinding.chatMeLL.visibility = View.VISIBLE
            if(chatItem.chatContent.img_path == ""){
                chatItemBinding.myChatIV.visibility = View.GONE
            }
        }//봇 챗일 경우
        else{
            chatItemBinding.chatBotLL.visibility = View.VISIBLE
            chatItemBinding.chatMeLL.visibility = View.GONE
            if(chatItem.chatContent.img_path == ""){
                chatItemBinding.yourChatIV.visibility = View.GONE
            }else{
                mRequestManager.load(chatItem.chatContent.img_path).placeholder(R.drawable.nosearch_poster_img).into(chatItemBinding.yourChatIV)
            }
        }
    }
}