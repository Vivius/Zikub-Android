package info706.zikub.adapters;

import info706.zikub.models.Music;

public interface MusicEditionAdapterListener {
    void onEditMusic(Music music);
    void onDeleteMusic(Music music);
}
