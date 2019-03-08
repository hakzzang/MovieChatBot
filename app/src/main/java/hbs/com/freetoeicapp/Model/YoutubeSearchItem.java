package hbs.com.freetoeicapp.Model;

import com.google.gson.annotations.SerializedName;

public class YoutubeSearchItem {
    @SerializedName("items")
    private YoutubeSearch[] youtubeSearch;

    public YoutubeSearch[] getYoutubeSearch() {
        return youtubeSearch;
    }

    public class YoutubeSearch {
        @SerializedName("snippet")
        private Snippet snippet;

        @SerializedName("id")
        private VideoIDs videoIds;

        public VideoIDs getVideoIds() {
            return videoIds;
        }

        public Snippet getSnippet() {
            return snippet;
        }
    }

    public class VideoIDs{
        @SerializedName("kind")
        private String kind;

        @SerializedName("videoId")
        private String videoId;

        @SerializedName("channelId")
        private String channelId;

        @SerializedName("playlistId")
        private String playlistId;

        public String getKind() {
            return kind;
        }

        public String getVideoId() {
            return videoId;
        }

        public String getChannelId() {
            return channelId;
        }

        public String getPlaylistId() {
            return playlistId;
        }
    }

    public class Snippet {
        @SerializedName("publishedAt")
        private String publishedAt;

        @SerializedName("channelId")
        private String channelId;

        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("channelTitle")
        private String channelTitle;

        @SerializedName("thumbnails")
        private Thumbnails thumbnails;

        public String getChannelTitle() {
            return channelTitle;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public String getChannelId() {
            return channelId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Thumbnails getThumbnails() {
            return thumbnails;
        }
    }

    public class Thumbnails {
        @SerializedName("default")
        private Thumbnail defaultThumbnail;

        @SerializedName("medium")
        private Thumbnail mediumThumbnail;

        @SerializedName("high")
        private Thumbnail highThumbnail;

        public Thumbnail getDefaultThumbnail() {
            return defaultThumbnail;
        }

        public Thumbnail getMediumThumbnail() {
            return mediumThumbnail;
        }

        public Thumbnail getHighThumbnail() {
            return highThumbnail;
        }
    }

    public class Thumbnail {
        @SerializedName("url")
        private String url;

        @SerializedName("width")
        private int width;

        @SerializedName("height")
        private int height;

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
