package info706.zikub.models;

import java.util.ArrayList;
import java.util.List;

public class PlayList {
    private int id;
    private String created_at;
    private String updated_at;
    private String name;
    private String description;
    private int user_id;
    private List<Music> musics;
    private User user;

    public PlayList() {
        musics = new ArrayList<>();
    }

    public PlayList(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public int getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getUser_id() {
        return user_id;
    }

    public User getUser() {
        return user;
    }

    public void fill(PlayList playList)
    {
        id = playList.getId();
        created_at = playList.getCreated_at();
        updated_at = playList.getCreated_at();
        name = playList.getName();
        description = playList.getDescription();
        for(int i=0; i<getMusics().size(); ++i)
            getMusics().set(i, playList.getMusics().get(i));
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "id=" + id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", user_id=" + user_id +
                ", musics=" + musics +
                '}';
    }
}
