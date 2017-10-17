package info706.zikub.models;

public class Music {
    private int id;
    private String uddated_at;
    private String created_at;
    private String title;
    private String author;
    private String cover;
    private String url;
    private int rank;
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

    public String getCover() {
        return cover;
    }

    public String getUrl() {
        return url;
    }

    public int getRank() {
        return rank;
    }

    public int getPlaylist_id() {
        return playlist_id;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setPlaylist_id(int playlist_id) {
        this.playlist_id = playlist_id;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", uddated_at='" + uddated_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", cover='" + cover + '\'' +
                ", url='" + url + '\'' +
                ", rank=" + rank +
                ", playlist_id=" + playlist_id +
                '}';
    }
}
