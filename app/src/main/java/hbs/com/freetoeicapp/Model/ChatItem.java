package hbs.com.freetoeicapp.Model;

import com.google.gson.annotations.SerializedName;

public class ChatItem {
    @SerializedName("text")
    private ChatContent chatContent;
    private boolean myChatBool = false;

    public ChatItem(ChatContent chatContent, boolean myChatBool) {
        this.chatContent = chatContent;
        this.myChatBool = myChatBool;
    }

    public ChatItem(ChatContent chatContent) {
        this.chatContent = chatContent;
    }

    public ChatItem() {
    }

    public ChatContent getChatContent() {
        return chatContent;
    }

    public void setChatContent(ChatContent chatContent) {
        this.chatContent = chatContent;
    }

    public boolean isMyChatBool() {
        return myChatBool;
    }

    public void setMyChatBool(boolean myChatBool) {
        this.myChatBool = myChatBool;
    }

    public class ChatContent{
        @SerializedName("text")
        String content_text;
        @SerializedName("img_path")
        String img_path;

        public ChatContent(String content_text, String img_path) {
            this.content_text = content_text;
            this.img_path = img_path;
        }

        public String getContent_text() {
            return content_text;
        }

        public String getImg_path() {
            return img_path;
        }
    }
}
