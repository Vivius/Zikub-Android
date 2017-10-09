package info706.zikub.models;

public class Music {
    private int id;
    private String uddated_at;
    private String created_at;
    private String title;
    private String author;
    private int duration;
    private String cover;
    private String provider;
    private String url;
    private int playlist_id;

    public int getId() {
        return id;
    }

    public String getUddated_at() {
        return uddated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getDuration() {
        return duration;
    }

    public String getCover() {
        return cover;
    }

    public String getProvider() {
        return provider;
    }

    public String getUrl() {
        return url;
    }

    public int getPlaylist_id() {
        return playlist_id;
    }
}
